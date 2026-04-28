package pakag.datuak;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * KontuaDAO klasea kontuen datuak datu-basean kudeatzeko erabiltzen da.
 * Banatzaile bat sortzen denean, bere kontua automatikoki sortzeko erabiltzen da.
 */
public class KontuaDAO {

    /**
     * Kontu berri bat datu-basean sartzen du.
     *
     * @param erabiltzailea kontuaren erabiltzaile izena
     * @param pasahitza kontuaren pasahitza
     * @param banatzaileaId lotutako banatzailearen ID-a
     * @return true ondo joan bada, false bestela
     */
    public boolean gehitu(String erabiltzailea, String pasahitza, int banatzaileaId) {
        String sql = "INSERT INTO Kontua (erabiltzailea, pasahitza, Banatzailea_ID_Ba) VALUES (?, ?, ?)";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, erabiltzailea);
            pst.setString(2, pasahitza);
            pst.setInt(3, banatzaileaId);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea kontua gehitzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * Banatzaile bati lotutako kontua ezabatzen du.
     *
     * @param banatzaileaId banatzailearen ID-a
     * @return true ondo joan bada, false bestela
     */
    public boolean ezabatuBanatzailearenKontua(int banatzaileaId) {
        String sql = "DELETE FROM Kontua WHERE Banatzailea_ID_Ba = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, banatzaileaId);
            pst.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Errorea kontua ezabatzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * Banatzaile bati lotutako kontuaren erabiltzailea eguneratzen du.
     *
     * @param banatzaileaId banatzailearen ID-a
     * @param erabiltzailea erabiltzaile izen berria
     * @return true ondo joan bada, false bestela
     */
    public boolean erabiltzaileaEguneratu(int banatzaileaId, String erabiltzailea) {
        String sql = "UPDATE Kontua SET erabiltzailea = ? WHERE Banatzailea_ID_Ba = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, erabiltzailea);
            pst.setInt(2, banatzaileaId);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea kontuaren erabiltzailea eguneratzean: " + e.getMessage());
            return false;
        }
    }
}