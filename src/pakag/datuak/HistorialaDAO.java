package pakag.datuak;

import pakag.eredua.Historiala;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * HistorialaDAO klasea datu-baseko historialaren informazioa lortzeko erabiltzen da.
 * Historiala Paketea eta Entrega taulen arteko loturatik sortzen da.
 */
public class HistorialaDAO {

    /**
     * Paketeekin lotutako entrega guztien historiala lortzen du.
     *
     * @return historialaren zerrenda
     */
    public List<Historiala> lortuGuztiak() {
        List<Historiala> zerrenda = new ArrayList<>();

        String sql = "SELECT e.entrega_data, p.ID_P, e.ID_E, p.Bezeroa_ID_Be, " +
                "e.Banatzailea_ID_Ba, e.egoera, p.herria, p.helbidea " +
                "FROM Paketea p " +
                "INNER JOIN Entrega e ON p.Entrega_ID_E = e.ID_E " +
                "ORDER BY e.entrega_data DESC, e.ID_E DESC";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Historiala h = new Historiala();

                Date data = rs.getDate("entrega_data");
                if (data != null) {
                    h.setEntregaDate(data.toLocalDate());
                }

                h.setPaketeId(rs.getString("ID_P"));
                h.setEntregaId(rs.getInt("ID_E"));

                int bezeroId = rs.getInt("Bezeroa_ID_Be");
                h.setBezeroId(rs.wasNull() ? null : bezeroId);

                int banatzaileaId = rs.getInt("Banatzailea_ID_Ba");
                h.setBanatzaileaId(rs.wasNull() ? null : banatzaileaId);

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