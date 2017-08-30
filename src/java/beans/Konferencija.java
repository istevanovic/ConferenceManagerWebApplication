/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author ikas9
 */
public class Konferencija {
    
    private String naziv;
    private String datumPoc;
    private String datumKraj;
    private String mesto;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    

    public Konferencija() {
    }
    
    
    

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getDatumPoc() {
        return datumPoc;
    }

    public void setDatumPoc(String datumPoc) {
        this.datumPoc = datumPoc;
    }

    public String getDatumKraj() {
        return datumKraj;
    }

    public void setDatumKraj(String datumKraj) {
        this.datumKraj = datumKraj;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }
    
    
    
}
