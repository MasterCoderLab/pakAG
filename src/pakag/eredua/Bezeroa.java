package pakag.eredua;

/**
 * Bezero baten eredua.
 */
public class Bezeroa {

    private int idBe;
    private String izena;
    private String abizena;
    private String telefonoa;
    private String herria;

    public Bezeroa() {
    }

    public Bezeroa(int idBe, String izena, String abizena, String telefonoa, String herria) {
        this.idBe = idBe;
        this.izena = izena;
        this.abizena = abizena;
        this.telefonoa = telefonoa;
        this.herria = herria;
    }

    public int getIdBe() {
        return idBe;
    }

    public void setIdBe(int idBe) {
        this.idBe = idBe;
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

    public String getTelefonoa() {
        return telefonoa;
    }

    public void setTelefonoa(String telefonoa) {
        this.telefonoa = telefonoa;
    }

    public String getHerria() {
        return herria;
    }

    public void setHerria(String herria) {
        this.herria = herria;
    }
}