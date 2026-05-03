package pakag.datuak;

import pakag.eredua.Paketea;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * PaketeaDAO klasea datu-baseko Paketea taularekin komunikatzeko erabiltzen da.
 *
 * Klase honetan paketeen CRUD eragiketak egiten dira:
 * - Gehitu
 * - Lortu
 * - Editatu
 * - Ezabatu
 *
 * Gainera, pakete bat entrega batekin lotzeko metodoa dauka.
 */
public class PaketeaDAO {

    /**
     * Pakete berri bat datu-basean sartzen du eta sortutako ID-a itzultzen du.
     *
     * ID_P eremua datu-basean INT AUTO_INCREMENT da, beraz aplikazioak
     * ez du eskuz ID-rik sartzen. MySQL-k automatikoki sortzen du.
     *
     * Paketea hasieran entregarako loturarik gabe sor daiteke.
     * Ondoren aplikazio nagusiak entrega automatikoa sortzen du eta
     * entregaEsleitu() metodoaren bidez paketearekin lotzen du.
     *
     * @param paketea gehitu nahi den paketea
     * @return sortutako paketearen ID-a; errorea bada, -1
     */
    public int gehituEtaIdItzuli(Paketea paketea) {
        String sql = "INSERT INTO Paketea (pisua, edukia, herria, helbidea, sarrera_data, Bezeroa_ID_Be, Entrega_ID_E) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Paketearen oinarrizko datuak gordetzen dira.
            pst.setString(1, paketea.getPisua());
            pst.setString(2, paketea.getEdukia());
            pst.setString(3, paketea.getHerria());
            pst.setString(4, paketea.getHelbidea());
            pst.setDate(5, Date.valueOf(paketea.getSarreraData()));

            /*
             * Bezeroa_ID_Be NULL izan daiteke.
             * Horrek aukera ematen du pakete bat bezeroarekin lotu gabe gordetzeko,
             * nahiz eta normalean bezero batekin lotuta egongo den.
             */
            if (paketea.getBezeroaIdBe() == null) {
                pst.setNull(6, java.sql.Types.INTEGER);
            } else {
                pst.setInt(6, paketea.getBezeroaIdBe());
            }

            /*
             * Entrega_ID_E hasieran NULL izan daiteke.
             * Paketea sortu ondoren, aplikazioak entrega automatikoa sortzen du
             * eta gero paketearekin lotzen du.
             */
            if (paketea.getEntregaIdE() == null) {
                pst.setNull(7, java.sql.Types.INTEGER);
            } else {
                pst.setInt(7, paketea.getEntregaIdE());
            }

            int filas = pst.executeUpdate();

            /*
             * INSERT ondo egin bada, datu-baseak automatikoki sortutako
             * ID_P balioa jasotzen da.
             */
            if (filas > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Errorea paketea gehitzean: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Datu-baseko pakete guztiak lortzen ditu.
     *
     * @return paketeen zerrenda
     */
    public List<Paketea> lortuGuztiak() {
        List<Paketea> zerrenda = new ArrayList<>();
        String sql = "SELECT * FROM Paketea";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                zerrenda.add(mapPaketea(rs));
            }

        } catch (Exception e) {
            System.out.println("Errorea paketeen zerrenda lortzean: " + e.getMessage());
        }

        return zerrenda;
    }

