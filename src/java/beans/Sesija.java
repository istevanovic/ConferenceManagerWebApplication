/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.List;

/**
 *
 * @author Ilija
 */
public class Sesija {
    private String id;
    private String sala;
    private String datum;
    private String idKonf;
    private List<Event> predavanja;

    public Sesija() {
    }
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getIdKonf() {
        return idKonf;
    }

    public void setIdKonf(String idKonf) {
        this.idKonf = idKonf;
    }

    public List<Event> getPredavanja() {
        return predavanja;
    }

    public void setPredavanja(List<Event> predavanja) {
        this.predavanja = predavanja;
    }
    
    
    
}
