package pakag.eredua;

import java.time.LocalDateTime;

/**
 * Traza klaseak aplikazioan gertatutako ekintzen erregistroa adierazten du.
 * Erregistro bakoitzak data/ordua, ekintza mota eta deskribapena gordetzen ditu.
 */
public class Traza {

    private int idT;
    private LocalDateTime dataHora;
    private String ekintza;
    private String deskribapena;

    public Traza() {
    }

    public Traza(int idT, LocalDateTime dataHora, String ekintza, String deskribapena) {
        this.idT = idT;
        this.dataHora = dataHora;
        this.ekintza = ekintza;
        this.deskribapena = deskribapena;
    }

    public int getIdT() {
        return idT;
    }

    public void setIdT(int idT) {
        this.idT = idT;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getEkintza() {
        return ekintza;
    }

    public void setEkintza(String ekintza) {
        this.ekintza = ekintza;
    }

    public String getDeskribapena() {
        return deskribapena;
    }

    public void setDeskribapena(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    /**
     * Trazaren maila kalkulatzen du ekintzaren izenaren arabera.
     * <p>
     * Maila ez dago datu-basean gordeta; aplikazioan kalkulatzen da.
     * Horri esker, Traza taula sinple mantentzen da eta interfazean
     * INFO, WARN eta ERROR mailak erakutsi daitezke.
     *
     * @return INFO, WARN edo ERROR
     */
    public String getMaila() {
        if (ekintza == null || ekintza.isBlank()) {
            return "INFO";
        }

        String ekintzaMaiuskulaz = ekintza.toUpperCase();

        if (ekintzaMaiuskulaz.startsWith("ERROR")) {
            return "ERROR";
        }

        if (ekintzaMaiuskulaz.startsWith("DELETE")
                || ekintzaMaiuskulaz.startsWith("WARN")
                || ekintzaMaiuskulaz.contains("EZABATU")) {
            return "WARN";
        }

        return "INFO";
    }
}