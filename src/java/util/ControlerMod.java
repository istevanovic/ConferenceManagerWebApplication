package util;


import beans.Dogadjaj;
import util.Data;
import beans.Konferencija;
import beans.Korisnik;
import static com.sun.faces.facelets.util.Path.context;
import java.io.IOException;
import java.io.Serializable;
import java.lang.Object;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;
import util.HibernateUtil;

@SessionScoped
@ManagedBean
public class ControlerMod implements Serializable {
  private List<String> Sesije;
  private String date,time;
  private ArrayList predavaci;
  private String porukaVreme;
  private String dogadjaj;
  private String sala;
  private String naziv;
  private String dan,mesec,godina;
  private String sat1, min1, sat2, min2;
  private String porukaDodavanje;
  private String idKonf;
  private boolean renderTermin, renderNaziv, renderSala, renderPredavaci, renderDatum, renderSesija,renderButton,amIneeded;
  
    
    public void dodajDogadjaj() throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        int idKonf = (int) session.getAttribute("mod");
        predavaci= new ArrayList<>();
//        Integer s1 = Integer.parseInt(sat1); Integer s2=Integer.parseInt(sat2); Integer m1 = Integer.parseInt(min1); Integer m2 = Integer.parseInt(min2);
//        if ((s2<s1) || ((s2==s1) && (m2>m1))) {
//            setPorukaDodavanje("Uneti termin nije validan");
//            return;
//            
//        }
        time=sat1+":"+min1+":00";
        if (renderNaziv) {
            if (naziv.equals("")) {
            setPorukaDodavanje("Polje naziv je obavezno.");
            return;
            }
        }
         if (renderSala) {
            if (sala.equals("")) {
            setPorukaDodavanje("Polje naziv je obavezno.");
            return;
            }
        }
         
        if (renderPredavaci) {
            if (predavaci.size()==0) {
                setPorukaDodavanje("Predavaci nisu dodati.");
                return;   
            }
        }
        
