package util;

import beans.Konferencija;
import beans.Korisnik;
import com.mysql.jdbc.Blob;
import static com.sun.faces.facelets.util.Path.context;
import java.io.IOException;
import java.io.Serializable;
import java.lang.Object;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Calendar;
import java.util.Locale;

import java.util.List;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import static jdk.nashorn.internal.objects.NativeMath.random;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.Session;

@SessionScoped
@ManagedBean
@ApplicationScoped

public class ControlerKonf implements Serializable {
    
    private String porukamod;
    private java.lang.Long lastmodified;
    private List Konferencije, modKonferencije;
    private List<Korisnik> posetioci;
    private boolean renderusers, slanjePoruke;
    private Korisnik profil;
    private byte[] slika;
    private String porukaOmiljen, poruka;
    private String konferencija, pocetakKonf, krajKonf;
    private String mestoKonf, statusKonf;
    private String mailbox;
    private boolean renderPosetioci = false;
    private boolean renderbuttons, isMod;
    private int modovanaKonferencija;
    private String nazivModovaneKonferencije;
    //-------------------------------------------------MODOVANJE
    private String vremeotvaranja,tipDogadjaja,sala;
    private boolean dodavanjedogadjaja=false;
    private boolean dodavanjeSesija, dodavanjeCeremonijaOtv, dodavanjeCeremonijaZatv, dodavanjeRadionica,dodavanjePauza,dodavanjePredavanje;
    private String dan,trajanje;
    private List<Date> mogucidatumi;
    private String dodavanjePoruka,nazivPredavanja;

    public String getNazivPredavanja() {
        return nazivPredavanja;
    }

