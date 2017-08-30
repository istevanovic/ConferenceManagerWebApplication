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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@SessionScoped
@ManagedBean
public class ControlerKorisnik implements Serializable {
 
 private ArrayList<String> autosuggest;
 private ArrayList<Konferencija> Konf,Konfpretraga;
 private ArrayList<Poruka> poruke;
 ArrayList<Integer> prijave;
 private String porukaSanduce,porukaPrijavljivanje;
 private String mailbox;
 private String poruka;
 //------------------------------ pretraga
 private String NazivKonf,dan1,mesec1,godina1,dan2,mesec2,godina2,oblast,porukaPretraga;
 //------------------------------prijava
 private String porukaPrijava,idKonf;
 private boolean render,renderMailbox = false;
 private boolean modslanje = false;
 private boolean renderPristupForm = false;
 private String pristupnalozinka;

    public String getPristupnalozinka() {
        return pristupnalozinka;
    }

    public void setPristupnalozinka(String pristupnalozinka) {
        this.pristupnalozinka = pristupnalozinka;
    }

    public ArrayList<String> getAutosuggest() throws SQLException {
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit="select naziv from konferencija;";
        ResultSet rs=stmt.executeQuery(upit);
        autosuggest = new ArrayList<>();
        while (rs.next()) {
            String naziv = '"'+rs.getString("naziv")+'"';
            
            autosuggest.add(naziv);
        }
        DB.getInstance().putConnection(con);
        stmt.close();
        return autosuggest;
    }

    public void setAutosuggest(ArrayList<String> autosuggest) {
        this.autosuggest = autosuggest;
    }
 
 

 
 public void closeMailbox() {
     renderMailbox=false;
 }
 
 public void posalji(String ide) throws SQLException {
        String ime = ide;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Integer userid  = (Integer) session.getAttribute("id");
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit = "select id from user where username='"+ime+"';";
        ResultSet rs = stmt.executeQuery(upit);String id="";
        if (rs.next() ) {
            id = rs.getString("id");
        }
        
         upit="insert into mailbox (sender,reciever,content,owner) VALUES ('"+userid+
                "','"+id+"','"+poruka+"','"+id+"')";
        
        stmt.executeUpdate(upit);
         upit="insert into mailbox (sender,reciever,content,owner) VALUES ('"+userid+
                "','"+id+"','"+poruka+"','"+userid+"')";
        
        stmt.executeUpdate(upit);
        DB.getInstance().putConnection(con);
        stmt.close();
        nemodslanje();
        messages();
                
        
        
        
       
       
   }
    
    public String formprijava(String id) throws SQLException {
         idKonf = id;
    Connection con = DB.getInstance().getConnection();
    Statement stmt=con.createStatement();
    FacesContext context = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
    Integer userid  = (Integer) session.getAttribute("id");
    String username = (String) session.getAttribute("username");
    String upit="select * from linkkonfuser where userid='"+userid+"' and konferensid='"+idKonf+"';";
    ResultSet rs = stmt.executeQuery(upit);
    if(rs.next()){ 
        setPorukaPrijavljivanje("Vec ste prijavljeni na ovu konferenciju.");
        DB.getInstance().putConnection(con);
         setRenderPristupForm(false);
        stmt.close(); return "korisnik";}
        
    else {
        
        setPorukaPrijavljivanje("");
        session.setAttribute("konferensapply", idKonf);
        setRenderPristupForm(true);
         DB.getInstance().putConnection(con);
         stmt.close();
        return "korisnik";
       
    }
    }
    
    public String neformprijava() {
         setPorukaPrijavljivanje("");
          setRenderPristupForm(false);
        return "korisnik";
    }

    public boolean isRenderPristupForm() {
        return renderPristupForm;
    }

    public void setRenderPristupForm(boolean renderPristupForm) {
        this.renderPristupForm = renderPristupForm;
    }
 
    

    public String getPorukaPrijavljivanje() {
        return porukaPrijavljivanje;
    }

    public void setPorukaPrijavljivanje(String porukaPrijavljivanje) {
        this.porukaPrijavljivanje = porukaPrijavljivanje;
    }
 
 
    
 
    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
 
 
 
 
 
 
 