    /**
     * Dagoen pakete baten datuak eguneratzen ditu.
     *
     * ID_P ez da aldatzen, datu-baseko identifikatzaile nagusia delako.
     * Gainerako datuak eguneratzen dira: pisua, edukia, helmuga, data,
     * bezeroa eta entrega lotura.
     *
     * @param paketea eguneratu nahi den paketea
     * @return true ondo joan bada, false bestela
     */
    public boolean editatu(Paketea paketea) {
        String sql = "UPDATE Paketea SET pisua = ?, edukia = ?, herria = ?, helbidea = ?, sarrera_data = ?, "
                + "Bezeroa_ID_Be = ?, Entrega_ID_E = ? WHERE ID_P = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, paketea.getPisua());
            pst.setString(2, paketea.getEdukia());
            pst.setString(3, paketea.getHerria());
            pst.setString(4, paketea.getHelbidea());
            pst.setDate(5, Date.valueOf(paketea.getSarreraData()));

            if (paketea.getBezeroaIdBe() == null) {
                pst.setNull(6, java.sql.Types.INTEGER);
            } else {
                pst.setInt(6, paketea.getBezeroaIdBe());
            }

            if (paketea.getEntregaIdE() == null) {
                pst.setNull(7, java.sql.Types.INTEGER);
            } else {
                pst.setInt(7, paketea.getEntregaIdE());
            }

            /*
             * ID_P datu-basean INT da, baina modeloko getter-ak String itzultzen du.
             * PreparedStatement-ek balioa bidaltzen du eta MySQL-k zenbaki gisa interpretatzen du.
             */
            pst.setString(8, paketea.getIdP());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea paketea editatzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * Pakete bat datu-basetik ezabatzen du.
     *
     * ID_P datu-baseko identifikatzaile nagusia da.
     * Ezabatzean, ID horri dagokion paketea kentzen da.
     *
     * @param idP ezabatu nahi den paketearen identifikatzailea
     * @return true ondo joan bada, false bestela
     */
    public boolean ezabatu(String idP) {
        String sql = "DELETE FROM Paketea WHERE ID_P = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, idP);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea paketea ezabatzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * Oraindik entregarik ez duten paketeak lortzen ditu.
     *
     * Metodo hau erabilgarria da jakiteko zein paketek ez duten oraindik
     * entrega loturarik.
     *
     * @return esleitu gabeko paketeen zerrenda
     */
    public List<Paketea> lortuEsleituGabeak() {
        List<Paketea> zerrenda = new ArrayList<>();
        String sql = "SELECT * FROM Paketea WHERE Entrega_ID_E IS NULL ORDER BY sarrera_data ASC";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                zerrenda.add(mapPaketea(rs));
            }

        } catch (Exception e) {
            System.out.println("Errorea esleitu gabeko paketeak lortzean: " + e.getMessage());
        }

        return zerrenda;
    }

    /**
     * Pakete bati entrega ID bat esleitzen dio.
     *
     * Metodo hau entrega automatikoa sortu ondoren erabiltzen da.
     * Paketea taulako Entrega_ID_E eremuan sortutako entregaren ID-a gordetzen du.
     *
     * Horrela, paketea eta entrega 1:1 erlazioan lotuta geratzen dira.
     *
     * @param paketeId paketearen ID-a
     * @param entregaId entregaren ID-a
     * @return true ondo joan bada, false bestela
     */
    public boolean entregaEsleitu(String paketeId, int entregaId) {
        String sql = "UPDATE Paketea SET Entrega_ID_E = ? WHERE ID_P = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, entregaId);

            /*
             * ID_P datu-basean INT AUTO_INCREMENT da.
             * Hemen String moduan jasotzen da interfazeko eremuetatik datorrelako,
             * baina MySQL-k balioa zenbaki gisa erabiltzen du.
             */
            pst.setString(2, paketeId);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea paketeari entrega esleitzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * ResultSet-eko errenkada batetik Paketea objektu bat sortzen du.
     *
     * Metodo hau erabiltzen da SQL kontsulten emaitzak Java objektuetara
     * pasatzeko eta kodea ez errepikatzeko.
     *
     * @param rs SQL kontsultaren emaitza
     * @return Paketea objektua
     * @throws Exception ResultSet irakurtzean errorea gertatzen bada
     */
    private Paketea mapPaketea(ResultSet rs) throws Exception {
        Paketea p = new Paketea();

        /*
         * ID_P datu-basean INT da, baina aplikazioan String bezala gordetzen da
         * JavaFX testu-eremuekin errazago lan egiteko.
         */
        p.setIdP(rs.getString("ID_P"));

        p.setPisua(rs.getString("pisua"));
        p.setEdukia(rs.getString("edukia"));
        p.setHerria(rs.getString("herria"));
        p.setHelbidea(rs.getString("helbidea"));
        p.setSarreraData(rs.getDate("sarrera_data").toLocalDate());

        /*
         * Bezeroa_ID_Be NULL izan daiteke.
         * getInt-ek 0 itzul dezake NULL denean; horregatik rs.wasNull() erabiltzen da.
         */
        int bezeroId = rs.getInt("Bezeroa_ID_Be");
        if (rs.wasNull()) {
            p.setBezeroaIdBe(null);
        } else {
            p.setBezeroaIdBe(bezeroId);
        }

        /*
         * Entrega_ID_E ere NULL izan daiteke, paketeak oraindik entrega loturarik ez badu.
         */
        int entregaId = rs.getInt("Entrega_ID_E");
        if (rs.wasNull()) {
            p.setEntregaIdE(null);
        } else {
            p.setEntregaIdE(entregaId);
        }

        return p;
    }

    /**
     * Datu-baseko pakete guztien kopurua kalkulatzen du.
     *
     * Metodo hau hasierako dashboard-ean erabiltzen da.
     *
     * @return pakete kopurua
     */
    public int kontatuGuztiak() {
        String sql = "SELECT COUNT(*) FROM Paketea";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Errorea paketeak kontatzean: " + e.getMessage());
        }

        return 0;
    }
}