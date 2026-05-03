package pakag.datuak;

import pakag.eredua.Bezeroa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * BezeroaDAO klaseak bezeroa taularekin lan egiten du.
 * Aplikazio nagusiak bezeroen datuak kudeatu behar dituenean,
 * klase honetako metodoak erabiltzen ditu SQL kontsultak egiteko.
 */
public class BezeroaDAO {

    /**
     * Bezero berri bat datu-basean sartzen du eta sortutako ID-a itzultzen du.
     *
     * @param bezeroa gehitu nahi den bezeroa
     * @return sortutako bezeroaren ID-a; errorea bada, -1
     */
    public int gehituEtaIdItzuli(Bezeroa bezeroa) {
        String sql = "INSERT INTO Bezeroa (izena, abizena, telefonoa, herria) VALUES (?, ?, ?, ?)";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, bezeroa.getIzena());
            pst.setString(2, bezeroa.getAbizena());
            pst.setString(3, bezeroa.getTelefonoa());
            pst.setString(4, bezeroa.getHerria());

            int filas = pst.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Errorea bezeroa gehitzean: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Datu-baseko bezero guztiak lortzen ditu.
     *
     * @return bezeroen zerrenda
     */
    public List<Bezeroa> lortuGuztiak() {
        List<Bezeroa> zerrenda = new ArrayList<>();
        String sql = "SELECT * FROM Bezeroa";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Bezeroa b = new Bezeroa();
                b.setIdBe(rs.getInt("ID_Be"));
                b.setIzena(rs.getString("izena"));
                b.setAbizena(rs.getString("abizena"));
                b.setTelefonoa(rs.getString("telefonoa"));
                b.setHerria(rs.getString("herria"));
                zerrenda.add(b);
            }

        } catch (Exception e) {
            System.out.println("Errorea bezeroen zerrenda lortzean: " + e.getMessage());
        }

        return zerrenda;
    }

    /**
     * Datu-baseko bezero guztien kopurua kalkulatzen du.
     *
     * @return bezero kopurua
     */
    public int kontatuGuztiak() {
        String sql = "SELECT COUNT(*) FROM Bezeroa";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Errorea bezeroak kontatzean: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Bezero baten datuak eguneratzen ditu.
     *
     * @param bezeroa eguneratu nahi den bezeroa
     * @return true ondo joan bada, false bestela
     */
    public boolean editatu(Bezeroa bezeroa) {
        String sql = "UPDATE Bezeroa SET izena = ?, abizena = ?, telefonoa = ?, herria = ? WHERE ID_Be = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, bezeroa.getIzena());
            pst.setString(2, bezeroa.getAbizena());
            pst.setString(3, bezeroa.getTelefonoa());
            pst.setString(4, bezeroa.getHerria());
            pst.setInt(5, bezeroa.getIdBe());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea bezeroa editatzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * Bezero bat datu-basetik ezabatzen du.
     *
     * @param idBe ezabatu nahi den bezeroaren ID-a
     * @return true ondo joan bada, false bestela
     */
    public boolean ezabatu(int idBe) {
        String sql = "DELETE FROM Bezeroa WHERE ID_Be = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idBe);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea bezeroa ezabatzean: " + e.getMessage());
            return false;
        }
    }
}
