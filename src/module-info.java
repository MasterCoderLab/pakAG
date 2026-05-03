module pakag.kudeatzailea {
    requires javafx.controls;
    requires javafx.swing;
    requires java.sql;
    requires java.desktop;
    requires jdk.xml.dom;

    opens pakag.aplikazioa to javafx.graphics;
    opens pakag.eredua to javafx.base;

    exports pakag.aplikazioa;
}