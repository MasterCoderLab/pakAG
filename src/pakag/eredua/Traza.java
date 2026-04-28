package pakag.eredua;

import java.time.LocalDateTime;

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
}