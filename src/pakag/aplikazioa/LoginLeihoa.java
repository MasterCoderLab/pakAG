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
 * Kudeatzailearen saioa hasteko leihoa sortzen du.
 * Login lokala da eta ez du datu-basearekin lan egiten.
 */
public class LoginLeihoa {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "pakag2026";

    /**
     * Login leihoa erakusten du.
     *
     * @param stage JavaFX-eko leiho nagusia
     * @param onLoginOk login zuzena denean exekutatuko den ekintza
     */
    public void erakutsi(Stage stage, Runnable onLoginOk) {
        Image logoIrudia = new Image(getClass().getResourceAsStream("/pakag/irudiak/logo.png"));
        ImageView logoView = new ImageView(logoIrudia);
        logoView.setFitWidth(230);
        logoView.setPreserveRatio(true);

        Label titulua = new Label("Kudeatzailearen sarbidea");
        titulua.getStyleClass().add("login-titulua");

        TextField erabiltzaileaEremua = new TextField();
        erabiltzaileaEremua.setPromptText("Erabiltzailea");

        PasswordField pasahitzaEremua = new PasswordField();
        pasahitzaEremua.setPromptText("Pasahitza");

        Label erroreMezua = new Label();
        erroreMezua.getStyleClass().add("login-error");

        Button sartuBtn = new Button("Sartu");
        sartuBtn.getStyleClass().add("btn-primary");

        Button laguntzaBtn = new Button("Pasahitza ahaztu duzu?");
        laguntzaBtn.getStyleClass().add("btn-secondary");

        laguntzaBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pasahitza berreskuratu");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Pasahitza berreskuratzeko, sistemaren arduradunarekin harremanetan jarri."
            );
            alert.showAndWait();
        });

        Runnable saioaEgiaztatu = () -> {
            String erabiltzailea = erabiltzaileaEremua.getText().trim();
            String pasahitza = pasahitzaEremua.getText().trim();

            if (ADMIN_USER.equals(erabiltzailea) && ADMIN_PASS.equals(pasahitza)) {
                onLoginOk.run();
            } else {
                erroreMezua.setText("Erabiltzailea edo pasahitza ez da zuzena.");
                pasahitzaEremua.clear();
            }
        };

        sartuBtn.setOnAction(e -> saioaEgiaztatu.run());
        pasahitzaEremua.setOnAction(e -> saioaEgiaztatu.run());

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

        StackPane erroa = new StackPane(loginKutxa);
        erroa.getStyleClass().add("login-erroa");

        Scene escena = new Scene(erroa, 460, 430);
        escena.getStylesheets().add(
                getClass().getResource("/pakag/estiloa/estiloa.css").toExternalForm()
        );

        stage.setTitle("pakAG - Saioa hasi");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();
    }
}