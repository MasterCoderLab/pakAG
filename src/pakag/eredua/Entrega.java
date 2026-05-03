package pakag.eredua;

import java.time.LocalDate;

/**
 * Entrega eredua.
 * <p>
 * Klase honek datu-baseko Entrega taulako erregistro bat irudikatzen du.
 * Entrega batek data, egoera, mezua eta aukeran banatzaile bat izan ditzake.
 * <p>
 * paketeId atributua ez dator zuzenean Entrega taulatik;
 * LEFT JOIN bidez lortzen da, entregari lotutako paketea interfazean erakusteko.
 */
public class Entrega {

    private int idE;
    private LocalDate entregaDate;
    private String egoera;
    private String mezua;
    private Integer banatzaileaIdBa;
    private String paketeId; // erakusteko eta kudeatzeko erabilgarria

    public Entrega() {
    }

    public Entrega(int idE, LocalDate entregaDate, String egoera, String mezua, Integer banatzaileaIdBa, String paketeId) {
        this.idE = idE;
        this.entregaDate = entregaDate;
        this.egoera = egoera;
        this.mezua = mezua;
        this.banatzaileaIdBa = banatzaileaIdBa;
        this.paketeId = paketeId;
    }

    public int getIdE() {
        return idE;
    }

    public void setIdE(int idE) {
        this.idE = idE;
    }

    public LocalDate getEntregaDate() {
        return entregaDate;
    }

    public void setEntregaDate(LocalDate entregaDate) {
        this.entregaDate = entregaDate;
    }

    public String getEgoera() {
        return egoera;
    }

    public void setEgoera(String egoera) {
        this.egoera = egoera;
    }

    public String getMezua() {
        return mezua;
    }

    public void setMezua(String mezua) {
        this.mezua = mezua;
    }

    public Integer getBanatzaileaIdBa() {
        return banatzaileaIdBa;
    }

    public void setBanatzaileaIdBa(Integer banatzaileaIdBa) {
        this.banatzaileaIdBa = banatzaileaIdBa;
    }

    public String getPaketeId() {
        return paketeId;
    }

    public void setPaketeId(String paketeId) {
        this.paketeId = paketeId;
    }
}