 public String mojekonferencije() {
     closeMailbox();
     setPorukaPretraga("");
     return "mojekonferencije";
 }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public boolean isModslanje() {
        return modslanje;
    }

    public void setModslanje(boolean modslanje) {
        this.modslanje = modslanje;
    }
    
    public void modslanje() {
        this.modslanje=true;
    }
    
    public void nemodslanje() {
        this.modslanje=false;
    }
 
 
 public String prijava() throws SQLException {
     
  
    FacesContext context = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) context.getExternalContext().getSession(true); 
     
    idKonf = (String) session.getAttribute("konferensapply");
    Connection con = DB.getInstance().getConnection();
    Statement stmt=con.createStatement();
    
    String upit="Select * from konferencija where id='"+idKonf+"';";
    ResultSet rs = stmt.executeQuery(upit);
    
    if (rs.next()) {
        if (!rs.getString("pristup").equals(pristupnalozinka)) {
            setPorukaPrijavljivanje("Pogresna pristupna lozinka, pokusajte opet.");
            return "korisnik";
        }
        
        
    }
    
    
    
    
  
    Integer userid  = (Integer) session.getAttribute("id");
    String username = (String) session.getAttribute("username");
     upit="select * from linkkonfuser where userid='"+userid+"' and konferensid='"+idKonf+"';";
   
    
    upit="select * from konferencija where id='"+idKonf+"';";
     rs = stmt.executeQuery(upit); int a = 0;  Date datumPoc = null;
    
    /////////////////////// PROVERA 3 DANA ///////////////////////////////////////////////
    
