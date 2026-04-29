package pakag.aplikazioa;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

import pakag.datuak.BanatzaileaDAO;
import pakag.datuak.EntregaDAO;
import pakag.datuak.PaketeaDAO;
import pakag.eredua.Banatzailea;
import pakag.eredua.Entrega;
import pakag.eredua.Paketea;

import pakag.datuak.TrazaDAO;
import pakag.eredua.Traza;

import pakag.datuak.HistorialaDAO;
import pakag.eredua.Historiala;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;

import pakag.datuak.KontuaDAO;
import pakag.utilak.PasahitzaSortzailea;

import pakag.datuak.BezeroaDAO;
import pakag.eredua.Bezeroa;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableRow;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;


/**
 * pakAG proiektuaren kudeatzailearen aplikazio nagusia.
 * Klase honek JavaFX interfazea abiarazten du eta menu lateral baten bidez
 * atal desberdinen artean nabigatzeko aukera ematen du.
 *
 * Une honetan Banatzaileen eta Paketeen moduluek funtzionatzen dute;
 * gainerako panelak pixkanaka osatzen joango dira.
 */
public class PakAGAplikazioa extends Application {

    /** Banatzaileen datuak erakusteko taula */
    private final TableView<Banatzailea> taula = new TableView<>();
    /** Paketeen datuak erakusteko taula */
    private final TableView<Paketea> paketeTaula = new TableView<>();
    /** Entregen datuak erakusteko taula */
    private final TableView<Entrega> entregaTaula = new TableView<>();
    /** Trazen datuak erakusteko taula */
    private final TableView<Traza> trazaTaula = new TableView<>();
    /** historialaren datuak erakusteko taula */
    private final TableView<Historiala> historialaTaula = new TableView<>();
    /** Bezeroen datuak erakusteko taula */
    private final TableView<Bezeroa> bezeroTaula = new TableView<>();


    /** Banatzaileen formularioaren testu eremuak */
    private final TextField idEremua = new TextField();
    private final TextField izenaEremua = new TextField();
    private final TextField abizenaEremua = new TextField();
    private final TextField nanEremua = new TextField();

    /** Bezeroen formularioaren eremuak */
    private final TextField bezeroIdEremua = new TextField();
    private final TextField bezeroIzenaEremua = new TextField();
    private final TextField bezeroAbizenaEremua = new TextField();
    private final TextField bezeroTelefonoaEremua = new TextField();
    private final TextField bezeroHerriaEremua = new TextField();

    /** Entregen formularioaren eremuak */
    private final TextField entregaIdEremuaKud = new TextField();
    private final TextField entregaPaketeIdEremua = new TextField();
    private final DatePicker entregaDataEremua = new DatePicker();
    private final ComboBox<String> entregaEgoeraEremua = new ComboBox<>();
    private final TextField entregaMezuaEremua = new TextField();
    private final TextField entregaBanatzaileaIdEremua = new TextField();


    /** Paketeen formularioaren eremuak */
    private final TextField paketeIdEremua = new TextField();
    private final TextField pisuaEremua = new TextField();
    private final TextField edukiaEremua = new TextField();
    private final TextField herriaEremua = new TextField();
    private final TextField helbideaEremua = new TextField();
    private final DatePicker sarreraDataEremua = new DatePicker();
    private final TextField bezeroaIdEremua = new TextField();
    private final TextField entregaIdEremua = new TextField();

    /** Banatzaileekin lan egiteko DAO objektua */
    private final BanatzaileaDAO banatzaileaDAO = new BanatzaileaDAO();
    /** Paketeekin lan egiteko DAO objektua */
    private final PaketeaDAO paketeaDAO = new PaketeaDAO();
    /** Entregarekin lan egiteko DAO objektua */
    private final EntregaDAO entregaDAO = new EntregaDAO();
    /** Trazekin lan egiteko DAO objektua */
    private final TrazaDAO trazaDAO = new TrazaDAO();
    /** Historialarekin lan egiteko DAO objektua */
    private final HistorialaDAO historialaDAO = new HistorialaDAO();
    /** Kontuarekin lan egiteko DAO objektua */
    private final KontuaDAO kontuaDAO = new KontuaDAO();
    /** Bezeroekin lan egiteko DAO objektua */
    private final BezeroaDAO bezeroaDAO = new BezeroaDAO();


    /**
     * Aplikazioaren hasierako metodoa.
     * Leiho nagusia sortzen du eta menu laterala + edukia prestatzen ditu.
     *
     * @param stage JavaFX-eko leiho nagusia
     */
    @Override
    public void start(Stage stage) {
        Label titulua = new Label("pakAG - Kudeatzailea");
        titulua.getStyleClass().add("app-titulua");

        VBox goiburua = new VBox(titulua);
        goiburua.getStyleClass().add("goiburua");

        VBox menua = new VBox();
        menua.getStyleClass().add("menua");

        Button hasieraBtn = new Button("🏠  Hasiera");
        Button banatzaileakBtn = new Button("🚚  Banatzaileak");
        Button bezeroakBtn = new Button("👥  Bezeroak");
        Button paketeakBtn = new Button("📦  Paketeak");
        Button entregakBtn = new Button("🧾  Entregak");
        Button historialaBtn = new Button("📊  Historiala");
        Button trazakBtn = new Button("🕘  Trazak");
        Button irtenBtn = new Button("🚪  Irten");

        Button[] menuBotoiak = {
                hasieraBtn, banatzaileakBtn, bezeroakBtn, paketeakBtn,
                entregakBtn, historialaBtn, trazakBtn, irtenBtn
        };

        for (Button botoia : menuBotoiak) {
            botoia.setMaxWidth(Double.MAX_VALUE);
            botoia.getStyleClass().add("menu-btn");
        }

        menua.getChildren().addAll(menuBotoiak);

        BorderPane edukia = new BorderPane();
        edukia.getStyleClass().add("edukia");

        /*
         * Hasierako panela erakusten da aplikazioa irekitzean.
         */
        edukia.setCenter(sortuHasieraPanela());

        /*
         * Menu botoien ekintzak:
         * bakoitzak dagokion panela erakusten du erdiko eremuan.
         */
        hasieraBtn.setOnAction(e -> edukia.setCenter(sortuHasieraPanela()));

        banatzaileakBtn.setOnAction(e -> {
            edukia.setCenter(sortuBanatzailePanela());
            taulaKargatu();
        });

        bezeroakBtn.setOnAction(e -> edukia.setCenter(sortuBezeroPanela()));
        paketeakBtn.setOnAction(e -> edukia.setCenter(sortuPaketePanela()));
        entregakBtn.setOnAction(e -> edukia.setCenter(sortuEntregakPanela()));
        historialaBtn.setOnAction(e -> edukia.setCenter(sortuHistorialaPanela()));
        trazakBtn.setOnAction(e -> edukia.setCenter(sortuTrazaPanela()));
        irtenBtn.setOnAction(e -> stage.close());

        Label oinaTestua = new Label("© 2026 pakAG. Eskubide guztiak erreserbatuta.");
        oinaTestua.getStyleClass().add("footer-text");

        HBox oina = new HBox(oinaTestua);
        oina.setAlignment(Pos.CENTER);
        oina.getStyleClass().add("footer");

        BorderPane erroa = new BorderPane();
        erroa.getStyleClass().add("erroa");
        erroa.setTop(goiburua);
        erroa.setLeft(menua);
        erroa.setCenter(edukia);
        erroa.setBottom(oina);

        Scene escena = new Scene(erroa, 1100, 700);
        escena.getStylesheets().add(
                getClass().getResource("/pakag/estiloa/estiloa.css").toExternalForm()
        );

        stage.setTitle("pakAG Kudeatzailea");
        stage.setScene(escena);
        stage.show();
    }

