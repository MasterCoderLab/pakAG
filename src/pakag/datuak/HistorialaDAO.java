package pakag.datuak;

import pakag.eredua.Historiala;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * HistorialaDAO klasea aplikazioan historiala erakusteko erabiltzen da.
 *
 * Datu-basean ez dago Historiala izeneko taula fisikorik.
 * Historiala Paketea eta Entrega taulen arteko loturatik sortzen da,
 * INNER JOIN baten bidez.
 *
 * Horrela, paketeen eta entregen informazio historikoa erakusten da
 * datuak bikoiztu gabe.
 */
public class HistorialaDAO {

    /**
     * Paketeekin lotutako entrega guztien historiala lortzen du.
     *
     * Metodo honek Paketea eta Entrega taulak elkartzen ditu.
     * Pakete bakoitzari dagokion entrega hartu, eta informazio bateratua
     * Historiala objektuetan gordetzen du.
     *
     * @return historialaren zerrenda
     */
    public List<Historiala> lortuGuztiak() {
        List<Historiala> zerrenda = new ArrayList<>();

        /*
         * Historiala ez da datu-baseko taula fisiko bat.
         * Paketea eta Entrega taulen arteko JOIN baten bidez sortzen da.
         *
         * INNER JOIN erabiltzen da, historialean soilik entrega batekin
         * lotuta dauden paketeak erakusteko.
         *
         * ORDER BY erabilita, azken entregak lehenengo agertzen dira.
         */
        String sql = "SELECT e.entrega_data, p.ID_P, e.ID_E, p.Bezeroa_ID_Be, " +
                "e.Banatzailea_ID_Ba, e.egoera, p.herria, p.helbidea " +
                "FROM Paketea p " +
                "INNER JOIN Entrega e ON p.Entrega_ID_E = e.ID_E " +
                "ORDER BY e.entrega_data DESC, e.ID_E DESC";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            /*
             * Kontsultaren emaitza errenkadaz errenkada irakurtzen da.
             * Errenkada bakoitza Historiala objektu bihurtzen da.
             */
            while (rs.next()) {
                Historiala h = new Historiala();

                // SQL Date objektua Java-ko LocalDate bihurtzen da.
                Date data = rs.getDate("entrega_data");
                if (data != null) {
                    h.setEntregaDate(data.toLocalDate());
                }

                // Pakete eta entrega identifikatzaileak gordetzen dira.
                h.setPaketeId(rs.getString("ID_P"));
                h.setEntregaId(rs.getInt("ID_E"));

                /*
                 * Bezeroa_ID_Be NULL izan daiteke.
                 * getInt-ek 0 itzul dezakeenez, rs.wasNull() erabiltzen da
                 * benetan NULL den jakiteko.
                 */
                int bezeroId = rs.getInt("Bezeroa_ID_Be");
                h.setBezeroId(rs.wasNull() ? null : bezeroId);

                /*
                 * Banatzailea_ID_Ba ere NULL izan daiteke,
                 * entrega bat oraindik banatzaile bati esleitu gabe badago.
                 */
                int banatzaileaId = rs.getInt("Banatzailea_ID_Ba");
                h.setBanatzaileaId(rs.wasNull() ? null : banatzaileaId);

                // Entregaren egoera eta paketearen helmuga-datuak gordetzen dira.
                h.setEgoera(rs.getString("egoera"));
                h.setHerria(rs.getString("herria"));
                h.setHelbidea(rs.getString("helbidea"));

                zerrenda.add(h);
            }

        } catch (Exception e) {
            System.out.println("Errorea historiala lortzean: " + e.getMessage());
        }

        return zerrenda;
    }
}