         if (renderDatum) {
            if (dan.equals("-")||mesec.equals("")|| godina.equals("")) {
                setPorukaDodavanje("Predavaci nisu dodati.");
                return;   
            }
            date=godina+"-"+mesec+"-"+dan;
        }
         Connection con = DB.getInstance().getConnection();
         Statement stmt = con.createStatement();
         if (dogadjaj.equals("Sesija")) {
            
           
             
             String upit="insert into sesija (sala,datum,idkonf,tip) values ('"+sala+"','"+date+" "+time+"','"+idKonf+"','1');";
             stmt.executeUpdate(upit);
             setPorukaDodavanje("Sesija dodata.");
         }
         if (dogadjaj.equals("Pozdravni govor")) {
               String vremepoc=sat1+":"+min1+":00";
               String vremekraj=sat2+":"+min2+":"+"00";
               date=godina+"-"+mesec+"-"+dan;
               
               PreparedStatement statement = con.prepareStatement("insert into event (tip,idkonf,idsesija,vremepocetak,vremekraj,datumpocetak,sala)"
                       + "values (?,?,?,?,?,?,?)");
               statement.setString(1,dogadjaj);
               statement.setInt(2, idKonf);
               statement.setInt(3, 2);
               statement.setString(4, vremepoc);
               statement.setString(5, vremekraj);
               statement.setString(6, date);
               statement.setString(7, sala);
              
               statement.executeUpdate();
               statement.close();
               
         
            
            
           
         }
        
       
      
         
         
         
        DB.getInstance().putConnection(con);stmt.close();
    }
  
    public void onLoad() {
       setPorukaDodavanje("");
        renderTermin=false;
        renderNaziv=false;
        renderSala=false;
        renderPredavaci=false;
        renderDatum=false;
        renderSesija=false;
       
        renderButton=false;
    }
    public void renderNeeded() {
         
        renderTermin=false;
        renderNaziv=false;
        renderSala=false;
        renderPredavaci=false;
        renderDatum=false;
        renderSesija=false;
        amIneeded=true;
        renderButton=true;
        
        if (dogadjaj.equals("Sesija")) {
            renderDatum=true;
            renderTermin=true;
            renderSala=true;
            amIneeded=false;
        }
        
        if (dogadjaj.equals("Pozdravni govor")) {
            renderSesija=true;
            renderTermin=true;
            renderDatum=true;
            renderSala=true;
            
        }
        
       if (dogadjaj.equals("Predavanje")) {
           renderSesija=true;
           renderTermin=true;
           renderPredavaci=true;
       }
       
       if (dogadjaj.equals("Pauza")) {
           
           renderTermin=true;
           renderDatum=true;
       }
       
        if (dogadjaj.equals("Radionica")) {
           
           renderTermin=true;
           renderDatum=true;
           renderSala=true;
           renderNaziv=true;
       }
        if (dogadjaj.equals("Ceremonija zatvaranja")) {
            renderSesija=true;
            renderTermin=true;
            
        }
        
        
        
        
    }
  
   

   

    public String getPorukaVreme() {
        return porukaVreme;
    }

    public void setPorukaVreme(String porukaVreme) {
        this.porukaVreme = porukaVreme;
    }

    public String getDogadjaj() {
        return dogadjaj;
    }

    public void setDogadjaj(String dogadjaj) {
        this.dogadjaj = dogadjaj;
    }

    public boolean isRenderTermin() {
        return renderTermin;
    }

    public void setRenderTermin(boolean renderTermin) {
        this.renderTermin = renderTermin;
    }

    public boolean isRenderNaziv() {
        return renderNaziv;
    }

    public void setRenderNaziv(boolean renderNaziv) {
        this.renderNaziv = renderNaziv;
    }

    public boolean isRenderSala() {
        return renderSala;
    }

    public void setRenderSala(boolean renderSala) {
        this.renderSala = renderSala;
    }

    public boolean isRenderPredavaci() {
        return renderPredavaci;
    }

    public void setRenderPredavaci(boolean renderPredavaci) {
        this.renderPredavaci = renderPredavaci;
    }

    public boolean isRenderDatum() {
        return renderDatum;
    }

    public void setRenderDatum(boolean renderDatum) {
        this.renderDatum = renderDatum;
    }

    public boolean isRenderSesija() {
        return renderSesija;
    }

    public void setRenderSesija(boolean renderSesija) {
        this.renderSesija = renderSesija;
    }

    public boolean isRenderButton() {
        return renderButton;
    }

    public void setRenderButton(boolean renderButton) {
        this.renderButton = renderButton;
    }

    public String getSat1() {
        return sat1;
    }

    public void setSat1(String sat1) {
        this.sat1 = sat1;
    }

    public String getMin1() {
        return min1;
    }

    public void setMin1(String min1) {
        this.min1 = min1;
    }

    public String getSat2() {
        return sat2;
    }

    public void setSat2(String sat2) {
        this.sat2 = sat2;
    }

    public String getMin2() {
        return min2;
    }

    public void setMin2(String min2) {
        this.min2 = min2;
    }

    public String getPorukaDodavanje() {
        return porukaDodavanje;
    }

    public void setPorukaDodavanje(String porukaDodavanje) {
        this.porukaDodavanje = porukaDodavanje;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public ArrayList getPredavaci() {
        return predavaci;
    }

    public void setPredavaci(ArrayList predavaci) {
        this.predavaci = predavaci;
    }

    public String getDan() {
        return dan;
    }

    public void setDan(String dan) {
        this.dan = dan;
    }

    public String getMesec() {
        return mesec;
    }

    public void setMesec(String mesec) {
        this.mesec = mesec;
    }

    public String getGodina() {
        return godina;
    }

    public void setGodina(String godina) {
        this.godina = godina;
    }

    public boolean isAmIneeded() {
        return amIneeded;
    }

    public void setAmIneeded(boolean amIneeded) {
        this.amIneeded = amIneeded;
    }

    public List<String> getSesije() throws SQLException {
       Connection con = DB.getInstance().getConnection();
       Statement stmt = con.createStatement();
       return this.Sesije;
       
    }

    public void setSesije(List<String> Sesije) {
        this.Sesije = Sesije;
    }

    public String getIdKonf() {
        return idKonf;
    }

    public void setIdKonf(String idKonf) {
        this.idKonf = idKonf;
    }
    
    
    
    
  
  
  
}