    /**
     * Aplikazioaren hasierako panela sortzen du.
     * Logoarekin eta biltegiaren atzeko irudiarekin ongietorria erakusten du.
     *
     * @return hasierako panela
     */
    private StackPane sortuHasieraPanela() {
        StackPane erroa = new StackPane();
        erroa.getStyleClass().add("hasiera-panela");

        // Atzeko fondoa
        Image fondoIrudia = new Image(getClass().getResourceAsStream("/pakag/irudiak/fondo.jpg"));
        BackgroundSize bgSize = new BackgroundSize(
                100, 100, true, true, false, true
        );
        BackgroundImage bgImage = new BackgroundImage(
                fondoIrudia,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                bgSize
        );
        erroa.setBackground(new Background(bgImage));

        // Geruza iluna testua hobeto ikusteko
        Region iluna = new Region();
        iluna.getStyleClass().add("hasiera-iluna");
        iluna.prefWidthProperty().bind(erroa.widthProperty());
        iluna.prefHeightProperty().bind(erroa.heightProperty());

        // Logoa
        Image logoIrudia = new Image(getClass().getResourceAsStream("/pakag/irudiak/logo.png"));
        ImageView logoView = new ImageView(logoIrudia);
        logoView.setFitWidth(360);
        logoView.setPreserveRatio(true);

        // Titulua
        Label titulua = new Label("Logistika kudeaketa sistema");
        titulua.getStyleClass().add("hasiera-titulua");

        // Azpititulua
        Label azpititulua = new Label(
                "Paketeak, entregak, banatzailea eta bezeroak modu erraz eta profesionalean kudeatu."
        );
        azpititulua.setWrapText(true);
        azpititulua.setMaxWidth(700);
        azpititulua.setAlignment(Pos.CENTER);
        azpititulua.getStyleClass().add("hasiera-azpititulua");

        VBox testuKutxa = new VBox(8, titulua, azpititulua);
        testuKutxa.setAlignment(Pos.CENTER);
        testuKutxa.setMaxWidth(780);
        testuKutxa.getStyleClass().add("hasiera-testu-kutxa");

        // Info kutxa nagusia
        HBox cards = new HBox(20,
                sortuCard("Paketeak", paketeaDAO.kontatuGuztiak()),
                sortuCard("Entregak", entregaDAO.kontatuGuztiak()),
                sortuCard("Banatzaileak", banatzaileaDAO.kontatuGuztiak()),
                sortuCard("Bezeroak", bezeroaDAO.kontatuGuztiak())
        );

        cards.setAlignment(Pos.CENTER);
        cards.getStyleClass().add("dashboard-cards");

        VBox edukia = new VBox(20, logoView, testuKutxa, cards);
        edukia.setAlignment(Pos.CENTER);
        edukia.setMaxWidth(850);
        edukia.getStyleClass().add("hasiera-edukia");

        erroa.getChildren().addAll(iluna, edukia);
        StackPane.setAlignment(edukia, Pos.CENTER);

        return erroa;
    }

    /**
     * Dashboard-erako txartel bat sortzen du.
     *
     * @param titulua txartelaren izenburua
     * @param balioa erakutsi beharreko zenbakia
     * @return txartela VBox moduan
     */
    private VBox sortuCard(String titulua, int balioa) {
        Label title = new Label(titulua);
        title.getStyleClass().add("dashboard-card-title");

        Label value = new Label(String.valueOf(balioa));
        value.getStyleClass().add("dashboard-card-number");

        VBox box = new VBox(8, title, value);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("dashboard-card");

        return box;
    }

    /**
     * Banatzaileen kudeaketa panela sortzen du.
     * Formularioa, botoiak eta taula prestatzen ditu.
     *
     * @return Banatzaileen panela duen VBox bat
     */
    private VBox sortuBanatzailePanela() {
        Label atala = new Label("Banatzaileen kudeaketa");
        atala.getStyleClass().add("section-title");

        idEremua.setPromptText("ID");

        /*
         * ID eremua blokeatuta dagoenean (edit moduan),
         * klik egiterakoan abisu bat erakusten da.
         */
        idEremua.setOnMouseClicked(e -> {
            if (!idEremua.isEditable()) {
                mezua("Abisua", "Identifikatzailea ezin da editatu.", Alert.AlertType.INFORMATION);
            }
        });

        izenaEremua.setPromptText("Izena");
        abizenaEremua.setPromptText("Abizena");
        nanEremua.setPromptText("NAN");

        GridPane formularioa = new GridPane();
        formularioa.getStyleClass().add("formularioa");

        formularioa.add(new Label("Izena:"), 0, 0);
        formularioa.add(izenaEremua, 1, 0);
        formularioa.add(new Label("Abizena:"), 2, 0);
        formularioa.add(abizenaEremua, 3, 0);
        formularioa.add(new Label("NAN:"), 0, 1);
        formularioa.add(nanEremua, 1, 1);

        TableColumn<Banatzailea, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idBa"));

        TableColumn<Banatzailea, String> izenaCol = new TableColumn<>("Izena");
        izenaCol.setCellValueFactory(new PropertyValueFactory<>("izena"));

        TableColumn<Banatzailea, String> abizenaCol = new TableColumn<>("Abizena");
        abizenaCol.setCellValueFactory(new PropertyValueFactory<>("abizena"));

        TableColumn<Banatzailea, String> nanCol = new TableColumn<>("NAN");
        nanCol.setCellValueFactory(new PropertyValueFactory<>("nan"));

        taula.getColumns().clear();
        taula.getColumns().addAll(idCol, izenaCol, abizenaCol, nanCol);
        taula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button gehituBtn = new Button("➕ Gehitu");
        Button editatuBtn = new Button("✏️ Editatu");
        Button ezabatuBtn = new Button("🗑 Ezabatu");
        Button garbituBtn = new Button("🧹 Garbitu");

        gehituBtn.getStyleClass().add("btn-success");
        editatuBtn.getStyleClass().add("btn-warning");
        ezabatuBtn.getStyleClass().add("btn-danger");
        garbituBtn.getStyleClass().add("btn-secondary");

        HBox botoiak = new HBox(gehituBtn, editatuBtn, ezabatuBtn, garbituBtn);
        botoiak.getStyleClass().add("botoiak");

        gehituBtn.setOnAction(e -> banatzaileaGehitu());
        editatuBtn.setOnAction(e -> banatzaileaEditatu());
        ezabatuBtn.setOnAction(e -> banatzaileaEzabatu());
        garbituBtn.setOnAction(e -> eremuakGarbitu());

        /*
         * Taulako errenkada batean klik egitean,
         * datuak formularioan kargatzen dira editatzeko edo ezabatzeko.
         */
        taula.setOnMouseClicked(e -> {
            Banatzailea hautatua = taula.getSelectionModel().getSelectedItem();
            if (hautatua != null) {
                idEremua.setText(String.valueOf(hautatua.getIdBa()));
                izenaEremua.setText(hautatua.getIzena());
                abizenaEremua.setText(hautatua.getAbizena());
                nanEremua.setText(hautatua.getNan());
                idEremua.setEditable(false);
            }
        });

        VBox panela = new VBox(atala, formularioa, botoiak, taula);
        panela.getStyleClass().add("content-card");
        panela.getStyleClass().add("crud-panela");

        return panela;
    }

