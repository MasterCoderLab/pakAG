package pakag.datuak;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Konexioa klasea datu-basearekin konexioa sortzeko erabiltzen da.
 *
 * Aplikazioko DAO klase guztiek klase honetako lortuKonexioa() metodoa
 * erabiltzen dute MySQL datu-basearekin konektatzeko.
 *
 * Horrela, konexioaren datuak leku bakarrean daude:
 * URL-a, erabiltzailea eta pasahitza.
 */
public class Konexioa {

    /*
     * MySQL datu-basearen helbidea.
     *
     * localhost erabiltzen da datu-basea ordenagailu berean dagoelako.
     * 3306 MySQL-ren portu lehenetsia da.
     * pakag_db gure proiektuaren datu-basearen izena da.
     */
    private static final String URL =
            "jdbc:mysql://localhost:3306/pakag_db?useSSL=false&serverTimezone=UTC";

    // Datu-basera konektatzeko erabiltzailea.
    private static final String ERABILTZAILEA = "root";

    // Datu-basera konektatzeko pasahitza.
    private static final String PASAHITZA = "mysql";

    /**
     * MySQL datu-basearekin konexio berri bat sortzen du.
     *
     * Metodo hau estatikoa da, DAO klaseek objekturik sortu gabe
     * zuzenean erabili ahal izateko:
     *
     * Konexioa.lortuKonexioa()
     *
     * @return datu-basearekin sortutako konexioa
     * @throws SQLException konexioa sortzean errorea gertatzen bada
     */
    public static Connection lortuKonexioa() throws SQLException {
        return DriverManager.getConnection(URL, ERABILTZAILEA, PASAHITZA);
    }
}