     upit="insert into linkkonfuser (userid,konferensid) VALUES ('"+userid+"','"+idKonf+"');";
     stmt.executeUpdate(upit);
     setPorukaPrijavljivanje("Uspesno ste se prijavili na konferenciju.");
     setRenderPristupForm(false);
    
    
    DB.getInstance().putConnection(con);stmt.close();
    setPorukaPrijava("");
    return "korisnik";
 }
 
 public void MojeKonfReset() {
     setPorukaPrijava("");
     neformprijava();
 }
 
 public String pretrazi() throws SQLException {
     render=false;
        setPorukaPrijavljivanje("");
        setRenderPristupForm(false);
         String datum1="", datum2 = "";
        if(NazivKonf.isEmpty() && dan1.equals("-")&& mesec1.equals("-")&& godina1.equals("-")
                && dan2.equals("-")&& mesec2.equals("-")&& godina2.equals("-")&&oblast.equals("-"))
            setPorukaPretraga("Unesite parametre pretrage.");
        
        if(NazivKonf.isEmpty() && dan1.equals("-")&& mesec1.equals("-")&& godina1.equals("-")
                && dan2.equals("-")&& mesec2.equals("-")&& godina2.equals("-") && !oblast.equals("-"))
        {
            String upit = "select * from konferencija where oblast='"+oblast+"';";
             Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();
           ResultSet rs = stmt.executeQuery(upit);
            
            ArrayList<Konferencija> Konferencije= new ArrayList<>();
            
            boolean records = false;
             while (rs.next()) {
                    records = true;

                    Konferencija k = new Konferencija();
                    k.setNaziv(rs.getString("naziv"));
                    k.setId(rs.getString("id"));
                    k.setDatumPoc(rs.getString("datumpocetak"));
                    k.setDatumKraj(rs.getString("datumkraj"));
                    k.setMesto(rs.getString("mesto"));
                    Konferencije.add(k);
                    
                }
                  
                Konfpretraga = Konferencije; setPorukaPretraga("");
                
              render=true;
             if(!records) {
                render =false;
                 setPorukaPretraga("Nema rezultata koji zadovoljavaju trazenu pretragu.");
             }
              DB.getInstance().putConnection(con);
                 stmt.close();
        }
            
        if((!dan1.equals("-")) && (!mesec1.equals("-"))&&( !godina1.equals("-"))) {
            datum1 = godina1 + "-" + mesec1 + "-" + dan1;
        }
        
        if((!dan2.equals("-")) && (!mesec2.equals("-"))&&( !godina2.equals("-"))) {
            datum2 = godina2 + "-" + mesec2 + "-" + dan2;
        }
        
         if(!datum2.equals("")&& datum1.equals("")) {
            
            Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();  String upit;
            if (!NazivKonf.isEmpty()) {
             upit = "select * from konferencija where naziv LIKE '%"+NazivKonf+"%' and datumpocetak <= '"+datum2+"';";
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
                     k.setId(rs.getString("id"));
                    k.setDatumPoc(rs.getString("datumpocetak"));
                    k.setDatumKraj(rs.getString("datumkraj"));
                    k.setMesto(rs.getString("mesto"));
                    Konferencije.add(k);
                     
                }
                  
                Konfpretraga = Konferencije;
                setPorukaPretraga("");
                  DB.getInstance().putConnection(con);
                 stmt.close();
               render=true;
             if(!records) {
                render =false;
                 setPorukaPretraga("Nema rezultata koji zadovoljavaju trazenu pretragu.");
             }
            
            
            
            return "korisnik";
            
        } 
        
        
           if(!datum2.equals("")&& !datum1.equals("")) {
            
            Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();  String upit;
            if (!NazivKonf.isEmpty()) {
             upit = "select * from konferencija where naziv LIKE '%"+NazivKonf+"%' and datumpocetak <= '"+datum2+"'"
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
                    k.setId(rs.getString("id"));
                    k.setDatumPoc(rs.getString("datumpocetak"));
                    k.setDatumKraj(rs.getString("datumkraj"));
                    k.setMesto(rs.getString("mesto"));
                    Konferencije.add(k);
                    
                }
                  
                Konfpretraga = Konferencije;
            render=true;
             if(!records) {
             render =false;
                 setPorukaPretraga("Nema rezultata koji zadovoljavaju trazenu pretragu.");
             }
            
            
            DB.getInstance().putConnection(con);
            stmt.close();
            return "korisnik";
            
        } 
           
           
           
        
        if(!datum1.equals("")&& datum2.equals("")) {
            
            Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();  String upit;
            if (!NazivKonf.isEmpty()) {
             upit = "select * from konferencija where naziv LIKE '%"+NazivKonf+"%' and datumpocetak >= '"+datum1+"';";
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
                    k.setId(rs.getString("id"));
                    k.setDatumPoc(rs.getString("datumpocetak"));
                    k.setDatumKraj(rs.getString("datumkraj"));
                    k.setMesto(rs.getString("mesto"));
                    Konferencije.add(k);
                    
                }
                 DB.getInstance().putConnection(con);
                 stmt.close();
                Konfpretraga = Konferencije;
              render=true;
             if(!records) {
             render =false;
                 setPorukaPretraga("Nema rezultata koji zadovoljavaju trazenu pretragu.");
             }
            
            
            
            return "korisnik";
            
        } 
        
        if(datum1.equals("") && datum2.equals("") && !NazivKonf.isEmpty()) {
            Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();
            String upit = "select * from konferencija where naziv LIKE '%"+NazivKonf+"%';";
            ResultSet rs = stmt.executeQuery(upit);
            DB.getInstance().putConnection(con);
            ArrayList<Konferencija> Konferencije= new ArrayList<>();
            
            boolean records = false;
             while (rs.next()) {
                    records = true;

                    Konferencija k = new Konferencija();
                    k.setNaziv(rs.getString("naziv"));
                   k.setId(rs.getString("id"));
                    k.setDatumPoc(rs.getString("datumpocetak"));
                    k.setDatumKraj(rs.getString("datumkraj"));
                    k.setMesto(rs.getString("mesto"));
                    Konferencije.add(k);
                    
                }
                   render=true;
                Konfpretraga = Konferencije;
                 DB.getInstance().putConnection(con);
                   setPorukaPretraga("");
                stmt.close();
            ///    render = true;
             if(!records) {
                 render =false;
                 setPorukaPretraga("Nema rezultata koji zadovoljavaju trazenu pretragu.");
             }
         
         
    
    
    }
        return "korisnik";
 
 }
  public String odjava() {
       FacesContext context = FacesContext.getCurrentInstance();
       HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
       session.invalidate();
        return "index";
    }
 
 
    public void onLoadPrijave() throws SQLException {
     Connection con = DB.getInstance().getConnection();
     Statement stmt=con.createStatement();
      FacesContext context = FacesContext.getCurrentInstance();
      HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
      Integer userid = (Integer) session.getAttribute("id");
    
     String upit ="SELECT * FROM linkkonfuser where userid='"+userid+"'";
     ResultSet rs = stmt.executeQuery(upit);
     boolean imaprijava=false; int nprijava=0;
     ArrayList<Integer> prijave = new ArrayList<>(); 
     while(rs.next()) {
         imaprijava=true;nprijava++;
         prijave.add(rs.getInt("konferensid"));
     }
      ArrayList<Konferencija> Konferencije= new ArrayList<>();
     while(nprijava != 0) {
         int id = prijave.get(nprijava-1);
         upit="SELECT * from konferencija where id='"+id+"';";
         rs =stmt.executeQuery(upit);
         Konferencija k = new Konferencija();
         
         while (rs.next()) {
         k.setDatumKraj(rs.getString("datumkraj"));
         k.setDatumPoc(rs.getString("datumpocetak"));
         k.setMesto(rs.getString("mesto"));
         k.setNaziv((rs.getString("naziv")));
         Konferencije.add(k);
         nprijava--;
         }
     }
          setKonf(Konferencije);
          DB.getInstance().putConnection(con);
          stmt.close();
       
        
    }
    
     public String obrisipor(int id) throws SQLException {
         Connection con = DB.getInstance().getConnection();
         Statement stmt = con.createStatement();
         String upit = "Delete from mailbox where messageid='"+id+"';";
         stmt.executeUpdate(upit);
         stmt.close();
         DB.getInstance().putConnection(con);
         messages();
         return "korisnik";
     }
    
     public String messages() throws SQLException {
         setPorukaPretraga("");
       setRenderMailbox(true);  
       if (mailbox.equals("I")) {          Connection con = DB.getInstance().getConnection();
     Statement stmt=con.createStatement();
     Statement stmt2=con.createStatement();
      FacesContext context = FacesContext.getCurrentInstance();
      HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
      Integer userid = (Integer) session.getAttribute("id");
      String username = (String) session.getAttribute("username");
     String upit ="SELECT * FROM mailbox where reciever='"+userid+"' and owner='"+userid+"';";
     ResultSet rs = stmt.executeQuery(upit);
     boolean imaporuka = false;
    ArrayList<Poruka> Poruke = new ArrayList<>();
     while (rs.next()) {
         imaporuka=true;
         String sender = rs.getString("sender");
         upit ="SELECT * from user where id='"+sender+"';";
         Poruka p = new Poruka();
         p.setSadrzaj((rs.getString("content")));
         p.setPrimalac(username);
         p.setMessageid(rs.getInt("messageid"));
         ResultSet rs2 = stmt2.executeQuery(upit);
          if (rs2.next()) {
              p.setPosiljalac(rs2.getString("username"));
          }
            Poruke.add(p);
        
         }
     if (imaporuka == false )  setPorukaSanduce("Sanduce je prazno");
       setPoruke(Poruke);
       DB.getInstance().putConnection(con);
          stmt.close();}
       else {          Connection con = DB.getInstance().getConnection();
     Statement stmt=con.createStatement();
     Statement stmt2=con.createStatement();
      FacesContext context = FacesContext.getCurrentInstance();
      HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
      Integer userid = (Integer) session.getAttribute("id");
      String username = (String) session.getAttribute("username");
     String upit ="SELECT * FROM mailbox where sender='"+userid+"' and owner='"+userid+"';";
     ResultSet rs = stmt.executeQuery(upit);
     boolean imaporuka = false;
    ArrayList<Poruka> Poruke = new ArrayList<>();
     while (rs.next()) {
         imaporuka=true;
         String reciever = rs.getString("reciever");
         upit ="SELECT * from user where id='"+reciever+"';";
         Poruka p = new Poruka();
         p.setSadrzaj((rs.getString("content")));
         p.setMessageid(rs.getInt("messageid"));
        
         ResultSet rs2 = stmt2.executeQuery(upit);
          if (rs2.next()) {
              p.setPrimalac(rs2.getString("username"));
               p.setPosiljalac(rs2.getString("username"));
          }
            Poruke.add(p);
        
         }
     if (imaporuka == false )  setPorukaSanduce("Sanduce je prazno");
       setPoruke(Poruke);
       DB.getInstance().putConnection(con);
          stmt.close();
       }
      
       
      return "korisnik";  
    }
    
    
    
    public void onLoadPoruke() throws SQLException {
          Connection con = DB.getInstance().getConnection();
     Statement stmt=con.createStatement();
     Statement stmt2=con.createStatement();
      FacesContext context = FacesContext.getCurrentInstance();
      HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
      Integer userid = (Integer) session.getAttribute("id");
      String username = (String) session.getAttribute("username");
     String upit ="SELECT * FROM mailbox where reciever='"+userid+"' and owner='"+userid+"';";
     ResultSet rs = stmt.executeQuery(upit);
     boolean imaporuka = false;
    ArrayList<Poruka> Poruke = new ArrayList<>();
     while (rs.next()) {
         imaporuka=true;
         String sender = rs.getString("sender");
         upit ="SELECT * from user where id='"+sender+"';";
         Poruka p = new Poruka();
         p.setSadrzaj((rs.getString("content")));
         p.setPrimalac(username);
         ResultSet rs2 = stmt2.executeQuery(upit);
          if (rs2.next()) {
              p.setPosiljalac(rs2.getString("username"));
          }
            Poruke.add(p);
        
         }
     if (imaporuka == false )  setPorukaSanduce("Sanduce je prazno");
       setPoruke(Poruke);
       DB.getInstance().putConnection(con);
          stmt.close();
    }

    public String getIdKonf() {
        return idKonf;
    }

    public void setIdKonf(String idKonf) {
        this.idKonf = idKonf;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    
    
    public String getPorukaPrijava() {
        return porukaPrijava;
    }

    public void setPorukaPrijava(String porukaPrijava) {
        this.porukaPrijava = porukaPrijava;
    }

    
    
    
    public String getPorukaPretraga() {
        return porukaPretraga;
    }

    public void setPorukaPretraga(String porukaPretraga) {
        this.porukaPretraga = porukaPretraga;
    }
    
    
    
    public String getNazivKonf() {
        return NazivKonf;
    }

    public void setNazivKonf(String NazivKonf) {
        this.NazivKonf = NazivKonf;
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

    public String getOblast() {
        return oblast;
    }

    //------------------------------------------------------GETSET
    public void setOblast(String oblast) {    
        this.oblast = oblast;
    }

    public void setPorukaSanduce(String porukaSanduce) {
        this.porukaSanduce = porukaSanduce;
    }

    
    
    
    public ArrayList<Integer> getPrijave() {
        return prijave;
    }

    public void setPrijave(ArrayList<Integer> prijave) {
        this.prijave = prijave;
    }
     
  

    public ArrayList<Poruka> getPoruke() {
        return poruke;
    }

    public void setPoruke(ArrayList<Poruka> poruke) {
        this.poruke = poruke;
    }
 
    
 
    
    public ArrayList<Konferencija> getKonf() {
        return Konf;
    }

    public void setKonf(ArrayList<Konferencija> Konf) {
        this.Konf = Konf;
    }

    public ArrayList<Konferencija> getKonfpretraga() {
        return Konfpretraga;
    }

    public void setKonfpretraga(ArrayList<Konferencija> Konfpretraga) {
        this.Konfpretraga = Konfpretraga;
    }

    public boolean isRenderMailbox() {
        return renderMailbox;
    }

    public void setRenderMailbox(boolean renderMailbox) {
        this.renderMailbox = renderMailbox;
    }
    
    
 
 
 
 
}