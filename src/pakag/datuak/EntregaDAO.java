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
 * EntregaDAO klasea datu-basearekin komunikatzeko erabiltzen da.
 */
public class EntregaDAO {

    public int gehituEtaIdItzuli(Entrega entrega) {
        String sql = "INSERT INTO Entrega (entrega_data, egoera, mezua, Banatzailea_ID_Ba) VALUES (?, ?, ?, ?)";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (entrega.getEntregaDate() == null) {
                pst.setDate(1, Date.valueOf(java.time.LocalDate.now()));
            } else {
                pst.setDate(1, Date.valueOf(entrega.getEntregaDate()));
            }

            pst.setString(2, entrega.getEgoera());
            pst.setString(3, entrega.getMezua());

            if (entrega.getBanatzaileaIdBa() == null) {
                pst.setNull(4, java.sql.Types.INTEGER);
            } else {
                pst.setInt(4, entrega.getBanatzaileaIdBa());
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
            System.out.println("Errorea entrega gehitzean: " + e.getMessage());
        }

        return -1;
    }

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

    public boolean editatu(Entrega entrega) {
        String sql = "UPDATE Entrega SET entrega_data = ?, egoera = ?, mezua = ?, Banatzailea_ID_Ba = ? WHERE ID_E = ?";

        try (Connection con = Konexioa.lortuKonexioa();
             PreparedStatement pst = con.prepareStatement(sql)) {

            if (entrega.getEntregaDate() == null) {
                pst.setDate(1, Date.valueOf(java.time.LocalDate.now()));
            } else {
                pst.setDate(1, Date.valueOf(entrega.getEntregaDate()));
            }

            pst.setString(2, entrega.getEgoera());
            pst.setString(3, entrega.getMezua());

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

    public boolean ezabatu(int idE) {
        String sqlPakete = "UPDATE Paketea SET Entrega_ID_E = NULL WHERE Entrega_ID_E = ?";
        String sqlEntrega = "DELETE FROM Entrega WHERE ID_E = ?";

        try (Connection con = Konexioa.lortuKonexioa()) {
            con.setAutoCommit(false);

            try (PreparedStatement pst1 = con.prepareStatement(sqlPakete);
                 PreparedStatement pst2 = con.prepareStatement(sqlEntrega)) {

                pst1.setInt(1, idE);
                pst1.executeUpdate();

                pst2.setInt(1, idE);
                int filas = pst2.executeUpdate();

                con.commit();
                return filas > 0;

            } catch (Exception e) {
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

    private Entrega mapEntrega(ResultSet rs) throws Exception {
        Entrega e = new Entrega();

        e.setIdE(rs.getInt("ID_E"));

        Date data = rs.getDate("entrega_data");
        if (data != null) {
            e.setEntregaDate(data.toLocalDate());
        }

        e.setEgoera(rs.getString("egoera"));
        e.setMezua(rs.getString("mezua"));

        int banatzaileaId = rs.getInt("Banatzailea_ID_Ba");
        e.setBanatzaileaIdBa(rs.wasNull() ? null : banatzaileaId);

        e.setPaketeId(rs.getString("ID_P"));

        return e;
    }

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