package util;

import beans.Korisnik;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;

import org.primefaces.model.UploadedFile;

@ManagedBean
@RequestScoped
public class ImageStoreBean {

	private UploadedFile file;
        private String password, password2, ime, prezime, mail, institucija, linkedin,username,pol,majica;
        private String porukaRegister,porukaLoginOK;
        private Korisnik k;
        
           public String registracija() throws SQLException {
        
        
        if ( password.equals("")|| password2.equals("") || ime.equals("") || prezime.equals("") || mail.equals("")
                || institucija.equals("") ||  linkedin.equals("")||  username.equals(""))
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
        
        storeImage(k.getUsername());
        setPorukaLoginOK("Uspesno ste se registrovali!");
       
        
      
        return "index";
        
    } 
        
        
        
        
        
        
        
	
	// Store file in the database
	public void storeImage(String username) {
		// Create connection
		try {
			// Load driver
			Class.forName("com.mysql.jdbc.Driver");
			// Connect to the database
			Connection connection = DB.getInstance().getConnection();
			// Set autocommit to false to manage it by hand
			connection.setAutoCommit(false);
			
			// Create the statement object1';");
			PreparedStatement statement = connection.prepareStatement("UPDATE user SET slika = ? "
				                  + " WHERE username = ?");
			// Set file data
			statement.setBinaryStream(1, file.getInputstream());
                        statement.setString(2, username);
			String lala = statement.toString();
			// Insert data to the database
			statement.executeUpdate();
			
			// Commit & close
			connection.commit();	// when autocommit=false
			connection.close();
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Upload success", file.getFileName() + " is uploaded.");  
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// Add error message
			FacesMessage errorMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upload error", e.getMessage());  
	        FacesContext.getCurrentInstance().addMessage(null, errorMsg);
		}
		
	}

	// Getter method
	public UploadedFile getFile() {
		return file;
	}

	// Setter method
	public void setFile(UploadedFile file) {
		this.file = file;
	}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
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

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPorukaRegister() {
        return porukaRegister;
    }

    public void setPorukaRegister(String porukaRegister) {
        this.porukaRegister = porukaRegister;
    }

    public Korisnik getK() {
        return k;
    }

    public void setK(Korisnik k) {
        this.k = k;
    }

    public String getPorukaLoginOK() {
        return porukaLoginOK;
    }

    public void setPorukaLoginOK(String porukaLoginOK) {
        this.porukaLoginOK = porukaLoginOK;
    }
        
        
	
}
