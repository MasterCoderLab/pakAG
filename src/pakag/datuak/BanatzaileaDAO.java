package pakag.datuak;

import pakag.eredua.Banatzailea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * BanatzaileaDAO klasea datu-basearekin komunikatzeko erabiltzen da.
 * Hemen CRUD eragiketak egiten dira:
 * - Gehitu
 * - Lortu (guztiak)
 * - Editatu
 * - Ezabatu
 */
public class BanatzaileaDAO {

    /**
     * Banatzaile berri bat datu-basean sartzen du eta sortutako ID-a itzultzen du.
     *
     * @param banatzailea gehitu nahi den banatzailea
     * @return sortutako banatzailearen ID-a; errorea bada, -1
     */
    public int gehituEtaIdItzuli(Banatzailea banatzailea) {
        String sql = "INSERT INTO Banatzailea (izena, abizena, nan) VALUES (?, ?, ?)";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, banatzailea.getIzena());
            pst.setString(2, banatzailea.getAbizena());
            pst.setString(3, banatzailea.getNan());

            int filas = pst.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Errorea banatzailea gehitzean: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Datu-baseko banatzaile guztiak lortzen ditu.
     *
     * @return banatzaileen zerrenda
     */
    public List<Banatzailea> lortuGuztiak() {
        List<Banatzailea> zerrenda = new ArrayList<>();
        String sql = "SELECT * FROM Banatzailea";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Banatzailea b = new Banatzailea();
                b.setIdBa(rs.getInt("ID_Ba"));
                b.setIzena(rs.getString("izena"));
                b.setAbizena(rs.getString("abizena"));
                b.setNan(rs.getString("nan"));
                zerrenda.add(b);
            }

        } catch (Exception e) {
            System.out.println("Errorea zerrenda lortzean: " + e.getMessage());
        }

        return zerrenda;
    }

    /**
     * Dagoen banatzaile baten datuak eguneratzen ditu.
     *
     * @param banatzailea eguneratu nahi den banatzailea
     * @return true ondo joan bada, false bestela
     */
    public boolean editatu(Banatzailea banatzailea) {
        String sql = "UPDATE Banatzailea SET izena = ?, abizena = ?, nan = ? WHERE ID_Ba = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, banatzailea.getIzena());
            pst.setString(2, banatzailea.getAbizena());
            pst.setString(3, banatzailea.getNan());
            pst.setInt(4, banatzailea.getIdBa());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea editatzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * Banatzaile bat datu-basetik ezabatzen du IDaren arabera.
     *
     * @param idBa ezabatu nahi den banatzailearen ID-a
     * @return true ondo joan bada, false bestela
     */
    public boolean ezabatu(int idBa) {
        String sql = "DELETE FROM Banatzailea WHERE ID_Ba = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, idBa);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea ezabatzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * Datu-baseko banatzaile guztien kopurua kalkulatzen du.
     *
     * @return banatzaile kopurua
     */
    public int kontatuGuztiak() {
        String sql = "SELECT COUNT(*) FROM Banatzailea";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Errorea banatzaileak kontatzean: " + e.getMessage());
        }

        return 0;
    }
}