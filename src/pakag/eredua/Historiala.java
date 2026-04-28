package pakag.eredua;

import java.time.LocalDate;

public class Historiala {

    private LocalDate entregaDate;
    private String paketeId;
    private Integer entregaId;
    private Integer bezeroId;
    private Integer banatzaileaId;
    private String egoera;
    private String herria;
    private String helbidea;

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