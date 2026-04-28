package pakag.datuak;

import pakag.eredua.Traza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * TrazaDAO klasea sistemako trazak datu-basean kudeatzeko erabiltzen da.
 * Trazek aplikazioan egindako ekintza garrantzitsuak gordetzen dituzte.
 */
public class TrazaDAO {

    /**
     * Traza berri bat datu-basean sartzen du.
     *
     * @param ekintza egindako ekintzaren izena
     * @param deskribapena ekintzaren azalpena
     * @return true ondo joan bada, false bestela
     */
    public boolean gehitu(String ekintza, String deskribapena) {
        String sql = "INSERT INTO Traza (data_ordua, ekintza, deskribapena) VALUES (NOW(), ?, ?)";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, ekintza);
            pst.setString(2, deskribapena);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Errorea traza gehitzean: " + e.getMessage());
            return false;
        }
    }

    /**
     * Datu-baseko traza guztiak lortzen ditu.
     *
     * @return trazen zerrenda
     */
    public List<Traza> lortuGuztiak() {
        List<Traza> zerrenda = new ArrayList<>();
        String sql = "SELECT * FROM Traza ORDER BY data_ordua DESC";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Traza t = new Traza();
                t.setIdT(rs.getInt("ID_T"));

                Timestamp ts = rs.getTimestamp("data_ordua");
                if (ts != null) {
                    t.setDataHora(ts.toLocalDateTime());
                }

                t.setEkintza(rs.getString("ekintza"));
                t.setDeskribapena(rs.getString("deskribapena"));

                zerrenda.add(t);
            }

        } catch (Exception e) {
            System.out.println("Errorea trazen zerrenda lortzean: " + e.getMessage());
        }

        return zerrenda;
    }

    /**
     * Datu-baseko traza guztien kopurua kalkulatzen du.
     *
     * @return traza kopurua
     */
    public int kontatuGuztiak() {
        String sql = "SELECT COUNT(*) FROM Traza";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Errorea trazak kontatzean: " + e.getMessage());
        }

        return 0;
    }
}