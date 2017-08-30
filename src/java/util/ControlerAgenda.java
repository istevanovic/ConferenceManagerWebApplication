package util;


import beans.Event;
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
import java.util.HashMap;
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
import org.primefaces.model.UploadedFile;

@SessionScoped
@ManagedBean
public class ControlerAgenda implements Serializable {
    private String imeKonf;
    private boolean uploadForm;
      private List<Event> eventovi;
      private HashMap predaje;
      private UploadedFile File;
      private String porukaUpload;
      private String porukaAgenda;
      private int eventid;
      private HashMap fileName;
      private HashMap Files;
      
      
    
    public void onLoad() throws SQLException {
        Files = new HashMap();
        fileName = new HashMap();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Integer userid  = (Integer) session.getAttribute("id");
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit = "select * from presentations";
        ResultSet rs = stmt.executeQuery(upit);
        while (rs.next()) 
        {
            
            for (int i=0;i<eventovi.size();i++)
            {
                if((rs.getString("idevent")).equals(eventovi.get(i).getId()))
                {
                    Files.put(eventovi.get(i).getId(), rs.getBlob("file"));
                    fileName.put(eventovi.get(i).getId(), eventovi.get(i).getNaziv());
                }
            }
        }
    }
      
    public void uploadtodb(int id) throws SQLException, IOException  {
        if(File.getSize()==0) {
            setPorukaUpload("Izaberite fajl.");
            return;
        }
        Integer idevent = id;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
       Integer userid  = (Integer) session.getAttribute("id");
       Connection con = DB.getInstance().getConnection();
       Statement stmt = con.createStatement();
       String upit = "select * from presentations where iduser='"+userid+"'and idevent='"+id+"';";
       ResultSet rs = stmt.executeQuery(upit); boolean b = false;
       if (rs.next()) {
          PreparedStatement statement = con.prepareStatement("UPDATE presentations SET file = ? "
				                  + " WHERE iduser = ? and idevent = ?");
           statement.setBinaryStream(1, File.getInputstream());
           statement.setInt(2, userid); statement.setInt(3, id);
           statement.executeUpdate();
           setPorukaUpload("Prethodni fajl za ovaj događaj je poništen. Novi fajl je uspešno učitan.");
           DB.getInstance().putConnection(con);
           statement.close();
           return;
       }
            PreparedStatement statement = con.prepareStatement("insert into presentations (iduser,idevent,file) values (?,?,?)");
           
           statement.setString(1, userid.toString());
           statement.setString(2, idevent.toString());
           statement.setBinaryStream(3, File.getInputstream());
           statement.executeUpdate();
           setPorukaUpload(" Novi fajl je uspešno učitan.");
           DB.getInstance().putConnection(con);
           statement.close();
           return;
        
        
        
    }  
      
    public void uploadFile(int id) {
        setUploadForm(true);
        eventid=id;
    }
    
    public void odustani() {
        setUploadForm(false);
    }
    
    
    public String agenda(int id) throws SQLException {
        setPorukaAgenda("");
        eventovi = new ArrayList<>();
        predaje=new HashMap();
        
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
       Integer userid  = (Integer) session.getAttribute("id");
        Connection con = DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit = "select naziv from konferencija where id='"+id+"';";
        ResultSet rs = stmt.executeQuery(upit);
        if (rs.next()) {
            imeKonf = rs.getString("naziv");
        }
        
        upit="select * from event where idkonf='"+id+"';";
        rs=stmt.executeQuery(upit); boolean imadogadjaja=false;
        Statement stmt2 = con.createStatement();
        while (rs.next()) {
            imadogadjaja=true;
            String upit2="select * from linkuserevent where user='"+userid+"' and event='"+rs.getString("id")+"';";
            ResultSet rs2 = stmt2.executeQuery(upit2);
            if(rs2.next()) {
              
                Event e = new Event();
               e.setId(rs.getInt("id"));
                 if (rs2.getString("predaje").equals("1")) {
                    predaje.put(e.getId(), true);
                }
                else predaje.put(e.getId(), false);
            
            e.setTip(rs.getString("tip"));
            e.setIdKonf(rs.getInt("idkonf"));
            e.setIdSesija(rs.getInt("idsesija"));
            e.setVremePocetak(rs.getString("vremepocetak"));
            e.setVremeKraj(rs.getString("vremekraj"));
            e.setDatumPocetak(rs.getString("datumpocetak"));
            e.setSala(rs.getString("sala"));
            e.setNaziv(rs.getString("naziv"));
            eventovi.add(e);
            }
            if (imadogadjaja==false) setPorukaAgenda("Još uvek nemate ni jedan događaj za ovu konferenciju u vašoj agendi.");
           
        }
         stmt.close();stmt2.close();DB.getInstance().putConnection(con);
         return "agenda";
        
        
        
    }

    public String getImeKonf() {
        return imeKonf;
    }

    public void setImeKonf(String imeKonf) {
        this.imeKonf = imeKonf;
    }

    public List<Event> getEventovi() {
        return eventovi;
    }

    public void setEventovi(List<Event> eventovi) {
        this.eventovi = eventovi;
    }

    public HashMap getPredaje() {
        return predaje;
    }

    public void setPredaje(HashMap predaje) {
        this.predaje = predaje;
    }

    public boolean isUploadForm() {
        return uploadForm;
    }

    public void setUploadForm(boolean uploadForm) {
        this.uploadForm = uploadForm;
    }

    public UploadedFile getFile() {
        return File;
    }

    public void setFile(UploadedFile uploadFile) {
        this.File = uploadFile;
    }

    public String getPorukaUpload() {
        return porukaUpload;
    }

    public void setPorukaUpload(String porukaUpload) {
        this.porukaUpload = porukaUpload;
    }

    public int getEventid() {
        return eventid;
    }

    public void setEventid(int eventid) {
        this.eventid = eventid;
    }

    public HashMap getFileName() {
        return fileName;
    }

    public void setFileName(HashMap fileName) {
        this.fileName = fileName;
    }

    public HashMap getFiles() {
        return Files;
    }

    public void setFiles(HashMap Files) {
        this.Files = Files;
    }

    public String getPorukaAgenda() {
        return porukaAgenda;
    }

    public void setPorukaAgenda(String porukaAgenda) {
        this.porukaAgenda = porukaAgenda;
    }
    
    
    
    
    
}