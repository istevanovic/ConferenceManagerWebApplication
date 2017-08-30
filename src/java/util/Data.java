/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


import beans.Korisnik;
import com.mysql.jdbc.Blob;
import java.io.Serializable;
import java.sql.Connection;
import beans.Image;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import util.ImageStoreBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Query;
import org.hibernate.Session;
import static org.hibernate.type.StandardBasicTypes.BLOB;
import org.primefaces.event.FileUploadEvent;
import sun.misc.IOUtils;


@ManagedBean
@SessionScoped
public class Data implements Serializable{
    
    private String username, password,password2, tip, ime, prezime, mail, institucija, pol, majica, linkedin,id;
    private ImageStoreBean img;
    private String porukaimage;
   
    private String porukaRegister,porukaLogin,porukaLoginOK;
   
    private Korisnik k=null;
     HibernateUtil helper;
         Session session;

   
         
         

    public Data() {
    }
         

   
         
    public void reset() {
        setPorukaLogin("");
        setPorukaLoginOK("");
        //setPorukaRegister("");
        setPorukaimage("");
    } 

    public String getPorukaimage() {
        return porukaimage;
    }

    public void setPorukaimage(String porukaimage) {
        this.porukaimage = porukaimage;
    }
    
    
    
    public String login() throws SQLException {
         HibernateUtil helper = new HibernateUtil();
         Session session;
         
         session = helper.getSessionFactory().openSession();
         session.beginTransaction();
         
        Query query= session.createQuery("from Korisnik where username=:username");
        query.setParameter("username", username);
         k = (Korisnik) query.uniqueResult();
        
         if (k==null) { setPorukaLogin("Ne postoji korisnik sa unetim korisnickim imenom."); return "index";}
         if (!k.getPassword().equals(password)) { setPorukaLogin("Pogresna lozinka"); return "index";}
         else {
             if (k.getTip() == 2) {
                 FacesContext context = FacesContext.getCurrentInstance();
                  HttpSession Hsession = (HttpSession) context.getExternalContext().getSession(true);
                Hsession.setAttribute("id", k.getId());
                 Hsession.setAttribute("username",k.getUsername());
                 return "korisnik";}
             if (k.getTip()==3) { return "Admin";}
         }
         return "index";
    }
    
    /**
     *
     * @return
     * @throws SQLException
     */
    public String registracija() throws SQLException {
        
        
        if ( password.equals("")|| password2.equals("") || ime.equals("") || prezime.equals("") || mail.equals("")
                || institucija.equals("") ||  linkedin.equals(""))
        {
            setPorukaRegister("Niste uneli sva obavezna polja.");
           
            return "register";
        }
        
         Connection con = DB.getInstance().getConnection();
         Statement stmt=con.createStatement();
         String upit ="select * from user where username='"+username+"';";
         ResultSet rs=stmt.executeQuery(upit);
         
         if(rs.next()) {
            setPorukaRegister("Uneseno korisnicko ime je zauzeto. Probajte da izaberete drugo.");
            
            return "register";
         }
            DB.getInstance().putConnection(con);
            stmt.close();
      
      
            
            
       
       
       if (!password.equals(password2)) {
           setPorukaRegister("Unesene lozinke se ne podudaraju.");
            
            return "register";
           
       }
        
         HibernateUtil helper = new HibernateUtil();
         Session session;
         
          session = helper.getSessionFactory().openSession();
       session.beginTransaction();
        
        
       
        k = new Korisnik();
        k.setUsername(username);
        k.setPassword(password);
        k.setTip(2);
        k.setIme(ime);
        k.setPrezime(prezime);
        k.setEmail(mail);
        k.setInstitucija(institucija);
        k.setPol(pol);
        k.setMajica(majica);
        k.setLinkedin(linkedin);
        //k.setSlika(slika);
   
        session = helper.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(k);
        session.getTransaction().commit();
        
        session.close();
        
        Connection con2= DB.getInstance().getConnection();
        
        setPorukaLoginOK("Uspesno ste se registrovali.");
        return "index";
        
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

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Korisnik getK() {
        return k;
    }

    public void setK(Korisnik k) {
        this.k = k;
    }

    public HibernateUtil getHelper() {
        return helper;
    }

    public void setHelper(HibernateUtil helper) {
        this.helper = helper;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPorukaRegister() {
        return porukaRegister;
    }

    public void setPorukaRegister(String porukaRegister) {
        this.porukaRegister = porukaRegister;
    }

    public String getPorukaLogin() {
        return porukaLogin;
    }

    public void setPorukaLogin(String porukaLogin) {
        this.porukaLogin = porukaLogin;
    }

    public String getPorukaLoginOK() {
        return porukaLoginOK;
    }

    public void setPorukaLoginOK(String porukaLoginOK) {
        this.porukaLoginOK = porukaLoginOK;
    }

    
    
    
    
}
