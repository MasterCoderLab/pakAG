module pakag.kudeatzailea {
    requires javafx.controls;
    requires java.sql;
    requires jdk.xml.dom;

    opens pakag.aplikazioa to javafx.graphics;
    opens pakag.eredua to javafx.base;

    exports pakag.aplikazioa;
}