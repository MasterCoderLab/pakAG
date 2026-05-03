package pakag.datuak;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * KontuaDAO klaseak banatzaileen kontuak kudeatzen ditu.
 *
 * Kontua taula Banatzailea taularekin lotuta dago Banatzailea_ID_Ba bidez.
 * Datu-basean eremu hori UNIQUE denez, banatzaile bakoitzak kontu bakarra izan dezake.
 *
 * Kudeatzaileak banatzaile bat sortzen duenean, aplikazioak automatikoki
 * kontu bat sortzen dio web aplikazioan sartu ahal izateko.
 */
public class KontuaDAO {

    /**
     * Kontu berri bat datu-basean sartzen du.
     *
     * Proiektuan kontuaren erabiltzailea banatzailearen NAN-a da,
     * eta pasahitza aplikazioak automatikoki sortzen du.
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

            // Kontuaren erabiltzailea gordetzen da.
            pst.setString(1, erabiltzailea);

            // Kontuaren pasahitza gordetzen da.
            // Benetako sistema batean pasahitza hash moduan gordeko litzateke.
            pst.setString(2, pasahitza);

            // Kontua banatzaile zehatz bati lotzen zaio.
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
     * Metodo hau banatzaile bat ezabatzeko prozesuan erabiltzen da.
     * Lehenengo kontua ezabatzen da, eta ondoren banatzailea ezabatzen saiatzen da.
     *
     * Banatzaileak historialean entregak baditu, baliteke banatzailea ezin ezabatzea,
     * baina kontua ezabatuta geratzen da web aplikaziora sarbidea kentzeko.
     *
     * @param banatzaileaId banatzailearen ID-a
     * @return true ondo joan bada, false bestela
     */
    public boolean ezabatuBanatzailearenKontua(int banatzaileaId) {
        String sql = "DELETE FROM Kontua WHERE Banatzailea_ID_Ba = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Banatzailearen ID-aren bidez bere kontua bilatu eta ezabatzen da.
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
     * Banatzailearen NAN-a aldatzen bada, bere kontuko erabiltzailea ere
     * eguneratzen da. Proiektuan kontuaren erabiltzailea banatzailearen NAN-a da.
     *
     * Horrela, Banatzailea eta Kontua taulen arteko informazioa sinkronizatuta
     * mantentzen da.
     *
     * @param banatzaileaId banatzailearen ID-a
     * @param erabiltzailea erabiltzaile izen berria
     * @return true ondo joan bada, false bestela
     */
    public boolean erabiltzaileaEguneratu(int banatzaileaId, String erabiltzailea) {
        String sql = "UPDATE Kontua SET erabiltzailea = ? WHERE Banatzailea_ID_Ba = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            // Erabiltzaile berria normalean banatzailearen NAN berria izango da.
            pst.setString(1, erabiltzailea);

            // Zein banatzaileren kontua eguneratu behar den adierazten da.
            pst.setInt(2, banatzaileaId);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea kontuaren erabiltzailea eguneratzean: " + e.getMessage());
            return false;
        }
    }
}