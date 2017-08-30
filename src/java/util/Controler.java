package util;


import util.Data;
import beans.Konferencija;
import beans.Korisnik;
import static com.sun.faces.facelets.util.Path.context;
import java.io.IOException;
import java.io.Serializable;
import java.lang.Object;
import java.sql.Connection;
import java.sql.Date;
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

@SessionScoped
@ManagedBean
public class Controler implements Serializable {
    private String porukalogin;
    private String porukaregister;
    private String currentuser;
  
    
    //------------------------------ LOGIN
    private String user;
    private String pass;
    private String porukaloginOK;
    //------------------------------REGISTER 
    private String username;
    private String ime;
    private String prezime;
    private String email;
    private String institucija;
    private String password1,password2;
    private char pol;
    private String majica;
    private String linkedin;
    //-------------------------------CHANGEPW
    private String changer;
    private String oldpass,newpass;
    private String porukachangepw;
    //-------------------------------PRETRAGA
     private String nazivKonf;
     private String dan1,mesec1,godina1;
     private String dan2,mesec2,godina2;
     private String porukaPretraga;
     private ArrayList<Konferencija> Konf,Aktuelne;
     private Boolean render=true;
     private Boolean render2=false;

    public String getCurrentuser() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        
        String username = (String) session.getAttribute("username");
        return username;
    }
    
    public void resetIndex() {
        
         Controler.this.setPorukachangepw("");
         Controler.this.setPorukaregister("");
         
        
        
    } 

   
     
    public void resetPromSifra() {
        setPorukaPretraga("");
        setPorukaloginOK("");
         setPorukalogin("");
        
         Controler.this.setPorukalogin("");
         Controler.this.setPorukaloginOK("");
         //util.Controler.this.setPorukachangepw("");
         Controler.this.setPorukaregister("");
         
        
        
    } 
      public void resetRegister() {
        setPorukaPretraga("");
        setPorukaloginOK("");
         setPorukalogin("");
        
         Controler.this.setPorukalogin("");
         Controler.this.setPorukaloginOK("");
         Controler.this.setPorukachangepw("");
         //util.Controler.this.setPorukaregister("");
         
        
        
    } 
      
   
    
    
   
    public void konfOnLoad() throws SQLException {
       Connection con = DB.getInstance().getConnection();
       Statement stmt=con.createStatement();
       String upit ="SELECT * FROM konferencija WHERE  datumpocetak >=CURDATE()";
       ResultSet rs = stmt.executeQuery(upit);
       
       ArrayList<Konferencija> Konferencije = new ArrayList<>();
       while (rs.next()) {
           Konferencija k = new Konferencija();
           k.setDatumPoc(rs.getString("datumpocetak"));
           k.setDatumKraj(rs.getString("datumkraj"));
           k.setId(rs.getString("id"));
           k.setMesto(rs.getString("mesto"));
           k.setNaziv(rs.getString("naziv"));
           Konferencije.add(k);
       }
         Aktuelne=Konferencije;
         DB.getInstance().putConnection(con);
         stmt.close();
         
    }

    public ArrayList<Konferencija> getAktuelne() {
        return Aktuelne;
    }

    public void setAktuelne(ArrayList<Konferencija> Aktuelne) {
        this.Aktuelne = Aktuelne;
    }
     
    
    
    
    public Boolean getRender() {
        return render;
    }

    public void setRender(Boolean render) {
        this.render = render;
    }
     
   public ArrayList<Konferencija> getKonf() throws SQLException {
       return Konf;
   }


   public void setKonf(ArrayList<Konferencija> Konf) {
        this.Konf = Konf;
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

    public Boolean getRender2() {
        return render2;
    }

    public void setRender2(Boolean render2) {
        this.render2 = render2;
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

    public String getDan1() {
        return dan1;
    }

    public void setDan1(String dan1) {
        this.dan1 = dan1;
    }

    

   

    public Object getPorukaPretraga() {
        return porukaPretraga;
    }

    public void setPorukaPretraga(String porukaPretraga) {
        this.porukaPretraga = porukaPretraga;
    }
     
     
  
     
    public String getPorukaloginOK() {
        return porukaloginOK;
    }

    public void setPorukaloginOK(String porukaloginOK) {
        this.porukaloginOK = porukaloginOK;
    }

    
    
    
    public String getPorukachangepw() {
        return porukachangepw;
    }

    public void setPorukachangepw(String porukachangepw) {
        this.porukachangepw = porukachangepw;
    }
    
    

    public String getOldpass() {
        return oldpass;
    }

    public void setOldpass(String oldpass) {
        this.oldpass = oldpass;
    }

    public String getNewpass() {
        return newpass;
    }

    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }
    
    
    public String getChanger() {
        return changer;
    }

    public void setChanger(String changer) {
        this.changer = changer;
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

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public char getPol() {
        return pol;
    }

    public void setPol(char pol) {
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
    
    
    
    
    
    
    
    public String getPorukalogin() {
        return porukalogin;
    }

    public void setPorukalogin(String porukalogin) {
        this.porukalogin = porukalogin;
    }
    
    

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPorukaregister() {
        return porukaregister;
    }

    public void setPorukaregister(String porukaregister) {
        this.porukaregister = porukaregister;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
  //-------------------------------------------------------------------------------------------  
    
    public String changePW() throws SQLException {
        Connection con = DB.getInstance().getConnection();
         Statement stmt=con.createStatement();
        setPorukachangepw("");
        if (changer.isEmpty() || newpass.isEmpty() || oldpass.isEmpty())  {
            setPorukachangepw("Niste uneli sva trazena polja");
           DB.getInstance().putConnection(con); stmt.close(); return "promenasifre";
        }
        
         String upit ="select * from user where username='"+changer+"';";
         ResultSet rs=stmt.executeQuery(upit);
         if (!rs.next()) {
              setPorukachangepw("Ne postoji korisnik sa unetim korisnickim imenom");
              DB.getInstance().putConnection(con);
              stmt.close();
             DB.getInstance().putConnection(con); stmt.close();   return "promenasifre";
              
         }
         
         String truepw =rs.getString("password");
         if (!truepw.equals(oldpass)) {
             setPorukachangepw("Pogresno uneta stara lozinka!");
              DB.getInstance().putConnection(con);
              stmt.close();
             return "promenasifre";
             
         }
         
         else {
    upit="UPDATE user set password='"+newpass+"' where username='"+changer+"';";
    stmt.executeUpdate(upit);
    setPorukaloginOK("Uspesno promenjena lozinka");
     DB.getInstance().putConnection(con);
     stmt.close();
    return "index";
         }
      
}
        
    

   

//--------------------------------------------------------------------------------------------------------------
 public String pretrazi() throws SQLException {
      this.render2=false;
         String datum1="", datum2 = "";
        if(nazivKonf.isEmpty() && dan1.equals("-")&& mesec1.equals("-")&& godina1.equals("-")
                && dan2.equals("-")&& mesec2.equals("-")&& godina2.equals("-"))
            setPorukaPretraga("Unesite parametre pretrage.");
            
        if((!dan1.equals("-")) && (!mesec1.equals("-"))&&( !godina1.equals("-"))) {
            datum1 = godina1 + "-" + mesec1 + "-" + dan1;
        }
        
        if((!dan2.equals("-")) && (!mesec2.equals("-"))&&( !godina2.equals("-"))) {
            datum2 = godina2 + "-" + mesec2 + "-" + dan2;
        }
        
         if(!datum2.equals("")&& datum1.equals("")) {
            
            Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();  String upit;
            if (!nazivKonf.isEmpty()) {
             upit = "select * from konferencija where naziv LIKE '%"+nazivKonf+"%' and datumpocetak <= '"+datum2+"';";
            }
            else {
             upit = "select * from konferencija where datumpocetak <= '"+datum2+"';";
            }
            ResultSet rs = stmt.executeQuery(upit);
            
            ArrayList<Konferencija> Konferencije= new ArrayList<>();
            
            boolean records = false;
             while (rs.next()) {
                    records = true;
                    Konferencija k = new Konferencija();
                    k.setNaziv(rs.getString("naziv"));
                    k.setDatumPoc(rs.getString("datumpocetak"));
                    k.setDatumKraj(rs.getString("datumkraj"));
                    k.setMesto(rs.getString("mesto"));
                    Konferencije.add(k);
                    
                }
                  
                Konf = Konferencije;
                 setPorukaPretraga("");
                 setRender2(true);
                DB.getInstance().putConnection(con);
                stmt.close();
             if(!records) {
                // render =false;
                 setRender2(false);
                 setPorukaPretraga("Nema rezultata koji zadovoljavaju trazenu pretragu.");
             }
            
            
                                

            return "index";
            
        } 
        
        
           if(!datum2.equals("")&& !datum1.equals("")) {
            
            Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();  String upit;
            if (!nazivKonf.isEmpty()) {
             upit = "select * from konferencija where naziv LIKE '%"+nazivKonf+"%' and datumpocetak <= '"+datum2+"'"
                     + "and datumpocetak>='"+datum1+"';";
            }
            else {
             upit = "select * from konferencija where datumpocetak <= '"+datum2 + "'and datumpocetak>='"+datum1+"';";
            }
            ResultSet rs = stmt.executeQuery(upit);
            
            ArrayList<Konferencija> Konferencije= new ArrayList<>();
            
            boolean records = false;
             while (rs.next()) {
                    records = true;

                    Konferencija k = new Konferencija();
                    k.setNaziv(rs.getString("naziv"));
                    k.setDatumPoc(rs.getString("datumpocetak"));
                    k.setDatumKraj(rs.getString("datumkraj"));
                    k.setMesto(rs.getString("mesto"));
                    Konferencije.add(k);
                    
                }
                  
                Konf = Konferencije;
           DB.getInstance().putConnection(con);
           setPorukaPretraga("");
             setRender2(true);
                stmt.close();
             if(!records) {
              //   render =false;
                 setPorukaPretraga("Nema rezultata koji zadovoljavaju trazenu pretragu.");
                  setRender2(false);
             }
            
            
           
            return "index";
            
        } 
           
           
           
        
        if(!datum1.equals("")&& datum2.equals("")) {
            
            Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();  String upit;
            if (!nazivKonf.isEmpty()) {
             upit = "select * from konferencija where naziv LIKE '%"+nazivKonf+"%' and datumpocetak >= '"+datum1+"';";
            }
            else {
             upit = "select * from konferencija where datumpocetak >= '"+datum1+"';";
            }
            ResultSet rs = stmt.executeQuery(upit);
            
            ArrayList<Konferencija> Konferencije= new ArrayList<>();
            
            boolean records = false;
             while (rs.next()) {
                    records = true;

                    Konferencija k = new Konferencija();
                    k.setNaziv(rs.getString("naziv"));
                    k.setDatumPoc(rs.getString("datumpocetak"));
                    k.setDatumKraj(rs.getString("datumkraj"));
                    k.setMesto(rs.getString("mesto"));
                    Konferencije.add(k);
                    
                }
                  
                Konf = Konferencije;
             DB.getInstance().putConnection(con);
             setPorukaPretraga("");
              setRender2(true);
             stmt.close();
             if(!records) {
             //    render =false;
                 setPorukaPretraga("Nema rezultata koji zadovoljavaju trazenu pretragu.");
                  setRender2(false);
             }
            
            
            
            return "index";
            
        } 
        
        if(datum1.equals("") && datum2.equals("") && !nazivKonf.isEmpty()) {
            Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();
            String upit = "select * from konferencija where naziv LIKE '%"+nazivKonf+"%';";
            ResultSet rs = stmt.executeQuery(upit);
            
            ArrayList<Konferencija> Konferencije= new ArrayList<>();
            
            boolean records = false;
             while (rs.next()) {
                    records = true;

                    Konferencija k = new Konferencija();
                    k.setNaziv(rs.getString("naziv"));
                    k.setDatumPoc(rs.getString("datumpocetak"));
                    k.setDatumKraj(rs.getString("datumkraj"));
                    k.setMesto(rs.getString("mesto"));
                    Konferencije.add(k);
                    
                }
                  
                Konf = Konferencije;
           DB.getInstance().putConnection(con);
           setPorukaPretraga("");
            setRender2(true);
             stmt.close();
             if(!records) {
            //     render =false;
                 setPorukaPretraga("Nema rezultata koji zadovoljavaju trazenu pretragu.");
                 setRender2(false);
             }
         
         
    
    
    }
        return "index";
 }
 
 
 
}