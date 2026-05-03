package pakag.eredua;

/**
 * Kontua eredua.
 *
 * Klase honek datu-baseko Kontua taulako erregistro bat irudikatzen du.
 * Kontua banatzaile bati lotuta dago, eta web aplikazioan sartzeko
 * erabiltzailea eta pasahitza gordetzen ditu.
 *
 * Oraingo bertsioan KontuaDAO-k kontuak zuzenean sortzen ditu parametroen bidez,
 * baina modelo hau prest dago kontuen datuak objektu bezala erabiltzeko.
 */
public class Kontua {

    private int idK;
    private String erabiltzailea;
    private String pasahitza;
    private int banatzaileaIdBa;

    public Kontua() {
    }

    public Kontua(int idK, String erabiltzailea, String pasahitza, int banatzaileaIdBa) {
        this.idK = idK;
        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
        this.banatzaileaIdBa = banatzaileaIdBa;
    }

    public int getIdK() {
        return idK;
    }

    public void setIdK(int idK) {
        this.idK = idK;
    }

    public String getErabiltzailea() {
        return erabiltzailea;
    }

    public void setErabiltzailea(String erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
    }

    public String getPasahitza() {
        return pasahitza;
    }

    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }

    public int getBanatzaileaIdBa() {
        return banatzaileaIdBa;
    }

    public void setBanatzaileaIdBa(int banatzaileaIdBa) {
        this.banatzaileaIdBa = banatzaileaIdBa;
    }
}