    /**
     * Bezeroen kudeaketa panela sortzen du.
     *
     * @return bezeroen panela
     */
    private VBox sortuBezeroPanela() {
        Label titulua = new Label("Bezeroen kudeaketa");
        titulua.getStyleClass().add("section-title");

        bezeroIdEremua.setPromptText("ID");
        bezeroIzenaEremua.setPromptText("Izena");
        bezeroAbizenaEremua.setPromptText("Abizena");
        bezeroTelefonoaEremua.setPromptText("Telefonoa");
        bezeroHerriaEremua.setPromptText("Herria");

        bezeroIdEremua.setEditable(false);

        GridPane formularioa = new GridPane();
        formularioa.setHgap(10);
        formularioa.setVgap(10);
        formularioa.getStyleClass().add("formularioa");

        formularioa.add(new Label("Izena:"), 0, 0);
        formularioa.add(bezeroIzenaEremua, 1, 0);

        formularioa.add(new Label("Abizena:"), 2, 0);
        formularioa.add(bezeroAbizenaEremua, 3, 0);

        formularioa.add(new Label("Telefonoa:"), 0, 1);
        formularioa.add(bezeroTelefonoaEremua, 1, 1);

        formularioa.add(new Label("Herria:"), 2, 1);
        formularioa.add(bezeroHerriaEremua, 3, 1);

        TableColumn<Bezeroa, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idBe"));

        TableColumn<Bezeroa, String> izenaCol = new TableColumn<>("Izena");
        izenaCol.setCellValueFactory(new PropertyValueFactory<>("izena"));

        TableColumn<Bezeroa, String> abizenaCol = new TableColumn<>("Abizena");
        abizenaCol.setCellValueFactory(new PropertyValueFactory<>("abizena"));

        TableColumn<Bezeroa, String> telefonoaCol = new TableColumn<>("Telefonoa");
        telefonoaCol.setCellValueFactory(new PropertyValueFactory<>("telefonoa"));

        TableColumn<Bezeroa, String> herriaCol = new TableColumn<>("Herria");
        herriaCol.setCellValueFactory(new PropertyValueFactory<>("herria"));

        bezeroTaula.getColumns().clear();
        bezeroTaula.getColumns().addAll(idCol, izenaCol, abizenaCol, telefonoaCol, herriaCol);
        bezeroTaula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button gehituBtn = new Button("➕ Gehitu");
        Button editatuBtn = new Button("✏️ Editatu");
        Button ezabatuBtn = new Button("🗑 Ezabatu");
        Button garbituBtn = new Button("🧹 Garbitu");

        gehituBtn.getStyleClass().add("btn-success");
        editatuBtn.getStyleClass().add("btn-warning");
        ezabatuBtn.getStyleClass().add("btn-danger");
        garbituBtn.getStyleClass().add("btn-secondary");

        HBox botoiak = new HBox(10, gehituBtn, editatuBtn, ezabatuBtn, garbituBtn);
        botoiak.getStyleClass().add("botoiak");

        gehituBtn.setOnAction(e -> bezeroaGehitu());
        editatuBtn.setOnAction(e -> bezeroaEditatu());
        ezabatuBtn.setOnAction(e -> bezeroaEzabatu());
        garbituBtn.setOnAction(e -> bezeroEremuakGarbitu());

        bezeroTaula.setOnMouseClicked(e -> {
            Bezeroa hautatua = bezeroTaula.getSelectionModel().getSelectedItem();

            if (hautatua != null) {
                bezeroIdEremua.setText(String.valueOf(hautatua.getIdBe()));
                bezeroIzenaEremua.setText(hautatua.getIzena());
                bezeroAbizenaEremua.setText(hautatua.getAbizena());
                bezeroTelefonoaEremua.setText(hautatua.getTelefonoa());
                bezeroHerriaEremua.setText(hautatua.getHerria());
            }
        });

        bezeroTaulaKargatu();

        VBox panela = new VBox(15, titulua, formularioa, botoiak, bezeroTaula);
        panela.setPadding(new Insets(10));
        panela.getStyleClass().add("content-card");

        return panela;
    }

    /**
     * Paketeen kudeaketa panela sortzen du.
     * Formularioa, botoiak eta taula prestatzen ditu.
     *
     * @return paketeen panela
     */
    private VBox sortuPaketePanela() {
        Label titulua = new Label("Paketeen kudeaketa");
        titulua.getStyleClass().add("section-title");

        pisuaEremua.setPromptText("Pisua");
        edukiaEremua.setPromptText("Edukia");
        herriaEremua.setPromptText("Herria");
        helbideaEremua.setPromptText("Helbidea");
        sarreraDataEremua.setPromptText("Sarrera data");
        bezeroaIdEremua.setPromptText("Bezero ID");

        paketeIdEremua.setOnMouseClicked(e -> {
            if (!paketeIdEremua.isEditable()) {
                mezua("Abisua", "Identifikatzailea ezin da editatu.", Alert.AlertType.INFORMATION);
            }
        });

        GridPane formularioa = new GridPane();
        formularioa.getStyleClass().add("formularioa");

        formularioa.add(new Label("Pisua:"), 0, 0);
        formularioa.add(pisuaEremua, 1, 0);
        formularioa.add(new Label("Edukia:"), 2, 0);
        formularioa.add(edukiaEremua, 3, 0);

        formularioa.add(new Label("Herria:"), 0, 1);
        formularioa.add(herriaEremua, 1, 1);
        formularioa.add(new Label("Helbidea:"), 2, 1);
        formularioa.add(helbideaEremua, 3, 1);

        formularioa.add(new Label("Sarrera data:"), 0, 2);
        formularioa.add(sarreraDataEremua, 1, 2);
        formularioa.add(new Label("Bezero ID:"), 2, 2);
        formularioa.add(bezeroaIdEremua, 3, 2);

        TableColumn<Paketea, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idP"));

        TableColumn<Paketea, String> pisuaCol = new TableColumn<>("Pisua");
        pisuaCol.setCellValueFactory(new PropertyValueFactory<>("pisua"));

        TableColumn<Paketea, String> edukiaCol = new TableColumn<>("Edukia");
        edukiaCol.setCellValueFactory(new PropertyValueFactory<>("edukia"));

        TableColumn<Paketea, String> herriaCol = new TableColumn<>("Herria");
        herriaCol.setCellValueFactory(new PropertyValueFactory<>("herria"));

        TableColumn<Paketea, String> helbideaCol = new TableColumn<>("Helbidea");
        helbideaCol.setCellValueFactory(new PropertyValueFactory<>("helbidea"));

        TableColumn<Paketea, java.time.LocalDate> sarreraCol = new TableColumn<>("Sarrera data");
        sarreraCol.setCellValueFactory(new PropertyValueFactory<>("sarreraData"));

        TableColumn<Paketea, Integer> bezeroCol = new TableColumn<>("Bezero ID");
        bezeroCol.setCellValueFactory(new PropertyValueFactory<>("bezeroaIdBe"));

        TableColumn<Paketea, Integer> entregaCol = new TableColumn<>("Entrega ID");
        entregaCol.setCellValueFactory(new PropertyValueFactory<>("entregaIdE"));

        paketeTaula.getColumns().clear();
        paketeTaula.getColumns().addAll(
                idCol, pisuaCol, edukiaCol, herriaCol, helbideaCol,
                sarreraCol, bezeroCol, entregaCol
        );
        paketeTaula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button gehituBtn = new Button("➕ Gehitu");
        Button editatuBtn = new Button("✏️ Editatu");
        Button ezabatuBtn = new Button("🗑 Ezabatu");
        Button garbituBtn = new Button("🧹 Garbitu");

        gehituBtn.getStyleClass().add("btn-success");
        editatuBtn.getStyleClass().add("btn-warning");
        ezabatuBtn.getStyleClass().add("btn-danger");
        garbituBtn.getStyleClass().add("btn-secondary");

        HBox botoiak = new HBox(gehituBtn, editatuBtn, ezabatuBtn, garbituBtn);
        botoiak.getStyleClass().add("botoiak");

        gehituBtn.setOnAction(e -> paketeaGehitu());
        editatuBtn.setOnAction(e -> paketeaEditatu());
        ezabatuBtn.setOnAction(e -> paketeaEzabatu());
        garbituBtn.setOnAction(e -> paketeEremuakGarbitu());

        paketeTaula.setOnMouseClicked(e -> {
            Paketea hautatua = paketeTaula.getSelectionModel().getSelectedItem();

            if (hautatua != null) {
                paketeIdEremua.setText(hautatua.getIdP());
                pisuaEremua.setText(hautatua.getPisua());
                edukiaEremua.setText(hautatua.getEdukia());
                herriaEremua.setText(hautatua.getHerria());
                helbideaEremua.setText(hautatua.getHelbidea());
                sarreraDataEremua.setValue(hautatua.getSarreraData());

                bezeroaIdEremua.setText(
                        hautatua.getBezeroaIdBe() == null ? "" : String.valueOf(hautatua.getBezeroaIdBe())
                );

                paketeIdEremua.setEditable(false);
            }
        });

        paketeTaulaKargatu();

        VBox panela = new VBox(titulua, formularioa, botoiak, paketeTaula);
        panela.getStyleClass().add("content-card");
        panela.getStyleClass().add("crud-panela");

        return panela;
    }

