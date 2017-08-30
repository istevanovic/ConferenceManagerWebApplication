package util;



import beans.Konferencija;
import beans.Poruka;
import java.io.Serializable;
import java.lang.Object;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ikas9
 */
@javax.faces.bean.SessionScoped
@ManagedBean
public class ControlerAdmin implements Serializable {
    
    private String nazivKonf;
    private String dan1,mesec1,godina1,dan2,mesec2,godina2,mesto,grad;
    private  ArrayList<String> gradovi,mesta,korisnici,mods,modsid,konferencije;
    private String oblast,porukaUnos,konfZaBris,porukaBris;

 
  public String obrisi() throws SQLException {
      
      setporukaUnos("");
      Connection con = DB.getInstance().getConnection();
      Statement stmt=con.createStatement();
      String upit="Select id from konferencija where naziv='"+konfZaBris+"';";
      ResultSet rs = stmt.executeQuery(upit);
      int idK=0;
      if (rs.next()) idK = rs.getInt("id");
      
      upit="Select userid from linkkonfuser where konferensid='"+idK+"';";
      rs=stmt.executeQuery(upit);
      ArrayList<Integer> idZaPor = new ArrayList<>();
      while (rs.next()) {
          idZaPor.add(rs.getInt("userid"));
      }
      String poruka="Sa zaljenjem vas obavestavamo da je konferencija " +konfZaBris+"  otkazana.";
      for (int i =0; i<idZaPor.size();i++) {
          upit = "INSERT into mailbox (sender,reciever,content,owner) values ('8','"+idZaPor.get(i)+"','"+poruka+"','"+idZaPor.get(i)+"');";
          stmt.executeUpdate(upit);
      }
      
      
      
      
      
      
      
      upit="DELETE from konferencija where id='"+idK+"';";
      stmt.executeUpdate(upit);
      
      upit="DELETE from linkkonfuser where konferensid='"+idK+"';";
      stmt.executeUpdate(upit);
      
      
      
      DB.getInstance().putConnection(con);
      stmt.close();
      setPorukaBris("Konferencija je uspesno obrisana.");
      return "Admin";
  } 
    
  public String unos() throws SQLException {
      setPorukaBris("");
      if(nazivKonf.equals("") || dan1.equals("")|| mesec1.equals("")|| godina1.equals("")|| dan2.equals("")
              || mesec2.equals("")|| godina2.equals("")|| oblast.equals("-"))
      {
        setporukaUnos("Niste uneli sve parametre konferencije.");
        return "Admin";
      }
      
      String datum1=godina1+"-"+mesec1+"-"+dan1;
      String datum2=godina2+"-"+mesec2+"-"+dan2;
      Connection con = DB.getInstance().getConnection();
      Statement stmt = con.createStatement();
      String upit ="SELECT * from mesto m, grad g where g.id=m.idgrad and g.grad='"+grad+"' and m.mesto='"+mesto+"';";
      ResultSet rs = stmt.executeQuery(upit);
      boolean okIzbor = false;
      if (rs.next()) {okIzbor = true;}
      
      if(okIzbor == false) {
          setporukaUnos("Trazeno mesto se ne nalazi u trazenom gradu.");
          DB.getInstance().putConnection(con);
           stmt.close();
          return "Admin";
      }
      
      upit="INSERT into Konferencija (naziv,datumpocetak,datumkraj,grad,mesto,oblast) VALUES ('"+nazivKonf+"','"+datum1+"','"
              +datum2+"','"+grad+"','"+mesto+"','"+oblast+"');";
      stmt.executeUpdate(upit);
      upit="SELECT id from konferencija where naziv='"+nazivKonf+"';";
      rs = stmt.executeQuery(upit);
      String idKonf="";
      if (rs.next()) {  
          idKonf =rs.getString("id");
      }
     
    
       ArrayList<String> modsids = new ArrayList<>();
      
      for (int i =0; i<mods.size(); i++) {
          String name = mods.get(i);
           upit="SELECT id from user where username='"+name+"';";
            rs = stmt.executeQuery(upit);
            if (rs.next()) modsids.add(rs.getString("id"));
      }
      
      for (int i =0; i<modsids.size(); i++) {
          upit="INSERT into linkkonfuser (userid,konferensid,ismod)  VALUES('"+modsids.get(i)+"','"+idKonf+"','1');";
          stmt.executeUpdate(upit);
          
      }
     
      
      DB.getInstance().putConnection(con);
      stmt.close();
      setporukaUnos("Konferencija je uspešno dodata.");
      return "Admin";
  }
  
  
  