    public void setNazivPredavanja(String nazivPredavanja) {
        this.nazivPredavanja = nazivPredavanja;
    }
    
    
    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }
    
    
    
    public String getDodavanjePoruka() {
        return dodavanjePoruka;
    }

    public void setDodavanjePoruka(String dodavanjePoruka) {
        this.dodavanjePoruka = dodavanjePoruka;
    }
    
    

    public List<Date> getMogucidatumi() {
        return mogucidatumi;
    }

    public void setMogucidatumi(List<Date> mogucidatumi) {
        this.mogucidatumi = mogucidatumi;
    }

    
    
    public String getDan() {
        return dan;
    }

    public void setDan(String dan) {
        this.dan = dan;
    }
    
    
    
    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }
    
    
    
    public boolean isDodavanjedogadjaja() {
        return dodavanjedogadjaja;
    }

    public void setDodavanjedogadjaja(boolean dodavanjedogadjaja) {
        this.dodavanjedogadjaja = dodavanjedogadjaja;
            dodavanjeSesija=false;
            dodavanjeCeremonijaOtv=false;
            dodavanjeCeremonijaZatv=false;
            dodavanjeRadionica=false;
            dodavanjePauza=false;
            dodavanjePredavanje=false;
    }
    
    public void dodajCeremonijuOtvaranja() throws SQLException {
        
         if (sala.isEmpty()) 
        {
            setDodavanjePoruka("Unesite salu"); 
            return;
        }
         
    Connection con = DB.getInstance().getConnection();
    Statement stmt = con.createStatement();
    String upit="select id from konferencija where naziv='"+nazivModovaneKonferencije+"';";
    ResultSet rs = stmt.executeQuery(upit);
        rs=stmt.executeQuery(upit); int idKonf = 0;
        if (rs.next())
        {
            idKonf=rs.getInt("id");
            
        }
        upit="select * from sesija where idkonf='"+idKonf+"' and tip='4';";
     rs =stmt.executeQuery(upit);
    if (rs.next()) {
        setDodavanjePoruka("Ova konferencija vec ima unetu ceremoniju otvaranja.");
        return;
    }
   
}
    public void dodajSesiju() throws SQLException {
        if (sala.isEmpty()) 
        {
            setDodavanjePoruka("Unesite salu"); 
            return;
        }
        Connection con = DB.getInstance().getConnection();
        Statement stmt=con.createStatement();
        String upit="Select * from sesija where sala='"+sala+"' and datum='"+dan+"';";
        ResultSet rs = stmt.executeQuery(upit);
        if (rs.next()) 
        {
            setDodavanjePoruka("Trazena sala je zauzeta u ovom terminu.");
            DB.getInstance().putConnection(con);
            stmt.close();
            return;
        }
        
        upit="select id from konferencija where naziv='"+nazivModovaneKonferencije+"';";
        rs=stmt.executeQuery(upit); int idKonf = 0;
        if (rs.next())
        {
            idKonf=rs.getInt("id");
            
        }
        upit="insert into sesija (sala,datum,idkonf) values ('"+sala+"','"+dan+"','"+idKonf+"');";
        stmt.executeUpdate(upit);
        setDodavanjePoruka("Sesija uspesno kreirana.");
        DB.getInstance().putConnection(con);
        stmt.close();
        
    }
    
    
    
    public void biranjeTipaDogadjaja() throws SQLException {
        if (tipDogadjaja.equals("Sesija"))
        {   
            Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();
            String upit="select datumpocetak,datumkraj from konferencija where naziv='"+nazivModovaneKonferencije+"';";
            ResultSet rs = stmt.executeQuery(upit);
            Date datum1=null,datum2=null;
            if(rs.next()) {
             datum1 = rs.getDate("datumpocetak");
             datum2 = rs.getDate("datumkraj");
            }
            List datumi= new ArrayList<>();
            
            while (datum2.after(datum1))
            {
                datumi.add(datum1);
            
              datum1= new java.sql.Date( datum1.getTime() + 24*60*60*1000);
            }
            setMogucidatumi(datumi);
            
            dodavanjeSesija=true;
            dodavanjeCeremonijaOtv=false;
            dodavanjeCeremonijaZatv=false;
            dodavanjeRadionica=false;
            dodavanjePauza=false;
            dodavanjePredavanje=false;
            DB.getInstance().putConnection(con);
            stmt.close();
        }
        
        if (tipDogadjaja.equals("Ceremonija otvaranja"))
        {    
             Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();
            String upit="select datumpocetak,datumkraj from konferencija where naziv='"+nazivModovaneKonferencije+"';";
            ResultSet rs = stmt.executeQuery(upit);
            Date datum1=null;
            if(rs.next()) {
             datum1 = rs.getDate("datumpocetak");
            }
            dodavanjeSesija=false;
            dodavanjeCeremonijaOtv=true;
            dodavanjeCeremonijaZatv=false;
            dodavanjeRadionica=false;
            dodavanjePauza=false;
            dodavanjePredavanje=false;
            setPocetakKonf(datum1.toString());
        }
        
        if (tipDogadjaja.equals("Ceremonija zatvaranja"))
        {
            dodavanjeSesija=false;
            dodavanjeCeremonijaOtv=false;
            dodavanjeCeremonijaZatv=true;
            dodavanjeRadionica=false;
            dodavanjePauza=false;
            dodavanjePredavanje=false;
        }
        
        if (tipDogadjaja.equals("Radionica"))
        {
            dodavanjeSesija=false;
            dodavanjeCeremonijaOtv=false;
            dodavanjeCeremonijaZatv=false;
            dodavanjeRadionica=true;
            dodavanjePauza=false;
            dodavanjePredavanje=false;
        }
        
        if (tipDogadjaja.equals("Pauza"))
        {
            dodavanjeSesija=false;
            dodavanjeCeremonijaOtv=false;
            dodavanjeCeremonijaZatv=false;
            dodavanjeRadionica=false;
            dodavanjePauza=true;
            dodavanjePredavanje=false;
        }
        if (tipDogadjaja.equals("Predavanje"))
        {
             Connection con = DB.getInstance().getConnection();
            Statement stmt=con.createStatement();
            String upit="select datumpocetak,datumkraj from konferencija where naziv='"+nazivModovaneKonferencije+"';";
            ResultSet rs = stmt.executeQuery(upit);
            Date datum1=null,datum2=null;
            if(rs.next()) {
             datum1 = rs.getDate("datumpocetak");
             datum2 = rs.getDate("datumkraj");
            }
            List datumi= new ArrayList<>();
            
            while (datum2.after(datum1))
            {
                datumi.add(datum1);
            
              datum1= new java.sql.Date( datum1.getTime() + 24*60*60*1000);
            }
            setMogucidatumi(datumi);
            
            dodavanjeSesija=false;
            dodavanjeCeremonijaOtv=false;
            dodavanjeCeremonijaZatv=false;
            dodavanjeRadionica=false;
            dodavanjePauza=false;
            dodavanjePredavanje=false;
            dodavanjePredavanje=true;
        }
        
    }

    public boolean isDodavanjePredavanje() {
        return dodavanjePredavanje;
    }

    public void setDodavanjePredavanje(boolean dodavanjePredavanje) {
        this.dodavanjePredavanje = dodavanjePredavanje;
    }

    
    public boolean isDodavanjeSesija() {
        return dodavanjeSesija;
    }

    public void setDodavanjeSesija(boolean dodavanjeSesija) {
        this.dodavanjeSesija = dodavanjeSesija;
    }

    public boolean isDodavanjeCeremonijaOtv() {
        return dodavanjeCeremonijaOtv;
    }

    public void setDodavanjeCeremonijaOtv(boolean dodavanjeCeremonijaOtv) {
        this.dodavanjeCeremonijaOtv = dodavanjeCeremonijaOtv;
    }

    public boolean isDodavanjeCeremonijaZatv() {
        return dodavanjeCeremonijaZatv;
    }

    public void setDodavanjeCeremonijaZatv(boolean dodavanjeCeremonijaZatv) {
        this.dodavanjeCeremonijaZatv = dodavanjeCeremonijaZatv;
    }

    public boolean isDodavanjeRadionica() {
        return dodavanjeRadionica;
    }

    public void setDodavanjeRadionica(boolean dodavanjeRadionica) {
        this.dodavanjeRadionica = dodavanjeRadionica;
    }

    public boolean isDodavanjePauza() {
        return dodavanjePauza;
    }

    public void setDodavanjePauza(boolean dodavanjePauza) {
        this.dodavanjePauza = dodavanjePauza;
    }

    public String getTipDogadjaja() {
        return tipDogadjaja;
    }

    public void setTipDogadjaja(String tipDogadjaja) {
        this.tipDogadjaja = tipDogadjaja;
    }
    
    
    
    
    
    
    
    
    
    
    

    public String getVremeotvaranja() {
        return vremeotvaranja;
    }

    public void setVremeotvaranja(String vremeotvaranja) {
        this.vremeotvaranja = vremeotvaranja;
    }

    public String getNazivModovaneKonferencije() {
        return nazivModovaneKonferencije;
    }

    public void setNazivModovaneKonferencije(String nazivModovaneKonferencije) {
        this.nazivModovaneKonferencije = nazivModovaneKonferencije;
    }

    public String setUpKonf(int idKonf) throws SQLException {
       
         setPorukamod("");
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit = "select * from konferencija where id='" + idKonf + "';";
        ResultSet rs = stmt.executeQuery(upit);
        if (rs.next()) {
            if (rs.getDate("datumpocetak").before(Calendar.getInstance().getTime())) {
                 DB.getInstance().putConnection(con);
                stmt.close();
                setPorukamod("Ne mozete trenutno urediti ovu konferenciju (U toku je ili je zavrsena)");
                return "mojekonferencije";
            }
            setNazivModovaneKonferencije(rs.getString("naziv"));
        }
        DB.getInstance().putConnection(con);
        stmt.close();
        setModovanaKonferencija(idKonf);
         FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("mod", idKonf);
        return "moderator";
    }

    public int getModovanaKonferencija() {
        return modovanaKonferencija;
    }

    public void setModovanaKonferencija(int modovanaKonferencija) {
        this.modovanaKonferencija = modovanaKonferencija;
    }

    public boolean isIsMod() {
        return isMod;
    }

    public void setIsMod(boolean isMod) {
        this.isMod = isMod;
    }

    public boolean isRenderbuttons() {
        return renderbuttons;
    }

    public void setRenderbuttons(boolean renderbuttons) {
        this.renderbuttons = renderbuttons;
    }

    public List getModKonferencije() {
        return modKonferencije;
    }

    public void setModKonferencije(List modKonferencije) {
        this.modKonferencije = modKonferencije;
    }

    public void korisnikReset() {
        renderPosetioci = false;

    }

    public void mojeKonfReset() {
        slanjePoruke = false;
    }

    public String mojprofil() throws SQLException {
        slika = null;
        setLastmodified(Calendar.getInstance().getTimeInMillis());
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Integer userid = (Integer) session.getAttribute("id");
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit = "select * from user where id='" + userid + "';";
        ResultSet rs = stmt.executeQuery(upit);
        Korisnik k = new Korisnik();
        if (rs.next()) {

            k.setIme(rs.getString("ime"));
            k.setPrezime(rs.getString("prezime"));
            k.setInstitucija(rs.getString("institucija"));
            k.setLinkedin(rs.getString("linkedin"));
            k.setUsername(rs.getString("username"));
            k.setEmail(rs.getString("mail"));

            slika = rs.getBlob("slika").getBytes(1, (int) rs.getBlob("slika").length());
        }
        DB.getInstance().putConnection(con);
        stmt.close();
        profil = k;
        renderProfileButtons(false);
        return "profil";
    }

    public void renderProfileButtons(boolean b) {
        renderbuttons = b;
    }

    public String program(int idKonf) throws SQLException {
        renderPosetioci = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        session.setAttribute("idKonf", idKonf);
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit = "select * from konferencija where id='" + idKonf + "';";
        ResultSet rs = stmt.executeQuery(upit);
        if (rs.next()) {
            setKonferencija(rs.getString("naziv"));
            setPocetakKonf(rs.getString("datumpocetak"));
            setKrajKonf(rs.getString("datumkraj"));
            setMestoKonf(rs.getString("grad"));
            if (rs.getDate("datumpocetak").after(Calendar.getInstance().getTime())) {
                setStatusKonf("Aktivna");
            }
            if ((rs.getDate("datumpocetak").before(Calendar.getInstance().getTime())) && (rs.getDate("datumkraj").after(Calendar.getInstance().getTime()))) {
                setStatusKonf("U toku");
            }
            if (rs.getDate("datumkraj").before(Calendar.getInstance().getTime())) {
                setStatusKonf("Završena");
            }
        }

        stmt.close();
        DB.getInstance().putConnection(con);

        renderusers = false;
        return "programkonf";

    }

    public String getStatusKonf() {
        return statusKonf;
    }

    public void setStatusKonf(String statusKonf) {
        this.statusKonf = statusKonf;
    }

    public String Posalji(int id) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Integer userid = (Integer) session.getAttribute("id");
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit = "insert into mailbox (sender,reciever,content,owner) VALUES ('" + userid
                + "','" + id + "','" + poruka + "','" + id + "')";

        stmt.executeUpdate(upit);
        upit = "insert into mailbox (sender,reciever,content,owner) VALUES ('" + userid
                + "','" + id + "','" + poruka + "','" + userid + "')";

        stmt.executeUpdate(upit);
        DB.getInstance().putConnection(con);
        stmt.close();
        setSlanjePoruke(false);

        return "profil";
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String odustajanjeodslanja() {

        setSlanjePoruke(false);
        return "profil";
    }

    public String slanjeporuke() {

        setSlanjePoruke(true);
        return "profil";
    }

    public String getMestoKonf() {
        return mestoKonf;
    }

    public void setMestoKonf(String mestoKonf) {
        this.mestoKonf = mestoKonf;
    }

    public String getKonferencija() {
        return konferencija;
    }

    public void setKonferencija(String konferencija) {
        this.konferencija = konferencija;
    }

    public String getPocetakKonf() {
        return pocetakKonf;
    }

    public void setPocetakKonf(String pocetakKonf) {
        this.pocetakKonf = pocetakKonf;
    }

    public String getKrajKonf() {
        return krajKonf;
    }

    public void setKrajKonf(String krajKonf) {
        this.krajKonf = krajKonf;
    }

    public String omiljen(Integer id) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Integer userid = (Integer) session.getAttribute("id");
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit = "select * from omiljen where idkor1='" + userid + "' and idkor2='" + id + "';";
        ResultSet rs = stmt.executeQuery(upit);
        if (rs.next()) {
            setPorukaOmiljen("Ovaj korisnik se vec nalazi u listi vasih omiljenih korisnika.");
            DB.getInstance().putConnection(con);
            stmt.close();
            return "profil";

        }

        upit = "Insert into omiljen (idkor1,idkor2) VALUES ('" + userid + "','" + id + "');";
        stmt.executeUpdate(upit);
        setPorukaOmiljen("Korisnik uspesno dodat u listu omiljenih.");
        DB.getInstance().putConnection(con);
        stmt.close();

        return "profil";
    }

    public String pogledajProfil(String iduser) throws SQLException {
        slika = null;
        setLastmodified(Calendar.getInstance().getTimeInMillis());
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit = "select * from user where username='" + iduser + "';";
        ResultSet rs = stmt.executeQuery(upit);
        Korisnik k = new Korisnik();
        while (rs.next()) {

            k.setIme(rs.getString("ime"));
            k.setPrezime(rs.getString("prezime"));
            k.setLinkedin(rs.getString("linkedin"));
            k.setEmail(rs.getString("mail"));
            k.setUsername(rs.getString("username"));
            k.setInstitucija(rs.getString("institucija"));
            k.setId(Integer.parseInt(rs.getString("id")));
            slika = rs.getBlob("slika").getBytes(1, (int) rs.getBlob("slika").length());
        }

        setProfil(k);
        DB.getInstance().putConnection(con);
        stmt.close();
        renderProfileButtons(true);
        return "profil";
    }

    public String posetioci(String idkonf) throws SQLException {
        if (renderPosetioci == false) {
            renderPosetioci = true;
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);

            String username = (String) session.getAttribute("username");
            Connection con = DB.getInstance().getConnection();
            Statement stmt = con.createStatement();
            Statement stmt2 = con.createStatement();
            String upit = "Select userid from linkkonfuser where konferensid='" + idkonf + "';";
            ResultSet rs = stmt.executeQuery(upit);
            List<Korisnik> prijavljeni = new ArrayList<>();
            while (rs.next()) {
                String userid = rs.getString("userid");
                upit = "Select * from user where id='" + userid + "';";
                ResultSet rs2 = stmt2.executeQuery(upit);
                while (rs2.next()) {
                    Korisnik k = new Korisnik();
                    k.setIme(rs2.getString("ime"));
                    k.setPrezime(rs2.getString("prezime"));
                    k.setUsername(rs2.getString("username"));
                    k.setInstitucija(rs2.getString("institucija"));
                    k.setEmail(rs2.getString("mail"));
                    k.setLinkedin(rs2.getString("linkedin"));
                    if (!k.getUsername().equals(username)) {
                        prijavljeni.add(k);
                    }

                }
            }
            setPosetioci(prijavljeni);
            DB.getInstance().putConnection(con);
            stmt.close();
            stmt2.close();
            renderPosetioci = true;
            return "mojekonferencije";

        } else {
            renderPosetioci = false;
            return "mojekonferencije";
        }
    }

    public void onLoad() throws SQLException {
        renderusers = false;
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        Statement stmt2 = con.createStatement();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Integer userid = (Integer) session.getAttribute("id");
        String username = (String) session.getAttribute("username");
        String upit = "Select * from linkkonfuser where userid='" + userid + "';";
        ResultSet rs = stmt.executeQuery(upit);
        boolean imaKonf = false;
        isMod = false;
        boolean insert = false;
        List<Konferencija> Prijavljene = new ArrayList<>();
        List<Konferencija> Modovane = new ArrayList<>();
        while (rs.next()) {
            if (rs.getInt("ismod") == 1) {
                isMod = true;
                insert = true;
            }

            renderusers = true;
            imaKonf = true;
            String konfid = rs.getString("konferensid");
            upit = "Select * from konferencija where id='" + konfid + "';";

            ResultSet rs2 = stmt2.executeQuery(upit);

            Konferencija k = new Konferencija();
            while (rs2.next()) {
                k.setNaziv(rs2.getString("naziv"));
                k.setMesto(rs2.getString("mesto"));
                k.setDatumPoc(rs2.getString("datumpocetak"));
                k.setDatumKraj(rs2.getString("datumkraj"));
                k.setId(rs2.getString("id"));
                Prijavljene.add(k);
                if (insert == true) {
                    Modovane.add(k);
                    insert = false;

                }

            }
        }

        DB.getInstance().putConnection(con);
        stmt.close();
        stmt2.close();
        setKonferencije(Prijavljene);
        setModKonferencije(Modovane);

    }

    //-----GS
    public List getKonferencije() {
        return Konferencije;
    }

    public void setKonferencije(List Konferencije) {
        this.Konferencije = Konferencije;
    }

    public boolean isRender() {
        return renderusers;
    }

    public void setRender(boolean render) {
        this.renderusers = renderusers;
    }

    public List getPosetioci() {
        return posetioci;
    }

    public void setPosetioci(List posetioci) {
        this.posetioci = posetioci;
    }

    public boolean isRenderPosetioci() {
        return renderPosetioci;
    }

    public void setRenderPosetioci(boolean renderPosetioci) {
        this.renderusers = renderPosetioci;
    }

    public Korisnik getProfil() {
        return profil;
    }

    public void setProfil(Korisnik profil) {
        this.profil = profil;
    }

    public String getPorukaOmiljen() {
        return porukaOmiljen;
    }

    public void setPorukaOmiljen(String porukaOmiljen) {
        this.porukaOmiljen = porukaOmiljen;
    }

    public boolean isSlanjePoruke() {
        return slanjePoruke;
    }

    public void setSlanjePoruke(boolean slanjePoruke) {
        this.slanjePoruke = slanjePoruke;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public byte[] getSlika() {
        return slika;
    }

    public void setSlika(byte[] slika) {
        this.slika = slika;
    }

    public java.lang.Long getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(java.lang.Long x) {
        this.lastmodified = x;
    }

    public String getPorukamod() {
        return porukamod;
    }

    public void setPorukamod(String porukamod) {
        this.porukamod = porukamod;
    }
    
}
