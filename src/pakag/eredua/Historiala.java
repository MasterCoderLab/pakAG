package pakag.eredua;

import java.time.LocalDate;

/**
 * Historiala eredua.
 *
 * Klase honek aplikazioan historiala erakusteko erabiltzen den objektua irudikatzen du.
 * Ez dator datu-baseko Historiala taula fisiko batetik, baizik eta Paketea eta Entrega
 * taulen arteko JOIN baten emaitzatik.
 *
 * Historiala atalean pakete baten entrega-data, pakete ID-a, entrega ID-a,
 * bezeroa, banatzailea, egoera eta helmuga-datuak erakusten dira.
 */
public class Historiala {

    // Entregaren data.
    private LocalDate entregaDate;

    // Historialean agertzen den paketearen identifikatzailea.
    private String paketeId;

    // Paketeari lotutako entregaren identifikatzailea.
    private Integer entregaId;

    // Paketearen bezeroaren identifikatzailea. Null izan daiteke paketeak bezero loturarik ez badu.
    private Integer bezeroId;

    // Entregari esleitutako banatzailearen identifikatzailea. Null izan daiteke oraindik esleitu gabe badago.
    private Integer banatzaileaId;

    // Entregaren egoera: pendiente, esleituta, bidean, entregatuta edo entregatu gabe.
    private String egoera;

    // Paketearen helmugako herria.
    private String herria;

    // Paketearen helmugako helbidea.
    private String helbidea;

    /**
     * Eraikitzaile hutsa.
     *
     * DAO klaseak objektua sortu eta ondoren setter metodoen bidez
     * datuak betetzeko erabiltzen da.
     */
    public Historiala() {
    }

    public LocalDate getEntregaDate() {
        return entregaDate;
    }

    public void setEntregaDate(LocalDate entregaDate) {
        this.entregaDate = entregaDate;
    }

    public String getPaketeId() {
        return paketeId;
    }

    public void setPaketeId(String paketeId) {
        this.paketeId = paketeId;
    }

    public Integer getEntregaId() {
        return entregaId;
    }

    public void setEntregaId(Integer entregaId) {
        this.entregaId = entregaId;
    }

    public Integer getBezeroId() {
        return bezeroId;
    }

    public void setBezeroId(Integer bezeroId) {
        this.bezeroId = bezeroId;
    }

    public Integer getBanatzaileaId() {
        return banatzaileaId;
    }

    public void setBanatzaileaId(Integer banatzaileaId) {
        this.banatzaileaId = banatzaileaId;
    }

    public String getEgoera() {
        return egoera;
    }

    public void setEgoera(String egoera) {
        this.egoera = egoera;
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
}