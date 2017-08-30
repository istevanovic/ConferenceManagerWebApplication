/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;


import com.mysql.jdbc.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Ilija
 */




public class Korisnik implements java.io.Serializable {
    
    private int id;
    private String username;
    private String password;
    private int tip;
    private String ime;
    private String prezime;
    private String email;
    private String institucija;
    private String pol;
    private String majica;
    private String linkedin;
    private Blob slika;

    public Blob getSlika() {
        return slika;
    }

    public void setSlika(Blob slika) {
        this.slika = slika;
    }
    
    
   

    public Korisnik() {
    }
    
    
     

  
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
 
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
   
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
   
    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }
    
   
    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }
     
    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
   
    public String getInstitucija() {
        return institucija;
    }

    public void setInstitucija(String institucija) {
        this.institucija = institucija;
    }
  
    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }
    
    public String getMajica() {
        return majica;
    }

    public void setMajica(String majica) {
        this.majica = majica;
    }
    
    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }
    
    
    
}
