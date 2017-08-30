/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author Ilija
 */
public class Event {
    
    private int id;
    private String tip;
    private int idKonf;
    private int idSesija;
    private String vremePocetak;
    private String vremeKraj;
    private String datumPocetak;
    private String sala;
    private String naziv;
    private boolean ocenjiv;
    private String predavaci="";

    public String getPredavaci() {
        return predavaci;
    }

    public void setPredavaci(String predavaci) {
        this.predavaci = predavaci;
    }
    
    

    public Event() {
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getIdKonf() {
        return idKonf;
    }

    public void setIdKonf(int idKonf) {
        this.idKonf = idKonf;
    }

    public int getIdSesija() {
        return idSesija;
    }

    public void setIdSesija(int idSesija) {
        this.idSesija = idSesija;
    }

    public String getVremePocetak() {
        return vremePocetak;
    }

    public void setVremePocetak(String vremePocetak) {
        this.vremePocetak = vremePocetak;
    }

    public String getVremeKraj() {
        return vremeKraj;
    }

    public void setVremeKraj(String vremeKraj) {
        this.vremeKraj = vremeKraj;
    }

    public String getDatumPocetak() {
        return datumPocetak;
    }

    public void setDatumPocetak(String datumPocetak) {
        this.datumPocetak = datumPocetak;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public boolean isOcenjiv() {
        return ocenjiv;
    }

    public void setOcenjiv(boolean ocenjiv) {
        this.ocenjiv = ocenjiv;
    }
    
    public boolean isGovor()
    {
        return this.tip.equals("govor");
    }
    
    public boolean isPredavanje()
    {
        return tip.equals("predavanje");
    }
    
     public boolean isPauza()
    {
        return tip.equals("pauza");
    }
     
      public boolean isRadionica()
    {
        return this.tip.equals("radionica");
    }
      
    
}
