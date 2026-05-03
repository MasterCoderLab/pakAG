package pakag.eredua;

/**
 * Banatzailea eredua.
 * Klase honek datu-baseko banatzailea taulako erregistro bat irudikatzen du.
 */
public class Banatzailea {

    private int idBa;
    private String izena;
    private String abizena;
    private String nan;

    public Banatzailea() {
    }

    public Banatzailea(int idBa, String izena, String abizena, String nan) {
        this.idBa = idBa;
        this.izena = izena;
        this.abizena = abizena;
        this.nan = nan;
    }

    public int getIdBa() {
        return idBa;
    }

    public void setIdBa(int idBa) {
        this.idBa = idBa;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public String getAbizena() {
        return abizena;
    }

    public void setAbizena(String abizena) {
        this.abizena = abizena;
    }

    public String getNan() {
        return nan;
    }

    public void setNan(String nan) {
        this.nan = nan;
    }

    @Override
    public String toString() {
        return "Banatzailea{" +
                "idBa=" + idBa +
                ", izena='" + izena + '\'' +
                ", abizena='" + abizena + '\'' +
                ", nan='" + nan + '\'' +
                '}';
    }
}