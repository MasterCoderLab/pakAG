package pakag.eredua;

import java.time.LocalDate;

/**
 * Paketea klaseak sistemako pakete baten informazioa gordetzen du.
 * Pakete bakoitzak bere identifikatzailea, pisua, edukia,
 * helmugako herria eta helbidea, sarrera data
 * eta lotutako bezeroa/entrega ditu.
 */
public class Paketea {

    /** Paketearen identifikatzailea */
    private String idP;

    /** Paketearen pisua */
    private String pisua;

    /** Paketearen edukia */
    private String edukia;

    /** Paketearen helmugako herria */
    private String herria;

    /** Paketearen helmugako helbidea */
    private String helbidea;

    /** Paketea sisteman sartu den data */
    private LocalDate sarreraData;

    /** Lotutako bezeroaren identifikatzailea (null izan daiteke) */
    private Integer bezeroaIdBe;

    /** Lotutako entregaren identifikatzailea (null izan daiteke) */
    private Integer entregaIdE;

    /**
     * Eraikitzaile hutsa.
     */
    public Paketea() {
    }

    /**
     * Eraikitzaile osoa.
     *
     * @param idP paketearen identifikatzailea
     * @param pisua paketearen pisua
     * @param edukia paketearen edukia
     * @param herria paketearen helmugako herria
     * @param helbidea paketearen helmugako helbidea
     * @param sarreraData paketea sisteman sartu den data
     * @param bezeroaIdBe bezeroaren identifikatzailea (null izan daiteke)
     * @param entregaIdE entregaren identifikatzailea (null izan daiteke)
     */
    public Paketea(String idP, String pisua, String edukia, String herria, String helbidea,
                   LocalDate sarreraData, Integer bezeroaIdBe, Integer entregaIdE) {
        this.idP = idP;
        this.pisua = pisua;
        this.edukia = edukia;
        this.herria = herria;
        this.helbidea = helbidea;
        this.sarreraData = sarreraData;
        this.bezeroaIdBe = bezeroaIdBe;
        this.entregaIdE = entregaIdE;
    }

    public String getIdP() {
        return idP;
    }

    public void setIdP(String idP) {
        this.idP = idP;
    }

    public String getPisua() {
        return pisua;
    }

    public void setPisua(String pisua) {
        this.pisua = pisua;
    }

    public String getEdukia() {
        return edukia;
    }

    public void setEdukia(String edukia) {
        this.edukia = edukia;
    }

    public String getHerria() {
        return herria;
    }

    public void setHerria(String herria) {
        this.herria = herria;
    }

    public String getHelbidea() {
        return helbidea;
    }

    public void setHelbidea(String helbidea) {
        this.helbidea = helbidea;
    }

    public LocalDate getSarreraData() {
        return sarreraData;
    }

    public void setSarreraData(LocalDate sarreraData) {
        this.sarreraData = sarreraData;
    }

    public Integer getBezeroaIdBe() {
        return bezeroaIdBe;
    }

    public void setBezeroaIdBe(Integer bezeroaIdBe) {
        this.bezeroaIdBe = bezeroaIdBe;
    }

    public Integer getEntregaIdE() {
        return entregaIdE;
    }

    public void setEntregaIdE(Integer entregaIdE) {
        this.entregaIdE = entregaIdE;
    }

    @Override
    public String toString() {
        return "Paketea{" +
                "idP='" + idP + '\'' +
                ", pisua='" + pisua + '\'' +
                ", edukia='" + edukia + '\'' +
                ", herria='" + herria + '\'' +
                ", helbidea='" + helbidea + '\'' +
                ", sarreraData=" + sarreraData +
                ", bezeroaIdBe=" + bezeroaIdBe +
                ", entregaIdE=" + entregaIdE +
                '}';
    }
}