    public String getporukaUnos() {
        return porukaUnos;
    }

    //-----------------------------GETSET

    public ArrayList<String> getModsid() {
        return modsid;
    }

    public void setModsid(ArrayList<String> modsid) {
        this.modsid = modsid;
    }
    
    
    
    public void setporukaUnos(String porukaPretraga) {
        this.porukaUnos = porukaPretraga;
    }

    public String getOblast() {
        return oblast;
    }

    public void setOblast(String oblast) {
        this.oblast = oblast;
    }
    
    

    public ArrayList<String> getMesta() throws SQLException {
       Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit="Select * from mesto";
        ResultSet rs = stmt.executeQuery(upit);
        ArrayList<String> mestaIzBaze = new ArrayList<>();
        
        while (rs.next()) {
            mestaIzBaze.add(rs.getString("mesto"));
        }
        DB.getInstance().putConnection(con);
        stmt.close();
        
        
        return mestaIzBaze;
    }

    public void setMesta(ArrayList<String> mesta) {
        this.mesta = mesta;
    }
    
    

    public ArrayList<String> getGradovi() throws SQLException {
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit="Select * from grad";
        ResultSet rs = stmt.executeQuery(upit);
        ArrayList<String> mestaIzBaze = new ArrayList<>();
        
        while (rs.next()) {
            mestaIzBaze.add(rs.getString("grad"));
        }
        DB.getInstance().putConnection(con);
        stmt.close();
        
        
        return mestaIzBaze;
        
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getPorukaBris() {
        return porukaBris;
    }

    public void setPorukaBris(String porukaBris) {
        this.porukaBris = porukaBris;
    }
    
    

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }
    
    

    public void setGradovi(ArrayList<String> mesta) {
        this.gradovi = gradovi;
    }

   
    
    

    public String getDan1() {
        return dan1;
    }

    public void setDan1(String dan1) {
        this.dan1 = dan1;
    }

    public String getMesec1() {
        return mesec1;
    }

    public void setMesec1(String mesec1) {
        this.mesec1 = mesec1;
    }

    public String getGodina1() {
        return godina1;
    }

    public void setGodina1(String godina1) {
        this.godina1 = godina1;
    }

    public String getDan2() {
        return dan2;
    }

    public void setDan2(String dan2) {
        this.dan2 = dan2;
    }

    public String getMesec2() {
        return mesec2;
    }

    public void setMesec2(String mesec2) {
        this.mesec2 = mesec2;
    }

    public String getGodina2() {
        return godina2;
    }

    public void setGodina2(String godina2) {
        this.godina2 = godina2;
    }
    
    
    

    public String getNazivKonf() {
        return nazivKonf;
    }

    public void setNazivKonf(String nazivKonf) {
        this.nazivKonf = nazivKonf;
    }

    public ArrayList<String> getKorisnici() throws SQLException {
       Connection con = DB.getInstance().getConnection();
       Statement stmt=con.createStatement();
       String upit ="Select username from user where username!='Admin';";
       ResultSet rs= stmt.executeQuery(upit);
       ArrayList<String> kor = new ArrayList<>();
       while (rs.next()) {
           kor.add(rs.getString("username"));
       }
       DB.getInstance().putConnection(con);
       stmt.close();
       return kor;
    }

    public void setKorisnici(ArrayList<String> korisnici) {
        this.korisnici = korisnici;
    }

    public String getPorukaUnos() {
        return porukaUnos;
    }

    public void setPorukaUnos(String porukaUnos) {
        this.porukaUnos = porukaUnos;
    }

    public ArrayList<String> getMods() {
        return mods;
    }

    public void setMods(ArrayList<String> mods) {
        this.mods = mods;
    }

    public ArrayList<String> getKonferencije() throws SQLException {
        ArrayList<String> konf = new ArrayList<>();
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit ="Select naziv from konferencija;";
        ResultSet rs = stmt.executeQuery(upit);
        while (rs.next()) {
            String k = rs.getString("naziv");
            konf.add(k);
        }
        
        DB.getInstance().putConnection(con);
        stmt.close();
        return konf;
    }

    public void setKonferencije(ArrayList<String> konferencije) {
        this.konferencije = konferencije;
    }

    public String getKonfZaBris() {
        return konfZaBris;
    }

    public void setKonfZaBris(String konfZaBris) {
        this.konfZaBris = konfZaBris;
    }
    
    
    
    
    
    
    
}