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
 * PaketeaDAO klasea datu-basearekin komunikatzeko erabiltzen da.
 * Hemen paketeen CRUD eragiketak eta kontsulta nagusiak egiten dira.
 */
public class PaketeaDAO {

    /**
     * Pakete berri bat datu-basean sartzen du eta sortutako ID-a itzultzen du.
     *
     * @param paketea gehitu nahi den paketea
     * @return sortutako paketearen ID-a; errorea bada, -1
     */
    public int gehituEtaIdItzuli(Paketea paketea) {
        String sql = "INSERT INTO Paketea (pisua, edukia, herria, helbidea, sarrera_data, Bezeroa_ID_Be, Entrega_ID_E) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

            int filas = pst.executeUpdate();

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
     * @param paketeId paketearen ID-a
     * @param entregaId entregaren ID-a
     * @return true ondo joan bada, false bestela
     */
    public boolean entregaEsleitu(String paketeId, int entregaId) {
        String sql = "UPDATE Paketea SET Entrega_ID_E = ? WHERE ID_P = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, entregaId);
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
     * @param rs ResultSet
     * @return Paketea objektua
     * @throws Exception errorea gertatuz gero
     */
    private Paketea mapPaketea(ResultSet rs) throws Exception {
        Paketea p = new Paketea();

        p.setIdP(rs.getString("ID_P"));
        p.setPisua(rs.getString("pisua"));
        p.setEdukia(rs.getString("edukia"));
        p.setHerria(rs.getString("herria"));
        p.setHelbidea(rs.getString("helbidea"));
        p.setSarreraData(rs.getDate("sarrera_data").toLocalDate());

        int bezeroId = rs.getInt("Bezeroa_ID_Be");
        if (rs.wasNull()) {
            p.setBezeroaIdBe(null);
        } else {
            p.setBezeroaIdBe(bezeroId);
        }

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