    /**
     * Entregen kudeaketa panela sortzen du.
     *
     * @return Entregak ataleko panela
     */
    private VBox sortuEntregakPanela() {
        Label titulua = new Label("Entregen kudeaketa");
        titulua.getStyleClass().add("section-title");

        entregaDataEremua.setPromptText("Entrega data");

        entregaEgoeraEremua.getItems().setAll(
                "pendiente",
                "esleituta",
                "bidean",
                "entregatuta",
                "ez_entregatuta",
                "atzeratuta"
        );
        entregaEgoeraEremua.setPromptText("Egoera");

        entregaMezuaEremua.setPromptText("Mezua");
        entregaBanatzaileaIdEremua.setPromptText("Banatzailea ID");

        GridPane formularioa = new GridPane();
        formularioa.getStyleClass().add("formularioa");

        formularioa.add(new Label("Entrega data:"), 0, 1);
        formularioa.add(entregaDataEremua, 1, 1);

        formularioa.add(new Label("Egoera:"), 2, 1);
        formularioa.add(entregaEgoeraEremua, 3, 1);

        formularioa.add(new Label("Mezua:"), 0, 2);
        formularioa.add(entregaMezuaEremua, 1, 2, 3, 1);

        formularioa.add(new Label("Banatzailea ID:"), 0, 3);
        formularioa.add(entregaBanatzaileaIdEremua, 1, 3);

        TableColumn<Entrega, Integer> idCol = new TableColumn<>("ID_E");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idE"));

        TableColumn<Entrega, String> paketeCol = new TableColumn<>("Pakete ID");
        paketeCol.setCellValueFactory(new PropertyValueFactory<>("paketeId"));

        TableColumn<Entrega, java.time.LocalDate> dataCol = new TableColumn<>("Entrega data");
        dataCol.setCellValueFactory(new PropertyValueFactory<>("entregaDate"));

        TableColumn<Entrega, String> egoeraCol = new TableColumn<>("Egoera");
        egoeraCol.setCellValueFactory(new PropertyValueFactory<>("egoera"));

        TableColumn<Entrega, String> mezuaCol = new TableColumn<>("Mezua");
        mezuaCol.setCellValueFactory(new PropertyValueFactory<>("mezua"));

        TableColumn<Entrega, Integer> banatzaileCol = new TableColumn<>("Banatzailea ID");
        banatzaileCol.setCellValueFactory(new PropertyValueFactory<>("banatzaileaIdBa"));

        entregaTaula.getColumns().clear();
        entregaTaula.getColumns().addAll(idCol, paketeCol, dataCol, egoeraCol, mezuaCol, banatzaileCol);
        entregaTaula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button editatuBtn = new Button("✏️ Editatu");
        Button ezabatuBtn = new Button("🗑 Ezabatu");
        Button garbituBtn = new Button("🧹 Garbitu");

        editatuBtn.getStyleClass().add("btn-warning");
        ezabatuBtn.getStyleClass().add("btn-danger");
        garbituBtn.getStyleClass().add("btn-secondary");

        HBox botoiak = new HBox(editatuBtn, ezabatuBtn, garbituBtn);
        botoiak.getStyleClass().add("botoiak");

        editatuBtn.setOnAction(e -> entregaEditatu());
        ezabatuBtn.setOnAction(e -> entregaEzabatu());
        garbituBtn.setOnAction(e -> entregaEremuakGarbitu());

        entregaTaula.setOnMouseClicked(e -> {
            Entrega hautatua = entregaTaula.getSelectionModel().getSelectedItem();

            if (hautatua != null) {
                entregaDataEremua.setValue(hautatua.getEntregaDate());
                entregaEgoeraEremua.setValue(hautatua.getEgoera());
                entregaMezuaEremua.setText(hautatua.getMezua() == null ? "" : hautatua.getMezua());
                entregaBanatzaileaIdEremua.setText(
                        hautatua.getBanatzaileaIdBa() == null ? "" : String.valueOf(hautatua.getBanatzaileaIdBa())
                );
            }
        });

        entregaTaulaKargatu();

        VBox panela = new VBox(titulua, formularioa, botoiak, entregaTaula);
        panela.getStyleClass().add("content-card");
        panela.getStyleClass().add("crud-panela");

        return panela;
    }


