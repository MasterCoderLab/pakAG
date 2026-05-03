package pakag.datuak;

import pakag.eredua.Entrega;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * EntregaDAO klasea datu-baseko Entrega taularekin komunikatzeko erabiltzen da.
 *
 * Klase honetan entregen CRUD eragiketak egiten dira:
 * - Gehitu
 * - Lortu
 * - Editatu
 * - Ezabatu
 *
 * Gainera, entrega eta paketeen arteko lotura kudeatzen da,
 * eta banatzaile batek entrega aktiboak dituen egiaztatzen da.
 */
public class EntregaDAO {

    /**
     * Entrega berri bat datu-basean sartzen du eta sortutako ID-a itzultzen du.
     *
     * Entrega bat sortzean banatzailea hutsik egon daiteke,
     * hasieran entregak "pendiente" egoeran egon daitezkeelako
     * eta gero kudeatzaileak banatzaile bati esleitu ahal diolako.
     *
     * @param entrega gehitu nahi den entrega
     * @return sortutako entregaren ID-a; errorea bada, -1
     */
    public int gehituEtaIdItzuli(Entrega entrega) {
        String sql = "INSERT INTO Entrega (entrega_data, egoera, mezua, Banatzailea_ID_Ba) VALUES (?, ?, ?, ?)";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Entregak datarik ez badu, gaurko data jartzen da automatikoki.
            if (entrega.getEntregaDate() == null) {
                pst.setDate(1, Date.valueOf(java.time.LocalDate.now()));
            } else {
                pst.setDate(1, Date.valueOf(entrega.getEntregaDate()));
            }

            pst.setString(2, entrega.getEgoera());
            pst.setString(3, entrega.getMezua());

            /*
             * Banatzailea_ID_Ba NULL izan daiteke.
             * Horrek esan nahi du entrega oraindik ez dagoela banatzaile bati esleituta.
             */
            if (entrega.getBanatzaileaIdBa() == null) {
                pst.setNull(4, java.sql.Types.INTEGER);
            } else {
                pst.setInt(4, entrega.getBanatzaileaIdBa());
            }

            int filas = pst.executeUpdate();

            // INSERT ondo egin bada, datu-baseak sortutako ID-a jasotzen da.
            if (filas > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Errorea entrega gehitzean: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Entrega guztiak lortzen ditu.
     *
     * LEFT JOIN baten bidez, entrega bakoitzari dagokion pakete ID-a ere lortzen da.
     * LEFT JOIN erabiltzen da entrega batek une jakin batean paketerik lotuta ez badu ere
     * zerrendan agertu ahal izateko.
     *
     * @return entregen zerrenda
     */
    public List<Entrega> lortuGuztiak() {
        List<Entrega> zerrenda = new ArrayList<>();

        String sql = "SELECT e.ID_E, e.entrega_data, e.egoera, e.mezua, e.Banatzailea_ID_Ba, p.ID_P " +
                "FROM Entrega e " +
                "LEFT JOIN Paketea p ON p.Entrega_ID_E = e.ID_E " +
                "ORDER BY e.ID_E ASC";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                zerrenda.add(mapEntrega(rs));
            }

        } catch (Exception e) {
            System.out.println("Errorea entregen zerrenda lortzean: " + e.getMessage());
        }

        return zerrenda;
    }

