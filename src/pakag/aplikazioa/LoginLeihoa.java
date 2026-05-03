package pakag.aplikazioa;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

/**
 * Kudeatzailearen saioa hasteko leihoa sortzen duen klasea.
 *
 * Leiho hau aplikazio nagusia ireki aurretik agertzen da.
 * Login hau lokala da, hau da, ez du datu-basearekin konexiorik egiten.
 *
 * Proiektu honetan horrela egin da, enuntziatuan kudeatzailearen aplikazioa
 * makina bakarrean instalatuta egongo dela adierazten delako.
 */
public class LoginLeihoa {

    // Kudeatzailearen erabiltzaile eta pasahitz lokalak.
    // Benetako sistema batean datu hauek ez lirateke kodean zuzenean gordeko.
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "pakag2026";

    /**
     * Login leihoa erakusten du.
     *
     * @param stage JavaFX-eko leiho nagusia.
     * @param onLoginOk login zuzena denean exekutatuko den ekintza.
     */
    public void erakutsi(Stage stage, Runnable onLoginOk) {

        // Aplikazioaren logoa kargatzen da baliabideen karpetatik.
        Image logoIrudia = new Image(getClass().getResourceAsStream("/pakag/irudiak/logo.png"));
        ImageView logoView = new ImageView(logoIrudia);
        logoView.setFitWidth(230);
        logoView.setPreserveRatio(true);

        // Login leihoaren titulua.
        Label titulua = new Label("Kudeatzailearen sarbidea");
        titulua.getStyleClass().add("login-titulua");

        // Erabiltzailea sartzeko testu-eremua.
        TextField erabiltzaileaEremua = new TextField();
        erabiltzaileaEremua.setPromptText("Erabiltzailea");

        // Pasahitza sartzeko eremua. PasswordField erabiltzen da testua ezkutatzeko.
        PasswordField pasahitzaEremua = new PasswordField();
        pasahitzaEremua.setPromptText("Pasahitza");

        // Login okerra denean mezua hemen erakusten da.
        Label erroreMezua = new Label();
        erroreMezua.getStyleClass().add("login-error");

        // Saioa hasteko botoia.
        Button sartuBtn = new Button("Sartu");
        sartuBtn.getStyleClass().add("btn-primary");

        // Pasahitza ahaztuz gero informazio-mezua erakusteko botoia.
        Button laguntzaBtn = new Button("Pasahitza ahaztu duzu?");
        laguntzaBtn.getStyleClass().add("btn-secondary");

        // Laguntza botoiak informazio-alerta bat irekitzen du.
        laguntzaBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pasahitza berreskuratu");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Pasahitza berreskuratzeko, sistemaren arduradunarekin harremanetan jarri."
            );
            alert.showAndWait();
        });

        /*
         * Loginaren egiaztapena Runnable batean gordetzen da.
         * Horrela kode bera erabil daiteke botoia sakatzean
         * eta pasahitz-eremuan Enter sakatzean.
         */
        Runnable saioaEgiaztatu = () -> {
            String erabiltzailea = erabiltzaileaEremua.getText().trim();
            String pasahitza = pasahitzaEremua.getText().trim();

            // Erabiltzailea eta pasahitza zuzenak badira, aplikazio nagusia irekitzen da.
            if (ADMIN_USER.equals(erabiltzailea) && ADMIN_PASS.equals(pasahitza)) {
                onLoginOk.run();
            } else {
                // Datuak okerrak badira, errore-mezua erakutsi eta pasahitza garbitu.
                erroreMezua.setText("Erabiltzailea edo pasahitza ez da zuzena.");
                pasahitzaEremua.clear();
            }
        };

        // Login botoia sakatzean egiaztapena egiten da.
        sartuBtn.setOnAction(e -> saioaEgiaztatu.run());

        // Pasahitz-eremuan Enter sakatuta ere login egitea ahalbidetzen da.
        pasahitzaEremua.setOnAction(e -> saioaEgiaztatu.run());

        /*
         * Loginaren elementu guztiak VBox batean antolatzen dira.
         * Diseinu bisuala CSS fitxategian dago definituta.
         */
        VBox loginKutxa = new VBox(
                16,
                logoView,
                titulua,
                erabiltzaileaEremua,
                pasahitzaEremua,
                erroreMezua,
                sartuBtn,
                laguntzaBtn
        );

        loginKutxa.setAlignment(Pos.CENTER);
        loginKutxa.getStyleClass().add("login-kutxa");

        // Leihoaren erro nagusia.
        StackPane erroa = new StackPane(loginKutxa);
        erroa.getStyleClass().add("login-erroa");

        // Eszena sortu eta CSS fitxategia aplikatzen da.
        Scene escena = new Scene(erroa, 460, 430);
        escena.getStylesheets().add(
                getClass().getResource("/pakag/estiloa/estiloa.css").toExternalForm()
        );

        // Leihoaren konfigurazioa.
        stage.setTitle("pakAG - Saioa hasi");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();
    }
}