    /**
     * Trazaren panela sortzen du.
     *
     * @return trazen panela
     */
    private VBox sortuTrazaPanela() {
        Label titulua = new Label("Trazak");
        titulua.getStyleClass().add("section-title");

        Label infoLabel = new Label();
        Label warnLabel = new Label();
        Label errorLabel = new Label();

        infoLabel.getStyleClass().add("traza-info");
        warnLabel.getStyleClass().add("traza-warn");
        errorLabel.getStyleClass().add("traza-error");

        ComboBox<String> filtroa = new ComboBox<>();
        filtroa.getItems().addAll("GUZTIAK", "INFO", "WARN", "ERROR");
        filtroa.setValue("GUZTIAK");

        Button kargatuBtn = new Button("Refreskatu");
        kargatuBtn.getStyleClass().add("btn-primary");

        HBox estatistikak = new HBox(infoLabel, warnLabel, errorLabel);
        estatistikak.getStyleClass().add("traza-estatistikak");

        HBox iragazkia = new HBox(new Label("Iragazi:"), filtroa, kargatuBtn);
        iragazkia.getStyleClass().add("traza-iragazkia");

        BorderPane goikoBarra = new BorderPane();
        goikoBarra.setLeft(estatistikak);
        goikoBarra.setRight(iragazkia);
        goikoBarra.getStyleClass().add("traza-goiko-barra");

        CategoryAxis xArdatza = new CategoryAxis();
        NumberAxis yArdatza = new NumberAxis();

        BarChart<String, Number> barraGrafikoa = new BarChart<>(xArdatza, yArdatza);
        barraGrafikoa.setLegendVisible(false);
        barraGrafikoa.setAnimated(false);
        barraGrafikoa.getStyleClass().add("traza-grafikoa");

        PieChart zirkuluGrafikoa = new PieChart();
        zirkuluGrafikoa.setAnimated(false);
        zirkuluGrafikoa.getStyleClass().add("traza-grafikoa");

        HBox grafikoak = new HBox(barraGrafikoa, zirkuluGrafikoa);
        grafikoak.getStyleClass().add("traza-grafikoak");

        TableColumn<Traza, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idT"));

        TableColumn<Traza, String> mailaCol = new TableColumn<>("Maila");
        mailaCol.setCellValueFactory(new PropertyValueFactory<>("maila"));

        TableColumn<Traza, java.time.LocalDateTime> dataCol = new TableColumn<>("Data/Hora");
        dataCol.setCellValueFactory(new PropertyValueFactory<>("dataHora"));

        TableColumn<Traza, String> ekintzaCol = new TableColumn<>("Ekintza");
        ekintzaCol.setCellValueFactory(new PropertyValueFactory<>("ekintza"));

        TableColumn<Traza, String> deskribapenaCol = new TableColumn<>("Deskribapena");
        deskribapenaCol.setCellValueFactory(new PropertyValueFactory<>("deskribapena"));

        trazaTaula.getColumns().clear();
        trazaTaula.getColumns().addAll(idCol, mailaCol, dataCol, ekintzaCol, deskribapenaCol);
        trazaTaula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        trazaTaula.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Traza traza, boolean empty) {
                super.updateItem(traza, empty);

                getStyleClass().removeAll("traza-row-info", "traza-row-warn", "traza-row-error");

                if (empty || traza == null) {
                    return;
                }

                switch (traza.getMaila()) {
                    case "ERROR" -> getStyleClass().add("traza-row-error");
                    case "WARN" -> getStyleClass().add("traza-row-warn");
                    default -> getStyleClass().add("traza-row-info");
                }
            }
        });

        Runnable trazakKargatu = () -> {
            List<Traza> guztiak = trazaDAO.lortuGuztiak();

            long infoKop = guztiak.stream()
                    .filter(t -> t.getMaila().equals("INFO"))
                    .count();

            long warnKop = guztiak.stream()
                    .filter(t -> t.getMaila().equals("WARN"))
                    .count();

            long errorKop = guztiak.stream()
                    .filter(t -> t.getMaila().equals("ERROR"))
                    .count();

            infoLabel.setText("INFO: " + infoKop);
            warnLabel.setText("WARN: " + warnKop);
            errorLabel.setText("ERROR: " + errorKop);

            XYChart.Data<String, Number> infoBarra = new XYChart.Data<>("INFO", infoKop);
            XYChart.Data<String, Number> warnBarra = new XYChart.Data<>("WARN", warnKop);
            XYChart.Data<String, Number> errorBarra = new XYChart.Data<>("ERROR", errorKop);

            XYChart.Series<String, Number> seriea = new XYChart.Series<>();
            seriea.getData().addAll(infoBarra, warnBarra, errorBarra);
            barraGrafikoa.getData().setAll(seriea);

            PieChart.Data infoZatia = new PieChart.Data("INFO (" + infoKop + ")", infoKop);
            PieChart.Data warnZatia = new PieChart.Data("WARN (" + warnKop + ")", warnKop);
            PieChart.Data errorZatia = new PieChart.Data("ERROR (" + errorKop + ")", errorKop);

            zirkuluGrafikoa.getData().setAll(infoZatia, warnZatia, errorZatia);

            Platform.runLater(() -> {
                if (infoBarra.getNode() != null) {
                    infoBarra.getNode().getStyleClass().add("traza-bar-info");
                }

                if (warnBarra.getNode() != null) {
                    warnBarra.getNode().getStyleClass().add("traza-bar-warn");
                }

                if (errorBarra.getNode() != null) {
                    errorBarra.getNode().getStyleClass().add("traza-bar-error");
                }

                if (infoZatia.getNode() != null) {
                    infoZatia.getNode().getStyleClass().add("traza-pie-info");
                }

                if (warnZatia.getNode() != null) {
                    warnZatia.getNode().getStyleClass().add("traza-pie-warn");
                }

                if (errorZatia.getNode() != null) {
                    errorZatia.getNode().getStyleClass().add("traza-pie-error");
                }
            });

            String aukeratua = filtroa.getValue();

            if ("GUZTIAK".equals(aukeratua)) {
                trazaTaula.getItems().setAll(guztiak);
            } else {
                trazaTaula.getItems().setAll(
                        guztiak.stream()
                                .filter(t -> t.getMaila().equals(aukeratua))
                                .toList()
                );
            }
        };

        filtroa.setOnAction(e -> trazakKargatu.run());
        kargatuBtn.setOnAction(e -> trazakKargatu.run());

        trazakKargatu.run();

        VBox panela = new VBox(titulua, goikoBarra, grafikoak, trazaTaula);
        panela.getStyleClass().add("content-card");
        panela.getStyleClass().add("crud-panela");

        return panela;
    }

    private VBox sortuHistorialaPanela() {
        Label titulua = new Label("Historiala");
        titulua.getStyleClass().add("section-title");

        TableColumn<Historiala, java.time.LocalDate> dataCol = new TableColumn<>("Entrega data");
        dataCol.setCellValueFactory(new PropertyValueFactory<>("entregaDate"));

        TableColumn<Historiala, String> paketeCol = new TableColumn<>("Pakete ID");
        paketeCol.setCellValueFactory(new PropertyValueFactory<>("paketeId"));

        TableColumn<Historiala, Integer> entregaCol = new TableColumn<>("Entrega ID");
        entregaCol.setCellValueFactory(new PropertyValueFactory<>("entregaId"));

        TableColumn<Historiala, Integer> bezeroCol = new TableColumn<>("Bezero ID");
        bezeroCol.setCellValueFactory(new PropertyValueFactory<>("bezeroId"));

        TableColumn<Historiala, Integer> banatzaileCol = new TableColumn<>("Banatzailea ID");
        banatzaileCol.setCellValueFactory(new PropertyValueFactory<>("banatzaileaId"));

        TableColumn<Historiala, String> egoeraCol = new TableColumn<>("Egoera");
        egoeraCol.setCellValueFactory(new PropertyValueFactory<>("egoera"));

        TableColumn<Historiala, String> herriaCol = new TableColumn<>("Herria");
        herriaCol.setCellValueFactory(new PropertyValueFactory<>("herria"));

        TableColumn<Historiala, String> helbideaCol = new TableColumn<>("Helbidea");
        helbideaCol.setCellValueFactory(new PropertyValueFactory<>("helbidea"));

        historialaTaula.getColumns().clear();
        historialaTaula.getColumns().addAll(
                dataCol, paketeCol, entregaCol, bezeroCol,
                banatzaileCol, egoeraCol, herriaCol, helbideaCol
        );
        historialaTaula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        historialaTaulaKargatu();

        VBox panela = new VBox(titulua, historialaTaula);
        panela.getStyleClass().add("content-card");
        panela.getStyleClass().add("crud-panela");

        return panela;
    }

    /**
     * Formularioan sartutako datuekin banatzaile berri bat gehitzen du.
     * Banatzailea sortzean, automatikoki kontu bat ere sortzen da.
     */
    private void banatzaileaGehitu() {
        try {
            if (!balidatuBanatzailea(false)) {
                return;
            }

            String izena = izenaEremua.getText().trim();
            String abizena = abizenaEremua.getText().trim();
            String nan = nanEremua.getText().trim().toUpperCase();

            Banatzailea banatzailea = new Banatzailea(0, izena, abizena, nan);
            int idSortua = banatzaileaDAO.gehituEtaIdItzuli(banatzailea);

            if (idSortua != -1) {
                String erabiltzailea = nan;
                String pasahitza = PasahitzaSortzailea.sortu();

                boolean kontuaSortuta = kontuaDAO.gehitu(erabiltzailea, pasahitza, idSortua);

                if (kontuaSortuta) {
                    mezua(
                            "Ondo",
                            "Banatzailea eta kontua ondo sortu dira.\n\n" +
                                    "ID: " + idSortua + "\n" +
                                    "Erabiltzailea: " + erabiltzailea + "\n" +
                                    "Pasahitza: " + pasahitza,
                            Alert.AlertType.INFORMATION
                    );
                } else {
                    mezua(
                            "Abisua",
                            "Banatzailea sortu da, baina ezin izan da kontua sortu.",
                            Alert.AlertType.WARNING
                    );
                }

                trazaDAO.gehitu("CREATE_BANATZAILEA", "Banatzailea sortu da: ID " + idSortua);

                taulaKargatu();
                eremuakGarbitu();
            } else {
                mezua("Errorea", "Ezin izan da banatzailea gehitu.", Alert.AlertType.ERROR);
            }

        } catch (Exception ex) {
            trazaDAO.gehitu("ERROR_BANATZAILEA", "Errorea banatzailea sortzean: " + ex.getMessage());
            mezua("Errorea", "Ezin izan da banatzailea sortu. Egiaztatu datuak.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Hautatutako edo formularioan agertzen den banatzailearen datuak editatzen ditu.
     */
    private void banatzaileaEditatu() {
        Banatzailea hautatua = taula.getSelectionModel().getSelectedItem();

        if (hautatua == null) {
            mezua("Abisua", "Editatzeko banatzaile bat hautatu behar duzu.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (!balidatuBanatzailea(true)) {
                return;
            }

            int id = Integer.parseInt(idEremua.getText().trim());
            String izena = izenaEremua.getText().trim();
            String abizena = abizenaEremua.getText().trim();
            String nan = nanEremua.getText().trim().toUpperCase();

            Banatzailea banatzailea = new Banatzailea(id, izena, abizena, nan);
            boolean ondo = banatzaileaDAO.editatu(banatzailea);

            if (ondo) {
                kontuaDAO.erabiltzaileaEguneratu(id, nan);

                trazaDAO.gehitu("UPDATE_BANATZAILEA", "Banatzailea editatu da: ID " + id);

                mezua("Ondo", "Banatzailea eta bere kontua ondo editatu dira.", Alert.AlertType.INFORMATION);
                taulaKargatu();
                eremuakGarbitu();
                taula.getSelectionModel().clearSelection();
            } else {
                mezua("Errorea", "Ezin izan da banatzailea editatu.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException ex) {
            mezua("Errorea", "ID zenbakia izan behar da.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Taulan hautatutako banatzailea ezabatzen du.
     * Ezabatu aurretik baieztapen leiho bat erakusten da.
     * Banatzailea ezabatu aurretik, bere kontua automatikoki ezabatzen da.
     */
    private void banatzaileaEzabatu() {
        Banatzailea hautatua = taula.getSelectionModel().getSelectedItem();

        if (hautatua == null) {
            mezua("Abisua", "Banatzaile bat hautatu behar duzu.", Alert.AlertType.WARNING);
            return;
        }

        Alert baieztapena = new Alert(Alert.AlertType.CONFIRMATION);
        baieztapena.setTitle("Baieztapena");
        baieztapena.setHeaderText(null);
        baieztapena.setContentText("Ziur zaude banatzailea eta bere kontua ezabatu nahi dituzula?");

        Optional<ButtonType> emaitza = baieztapena.showAndWait();

        if (emaitza.isPresent() && emaitza.get() == ButtonType.OK) {
            boolean kontuaEzabatuta = kontuaDAO.ezabatuBanatzailearenKontua(hautatua.getIdBa());

            if (kontuaEzabatuta) {
                boolean banatzaileaEzabatuta = banatzaileaDAO.ezabatu(hautatua.getIdBa());

                if (banatzaileaEzabatuta) {
                    trazaDAO.gehitu("DELETE_BANATZAILEA", "Banatzailea eta bere kontua ezabatu dira: " + hautatua.getIdBa());
                    mezua("Ondo", "Banatzailea eta bere kontua ezabatu dira.", Alert.AlertType.INFORMATION);
                    taulaKargatu();
                    eremuakGarbitu();
                } else {
                    mezua("Errorea", "Kontua ezabatu da, baina ezin izan da banatzailea ezabatu.", Alert.AlertType.ERROR);
                }
            } else {
                mezua("Errorea", "Ezin izan da banatzailearen kontua ezabatu.", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Formularioaren datuak balidatzen ditu.
     *
     * @param editatzenAriDa true bada, editatzeko balidazioa egingo da;
     *                       false bada, gehitzeko balidazioa egingo da
     * @return true balioak zuzenak badira, false bestela
     */
    private boolean balidatuBanatzailea(boolean editatzenAriDa) {
        String izena = izenaEremua.getText().trim();
        String nan = nanEremua.getText().trim().toUpperCase();

        if (izena.isBlank() || nan.isBlank()) {
            mezua("Errorea", "Izena eta NAN bete behar dira.", Alert.AlertType.ERROR);
            return false;
        }

        if (editatzenAriDa) {
            String idTestua = idEremua.getText().trim();

            if (idTestua.isBlank()) {
                mezua("Errorea", "Editatzeko banatzaile bat hautatu behar duzu.", Alert.AlertType.ERROR);
                return false;
            }

            try {
                Integer.parseInt(idTestua);
            } catch (NumberFormatException e) {
                mezua("Errorea", "ID zenbakia izan behar da.", Alert.AlertType.ERROR);
                return false;
            }
        }

        if (!nanBaliozkoaDa(nan)) {
            mezua("Errorea", "NAN ez da baliozkoa. Adibidea: 12345678A", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    /**
     * Formularioan sartutako datuekin bezero berri bat gehitzen du.
     */
    private void bezeroaGehitu() {
        if (!balidatuBezeroa(false)) {
            return;
        }

        String izena = bezeroIzenaEremua.getText().trim();
        String abizena = bezeroAbizenaEremua.getText().trim();
        String telefonoa = bezeroTelefonoaEremua.getText().trim();
        String herria = bezeroHerriaEremua.getText().trim();

        Bezeroa bezeroa = new Bezeroa(0, izena, abizena, telefonoa, herria);
        int idSortua = bezeroaDAO.gehituEtaIdItzuli(bezeroa);

        if (idSortua != -1) {
            trazaDAO.gehitu("CREATE_BEZEROA", "Bezeroa sortu da: " + idSortua);
            mezua("Ondo", "Bezeroa ondo sortu da. ID: " + idSortua, Alert.AlertType.INFORMATION);
            bezeroTaulaKargatu();
            bezeroEremuakGarbitu();
        } else {
            mezua("Errorea", "Ezin izan da bezeroa sortu.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Hautatutako bezeroaren datuak editatzen ditu.
     */
    private void bezeroaEditatu() {
        Bezeroa hautatua = bezeroTaula.getSelectionModel().getSelectedItem();

        if (hautatua == null) {
            mezua("Abisua", "Editatzeko bezero bat hautatu behar duzu.", Alert.AlertType.WARNING);
            return;
        }

        if (!balidatuBezeroa(true)) {
            return;
        }

        int id = hautatua.getIdBe();
        String izena = bezeroIzenaEremua.getText().trim();
        String abizena = bezeroAbizenaEremua.getText().trim();
        String telefonoa = bezeroTelefonoaEremua.getText().trim();
        String herria = bezeroHerriaEremua.getText().trim();

        Bezeroa bezeroa = new Bezeroa(id, izena, abizena, telefonoa, herria);
        boolean ondo = bezeroaDAO.editatu(bezeroa);

        if (ondo) {
            trazaDAO.gehitu("UPDATE_BEZEROA", "Bezeroa editatu da: " + id);
            mezua("Ondo", "Bezeroa ondo editatu da.", Alert.AlertType.INFORMATION);
            bezeroTaulaKargatu();
            bezeroEremuakGarbitu();
        } else {
            mezua("Errorea", "Ezin izan da bezeroa editatu.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Hautatutako bezeroa ezabatzen du.
     */
    private void bezeroaEzabatu() {
        Bezeroa hautatua = bezeroTaula.getSelectionModel().getSelectedItem();

        if (hautatua == null) {
            mezua("Abisua", "Ezabatzeko bezero bat hautatu behar duzu.", Alert.AlertType.WARNING);
            return;
        }

        Alert baieztapena = new Alert(Alert.AlertType.CONFIRMATION);
        baieztapena.setTitle("Baieztapena");
        baieztapena.setHeaderText(null);
        baieztapena.setContentText("Ziur zaude bezero hau ezabatu nahi duzula?");

        Optional<ButtonType> emaitza = baieztapena.showAndWait();

        if (emaitza.isPresent() && emaitza.get() == ButtonType.OK) {
            boolean ondo = bezeroaDAO.ezabatu(hautatua.getIdBe());

            if (ondo) {
                trazaDAO.gehitu("DELETE_BEZEROA", "Bezeroa ezabatu da: " + hautatua.getIdBe());
                mezua("Ondo", "Bezeroa ondo ezabatu da.", Alert.AlertType.INFORMATION);
                bezeroTaulaKargatu();
                bezeroEremuakGarbitu();
            } else {
                mezua("Errorea", "Ezin izan da bezeroa ezabatu. Baliteke paketeren batekin lotuta egotea.", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Bezeroen taula datu-baseko datuekin kargatzen du.
     */
    private void bezeroTaulaKargatu() {
        bezeroTaula.getItems().clear();
        bezeroTaula.getItems().addAll(bezeroaDAO.lortuGuztiak());
    }

    /**
     * Bezeroen formularioaren eremuak garbitzen ditu.
     */
    private void bezeroEremuakGarbitu() {
        bezeroIdEremua.clear();
        bezeroIzenaEremua.clear();
        bezeroAbizenaEremua.clear();
        bezeroTelefonoaEremua.clear();
        bezeroHerriaEremua.clear();
        bezeroTaula.getSelectionModel().clearSelection();
    }

    /**
     * Bezeroen formularioaren datuak balidatzen ditu.
     *
     * @param editatzenAriDa true bada editatzeko balidazioa da, false bada gehitzekoa
     * @return true datuak zuzenak badira, false bestela
     */
    private boolean balidatuBezeroa(boolean editatzenAriDa) {
        String izena = bezeroIzenaEremua.getText().trim();
        String telefonoa = bezeroTelefonoaEremua.getText().trim();
        String herria = bezeroHerriaEremua.getText().trim();

        if (izena.isBlank() || telefonoa.isBlank() || herria.isBlank()) {
            mezua("Errorea", "Izena, telefonoa eta herria bete behar dira.", Alert.AlertType.ERROR);
            return false;
        }

        if (!telefonoa.matches("\\d{9}")) {
            mezua("Errorea", "Telefonoak 9 zenbaki izan behar ditu.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    /**
     * NANaren formatua egokia den egiaztatzen du.
     * Formatu sinplea erabiltzen da: 8 zenbaki + letra 1.
     *
     * @param nan egiaztatu beharreko NAN-a
     * @return true formatua zuzena bada, false bestela
     */
    private boolean nanBaliozkoaDa(String nan) {
        return nan.matches("\\d{8}[A-Z]");
    }

    /**
     * ID bat dagoeneko taulan edo datuetan agertzen den egiaztatzen du.
     *
     * @param id egiaztatu beharreko ID-a
     * @return true errepikatuta badago, false bestela
     */
    private boolean idErrepikatuaDa(int id) {
        for (Banatzailea b : banatzaileaDAO.lortuGuztiak()) {
            if (b.getIdBa() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Datu-basetik banatzaile guztiak kargatzen ditu eta taulan erakusten ditu.
     */
    private void taulaKargatu() {
        taula.getItems().clear();
        taula.getItems().addAll(banatzaileaDAO.lortuGuztiak());
    }

    /**
     * Formularioan sartutako datuekin pakete berri bat gehitzen du.
     */
    private void paketeaGehitu() {
        try {
            if (!balidatuPaketea(false)) {
                return;
            }

            String pisua = pisuaEremua.getText().trim();
            String edukia = edukiaEremua.getText().trim();
            String herria = herriaEremua.getText().trim();
            String helbidea = helbideaEremua.getText().trim();
            java.time.LocalDate sarreraData = sarreraDataEremua.getValue();

            Integer bezeroId = null;
            String bezeroTestua = bezeroaIdEremua.getText().trim();
            if (!bezeroTestua.isBlank()) {
                bezeroId = Integer.parseInt(bezeroTestua);
            }

            Integer entregaId = null;

            // 1️⃣ Sortu paketea SIN entrega primero
            Paketea paketea = new Paketea(null, pisua, edukia, herria, helbidea, sarreraData, bezeroId, entregaId);
            int paketeIdSortua = paketeaDAO.gehituEtaIdItzuli(paketea);

            if (paketeIdSortua != -1) {

                String id = String.valueOf(paketeIdSortua);

                // 🔹 TRAZA: paquete creado
                trazaDAO.gehitu("CREATE_PAKETEA", "Paketea sortu da: " + id);

                // 🔹 ENTREGA automatikoa
                entregaAutomatikoaSortu(id, bezeroId, herria, helbidea);

                mezua("Ondo", "Paketea ondo gehitu da. ID: " + id, Alert.AlertType.INFORMATION);
                paketeTaulaKargatu();
                paketeEremuakGarbitu();

            } else {
                trazaDAO.gehitu("ERROR_PAKETEA", "Ezin izan da paketea sortu.");
                mezua("Errorea", "Ezin izan da paketea gehitu.", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            trazaDAO.gehitu("ERROR_FORMATUA", "Bezero ID okerra: " + bezeroaIdEremua.getText());
            mezua("Errorea", "Bezero ID zenbakia izan behar da.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Hautatutako paketearen datuak editatzen ditu.
     */
    private void paketeaEditatu() {
        Paketea hautatua = paketeTaula.getSelectionModel().getSelectedItem();

        if (hautatua == null) {
            mezua("Abisua", "Editatzeko pakete bat hautatu behar duzu.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (!balidatuPaketea(true)) {
                return;
            }

            String id = paketeIdEremua.getText().trim();
            String pisua = pisuaEremua.getText().trim();
            String edukia = edukiaEremua.getText().trim();
            String herria = herriaEremua.getText().trim();
            String helbidea = helbideaEremua.getText().trim();
            java.time.LocalDate sarreraData = sarreraDataEremua.getValue();

            Integer bezeroId = null;
            String bezeroTestua = bezeroaIdEremua.getText().trim();
            if (!bezeroTestua.isBlank()) {
                bezeroId = Integer.parseInt(bezeroTestua);
            }

            Integer entregaId = hautatua.getEntregaIdE();

            Paketea paketea = new Paketea(id, pisua, edukia, herria, helbidea, sarreraData, bezeroId, entregaId);
            boolean ondo = paketeaDAO.editatu(paketea);

            if (ondo) {

                trazaDAO.gehitu("UPDATE_PAKETEA", "Paketea editatu da: " + id);

                // 🔹 ENTREGA automatikoa (solo si no tenía antes)
                if (entregaId == null) {
                    entregaAutomatikoaSortu(id, bezeroId, herria, helbidea);
                }

                mezua("Ondo", "Paketea ondo editatu da.", Alert.AlertType.INFORMATION);
                paketeTaulaKargatu();
                paketeEremuakGarbitu();
                paketeTaula.getSelectionModel().clearSelection();

            } else {
                mezua("Errorea", "Ezin izan da paketea editatu.", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            mezua("Errorea", "Bezero ID zenbakizkoa izan behar da, edo hutsik utzi.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Hautatutako paketea ezabatzen du.
     */
    private void paketeaEzabatu() {
        Paketea hautatua = paketeTaula.getSelectionModel().getSelectedItem();

        if (hautatua == null) {
            mezua("Abisua", "Pakete bat hautatu behar duzu.", Alert.AlertType.WARNING);
            return;
        }

        Alert baieztapena = new Alert(Alert.AlertType.CONFIRMATION);
        baieztapena.setTitle("Baieztapena");
        baieztapena.setHeaderText(null);
        baieztapena.setContentText("Ziur zaude paketea ezabatu nahi duzula?");

        Optional<ButtonType> emaitza = baieztapena.showAndWait();

        if (emaitza.isPresent() && emaitza.get() == ButtonType.OK) {
            boolean ondo = paketeaDAO.ezabatu(hautatua.getIdP());

            if (ondo) {
                mezua("Ondo", "Paketea ezabatu da.", Alert.AlertType.INFORMATION);
                paketeTaulaKargatu();
                paketeEremuakGarbitu();
            } else {
                mezua("Errorea", "Ezin izan da paketea ezabatu.", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Pakete guztiak taulan kargatzen ditu.
     */
    private void paketeTaulaKargatu() {
        paketeTaula.getItems().clear();
        paketeTaula.getItems().addAll(paketeaDAO.lortuGuztiak());
    }

    /**
     * Paketeen formularioaren datuak balidatzen ditu.
     *
     * @param editatzenAriDa true bada editatzeko balidazioa da, false bada gehitzekoa
     * @return true datuak zuzenak badira, false bestela
     */
    private boolean balidatuPaketea(boolean editatzenAriDa) {
        String id = paketeIdEremua.getText().trim();
        String pisua = pisuaEremua.getText().trim();
        String edukia = edukiaEremua.getText().trim();
        String bezeroId = bezeroaIdEremua.getText().trim();

        /*
         * Stockean dagoen pakete bat sortzeko,
         * derrigorrezko eremuak hauek dira:
         * pisua, edukia eta sarrera data.
         * ID-a datu-baseak automatikoki sortzen du.
         */
        if (pisua.isBlank() || edukia.isBlank() || sarreraDataEremua.getValue() == null) {
            mezua("Errorea", "Pisua, edukia eta sarrera data bete behar dira.", Alert.AlertType.ERROR);
            return false;
        }

        /*
         * Editatzen ari bagara, ID-a beharrezkoa da,
         * hautatutako paketea zein den jakiteko.
         */
        if (editatzenAriDa) {
            if (id.isBlank()) {
                mezua("Errorea", "Editatzeko pakete bat hautatu behar duzu.", Alert.AlertType.ERROR);
                return false;
            }

            try {
                Integer.parseInt(id);
            } catch (NumberFormatException e) {
                mezua("Errorea", "ID zenbakia izan behar da.", Alert.AlertType.ERROR);
                return false;
            }
        }

        /*
         * Bezero ID-a beteta badago, zenbakizkoa izan behar da.
         */
        if (!bezeroId.isBlank()) {
            try {
                Integer.parseInt(bezeroId);
            } catch (NumberFormatException e) {
                mezua("Errorea", "Bezero ID zenbakia izan behar da.", Alert.AlertType.ERROR);
                return false;
            }
        }

        return true;
    }

    /**
     * Paketearen ID-a errepikatuta dagoen egiaztatzen du.
     *
     * @param id egiaztatu beharreko ID-a
     * @return true errepikatuta badago, false bestela
     */
    private boolean paketeIdErrepikatuaDa(String id) {
        for (Paketea p : paketeaDAO.lortuGuztiak()) {
            if (p.getIdP().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Paketeen formularioaren eremu guztiak garbitzen ditu.
     */
    private void paketeEremuakGarbitu() {
        paketeIdEremua.clear();
        pisuaEremua.clear();
        edukiaEremua.clear();
        herriaEremua.clear();
        helbideaEremua.clear();
        sarreraDataEremua.setValue(null);
        bezeroaIdEremua.clear();
        entregaIdEremua.clear();
        paketeIdEremua.setEditable(true);
    }

    /**
     * Formularioaren testu eremu guztiak garbitzen ditu.
     */
    private void eremuakGarbitu() {
        idEremua.clear();
        izenaEremua.clear();
        abizenaEremua.clear();
        nanEremua.clear();
        idEremua.setEditable(true);
    }

    /**
     * Erabiltzaileari alerta leiho baten bidez mezua erakusten dio.
     *
     * @param titulua alertaren titulua
     * @param edukia alertaren mezua
     * @param mota alertaren mota
     */
    private void mezua(String titulua, String edukia, Alert.AlertType mota) {
        Alert alerta = new Alert(mota);
        alerta.setTitle(titulua);
        alerta.setHeaderText(null);
        alerta.setContentText(edukia);
        alerta.showAndWait();
    }

    /**
     * Baldintzak betetzen badira, pakete bati entrega automatikoa sortu eta esleitzen dio.
     *
     * @param id paketearen ID-a
     * @param bezeroId bezeroaren ID-a
     * @param herria paketearen herria
     * @param helbidea paketearen helbidea
     */
    private void entregaAutomatikoaSortu(String id, Integer bezeroId, String herria, String helbidea) {
        if (bezeroId != null && !herria.isBlank() && !helbidea.isBlank()) {

            Entrega entregaBerria = new Entrega();
            entregaBerria.setEntregaDate(java.time.LocalDate.now());
            entregaBerria.setEgoera("pendiente");
            entregaBerria.setMezua("");
            entregaBerria.setBanatzaileaIdBa(null);

            int entregaIdSortua = entregaDAO.gehituEtaIdItzuli(entregaBerria);

            if (entregaIdSortua != -1) {
                boolean lotuta = paketeaDAO.entregaEsleitu(id, entregaIdSortua);

                if (lotuta) {
                    trazaDAO.gehitu(
                            "CREATE_ENTREGA",
                            "Entrega sortu eta esleitu da. Paketea: " + id + ", Entrega: " + entregaIdSortua
                    );
                } else {
                    trazaDAO.gehitu(
                            "ERROR_ENTREGA",
                            "Entrega sortu da baina ezin izan da paketearekin lotu. Paketea: " + id
                    );
                }
            } else {
                trazaDAO.gehitu(
                        "ERROR_ENTREGA",
                        "Ezin izan da entrega sortu paketerako: " + id
                );
            }
        }
    }

    /**
     * Hautatutako entregaren datuak editatzen ditu.
     */
    private void entregaEditatu() {
        Entrega hautatua = entregaTaula.getSelectionModel().getSelectedItem();

        if (hautatua == null) {
            mezua("Abisua", "Editatzeko entrega bat hautatu behar duzu.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int idE = hautatua.getIdE();

            java.time.LocalDate entregaData = entregaDataEremua.getValue();
            String egoera = entregaEgoeraEremua.getValue();
            String mezuaTestua = entregaMezuaEremua.getText().trim();

            Integer banatzaileaId = null;
            String banatzaileTestua = entregaBanatzaileaIdEremua.getText().trim();

            if (!banatzaileTestua.isBlank()) {
                banatzaileaId = Integer.parseInt(banatzaileTestua);
            }

            if (banatzaileaId != null && (egoera == null || egoera.equals("pendiente"))) {
                egoera = "esleituta";
            }

            Entrega entrega = new Entrega();
            entrega.setIdE(idE);
            entrega.setEntregaDate(entregaData);
            entrega.setEgoera(egoera);
            entrega.setMezua(mezuaTestua);
            entrega.setBanatzaileaIdBa(banatzaileaId);
            entrega.setPaketeId(hautatua.getPaketeId());

            boolean ondo = entregaDAO.editatu(entrega);

            if (ondo) {
                trazaDAO.gehitu("UPDATE_ENTREGA", "Entrega editatu da: " + idE + ", egoera: " + egoera);

                mezua("Ondo", "Entrega ondo editatu da.", Alert.AlertType.INFORMATION);
                entregaTaulaKargatu();
                entregaEremuakGarbitu();
            } else {
                mezua("Errorea", "Ezin izan da entrega editatu.", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            mezua("Errorea", "Banatzailea ID zenbakia izan behar da.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Hautatutako entrega ezabatzen du.
     */
    private void entregaEzabatu() {
        Entrega hautatua = entregaTaula.getSelectionModel().getSelectedItem();

        if (hautatua == null) {
            mezua("Abisua", "Ezabatzeko entrega bat hautatu behar duzu.", Alert.AlertType.WARNING);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Berrespena");
        alert.setHeaderText(null);
        alert.setContentText("Ziur zaude entrega hau ezabatu nahi duzula?");

        Optional<ButtonType> emaitza = alert.showAndWait();
        if (emaitza.isPresent() && emaitza.get() == ButtonType.OK) {
            boolean ondo = entregaDAO.ezabatu(hautatua.getIdE());

            if (ondo) {
                mezua("Ondo", "Entrega ondo ezabatu da.", Alert.AlertType.INFORMATION);
                entregaTaulaKargatu();
                entregaEremuakGarbitu();
            } else {
                mezua("Errorea", "Ezin izan da entrega ezabatu.", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Entregen taula datu-baseko datuekin kargatzen du.
     */
    private void entregaTaulaKargatu() {
        entregaTaula.getItems().clear();
        entregaTaula.getItems().addAll(entregaDAO.lortuGuztiak());
    }

    /**
     * Traza taula datu-baseko datuekin kargatzen du.
     */
    private void trazaTaulaKargatu() {
        trazaTaula.getItems().clear();
        trazaTaula.getItems().addAll(trazaDAO.lortuGuztiak());
    }

    /**
     * Historialaren taula datu-baseko datuekin kargatzen du.
     */
    private void historialaTaulaKargatu() {
        historialaTaula.getItems().clear();
        historialaTaula.getItems().addAll(historialaDAO.lortuGuztiak());
    }

    /**
     * Entregaren formularioaren eremuak garbitzen ditu.
     */
    private void entregaEremuakGarbitu() {
        entregaIdEremuaKud.clear();
        entregaPaketeIdEremua.clear();
        entregaDataEremua.setValue(null);
        entregaEgoeraEremua.setValue(null);
        entregaMezuaEremua.clear();
        entregaBanatzaileaIdEremua.clear();
        entregaTaula.getSelectionModel().clearSelection();
    }

    /**
     * Aplikazioa exekutatzeko metodo nagusia.
     *
     * @param args programaren argumentuak
     */
    public static void main(String[] args) {
        launch(args);
    }
}