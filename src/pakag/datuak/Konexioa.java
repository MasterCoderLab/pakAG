package pakag.datuak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Konexioa {

    private static final String URL = "jdbc:mysql://localhost:3306/pakag_db?useSSL=false&serverTimezone=UTC";
    private static final String ERABILTZAILEA = "root";
    private static final String PASAHITZA = "mysql";

    public static Connection lortuKonexioa() throws SQLException {
        return DriverManager.getConnection(URL, ERABILTZAILEA, PASAHITZA);
    }
}