    /**
     * Dagoen entrega baten datuak eguneratzen ditu.
     *
     * Entregaren data, egoera, mezua eta banatzailea aldatu daitezke.
     * Banatzailea hutsik uzten bada, entrega esleitu gabe geratzen da.
     *
     * @param entrega eguneratu nahi den entrega
     * @return true ondo joan bada, false bestela
     */
    public boolean editatu(Entrega entrega) {
        String sql = "UPDATE Entrega SET entrega_data = ?, egoera = ?, mezua = ?, Banatzailea_ID_Ba = ? WHERE ID_E = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Datarik ez badago, gaurko data erabiltzen da.
            if (entrega.getEntregaDate() == null) {
                pst.setDate(1, Date.valueOf(java.time.LocalDate.now()));
            } else {
                pst.setDate(1, Date.valueOf(entrega.getEntregaDate()));
            }

            pst.setString(2, entrega.getEgoera());
            pst.setString(3, entrega.getMezua());

            // Banatzailea aukeratu ez bada, datu-basean NULL gordetzen da.
            if (entrega.getBanatzaileaIdBa() == null) {
                pst.setNull(4, java.sql.Types.INTEGER);
            } else {
                pst.setInt(4, entrega.getBanatzaileaIdBa());
            }

            pst.setInt(5, entrega.getIdE());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea entrega editatzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * Entrega bat ezabatzen du.
     *
     * Lehenengo Paketea taulan entrega horrekin dagoen lotura kentzen da,
     * Entrega_ID_E eremua NULL bihurtuz. Ondoren, entrega bera ezabatzen da.
     *
     * Bi eragiketak transakzio batean egiten dira:
     * - Dena ondo badoa, commit egiten da.
     * - Errorea gertatzen bada, rollback egiten da.
     *
     * Horrela datu-basearen koherentzia mantentzen da.
     *
     * @param idE ezabatu nahi den entregaren ID-a
     * @return true ondo ezabatu bada, false bestela
     */
    public boolean ezabatu(int idE) {
        String sqlPakete = "UPDATE Paketea SET Entrega_ID_E = NULL WHERE Entrega_ID_E = ?";
        String sqlEntrega = "DELETE FROM Entrega WHERE ID_E = ?";

        try (Connection con = Konexioa.lortuKonexioa()) {
            con.setAutoCommit(false);

            try (PreparedStatement pst1 = con.prepareStatement(sqlPakete);
                 PreparedStatement pst2 = con.prepareStatement(sqlEntrega)) {

                // 1. Paketearekin dagoen lotura kentzen da.
                pst1.setInt(1, idE);
                pst1.executeUpdate();

                // 2. Entrega ezabatzen da.
                pst2.setInt(1, idE);
                int filas = pst2.executeUpdate();

                // Bi eragiketak ondo joan badira, aldaketak gordetzen dira.
                con.commit();
                return filas > 0;

            } catch (Exception e) {
                // Errorea badago, egindako aldaketak atzera botatzen dira.
                con.rollback();
                System.out.println("Errorea entrega ezabatzean: " + e.getMessage());
                return false;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            System.out.println("Errorea entrega ezabatzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * Pakete bat entrega batekin lotzen du.
     *
     * Metodo hau entrega automatikoa sortu ondoren erabiltzen da.
     * Paketea taulako Entrega_ID_E eremuan sortutako entregaren ID-a gordetzen da.
     *
     * @param paketeId lotu nahi den paketearen ID-a
     * @param entregaId lotu nahi den entregaren ID-a
     * @return true lotura ondo egin bada, false bestela
     */
    public boolean paketeaEsleitu(String paketeId, int entregaId) {
        String sql = "UPDATE Paketea SET Entrega_ID_E = ? WHERE ID_P = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, entregaId);
            pst.setString(2, paketeId);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea paketea entregari esleitzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * ResultSet bateko datuak Entrega objektu batera pasatzen ditu.
     *
     * Metodo hau erabiltzen da SQL kontsultaren emaitza Java objektu
     * bihurtzeko eta kodea ez errepikatzeko.
     *
     * @param rs SQL kontsultaren emaitza
     * @return Entrega objektua
     * @throws Exception ResultSet irakurtzean errorea gertatzen bada
     */
    private Entrega mapEntrega(ResultSet rs) throws Exception {
        Entrega e = new Entrega();

        e.setIdE(rs.getInt("ID_E"));

        Date data = rs.getDate("entrega_data");
        if (data != null) {
            e.setEntregaDate(data.toLocalDate());
        }

        e.setEgoera(rs.getString("egoera"));
        e.setMezua(rs.getString("mezua"));

        /*
         * getInt-ek 0 itzul dezake NULL balioetan.
         * Horregatik rs.wasNull() erabiltzen da benetan NULL den jakiteko.
         */
        int banatzaileaId = rs.getInt("Banatzailea_ID_Ba");
        e.setBanatzaileaIdBa(rs.wasNull() ? null : banatzaileaId);

        // LEFT JOIN bidez lortutako paketearen ID-a gordetzen da.
        e.setPaketeId(rs.getString("ID_P"));

        return e;
    }

    /**
     * Datu-baseko entrega guztien kopurua kalkulatzen du.
     *
     * Metodo hau hasierako dashboard-ean erabiltzen da.
     *
     * @return entrega kopurua
     */
    public int kontatuGuztiak() {
        String sql = "SELECT COUNT(*) FROM Entrega";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Errorea entregak kontatzean: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Banatzaile batek entrega aktiboak dituen egiaztatzen du.
     *
     * Metodo hau banatzaile bat ezabatu aurretik erabiltzen da.
     * Banatzaileak entrega aktiboak baditu, ez da ezabatzen,
     * datu-basearen eta sistemaren koherentzia mantentzeko.
     *
     * Entrega aktibotzat hartzen dira oraindik amaitu ez diren entregak.
     *
     * @param banatzaileaId banatzailearen ID-a
     * @return true entrega aktiboak baditu, false bestela
     */
    public boolean banatzaileakEntregaAktiboakDitu(int banatzaileaId) {
        String sql = """
            SELECT COUNT(*)
            FROM Entrega
            WHERE Banatzailea_ID_Ba = ?
            AND egoera IN ('pendiente', 'esleituta', 'bidean', 'atzeratuta')
            """;

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, banatzaileaId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            System.out.println("Errorea entrega aktiboak egiaztatzean: " + e.getMessage());
        }

        return false;
    }
}