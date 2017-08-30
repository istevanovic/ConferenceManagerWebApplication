package util;


import beans.Event;
import util.Data;
import beans.Konferencija;
import beans.Korisnik;
import beans.Sesija;
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
import java.util.Calendar;
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

@SessionScoped
@ManagedBean
public class ControlerProgram implements Serializable {
        
    private String nazivKonf;
    private int idKonf;
    private String porukaAgenda,porukaLike;
    private List<Sesija> sesije;
    private List<String> predavaci;
    private String poruka;
    private List<Event> eventovi;
    private boolean render;
    private String ocena;
     HashMap porukaOcena=new  HashMap();
     HashMap porukaPrijava=new  HashMap();
    private Integer renderid;
    
    public void reset() {
        porukaOcena.clear();    
        porukaPrijava.clear();
    }
    
    public boolean renderImage(int id) throws SQLException  {
        return (ocenjiv(id) && (!atenduje(id)));
    }
    
    public boolean renderAttendButton(int id)throws SQLException  {
        return (ocenjiv(id) && (atenduje(id)));
    }
    
    public boolean atenduje(int id) throws SQLException {
        
       FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
       Integer userid  = (Integer) session.getAttribute("id");
        Connection con=DB.getInstance().getConnection();
        Statement stmt=con.createStatement();
        String upit = "select * from linkuserevent where event='"+id+"' and user='"+userid+"';";
        ResultSet rs = stmt.executeQuery(upit);
        if (rs.next()) {
            DB.getInstance().putConnection(con);
            stmt.close();
            return false;
        }
        DB.getInstance().putConnection(con);
            stmt.close();
        return true;
      
  }
    
    public void agendaDodaj(int id) throws SQLException {
        reset();
        for (int i=0; i<eventovi.size();i++)  porukaPrijava.put(eventovi.get(i).getId(), "");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Integer userid  = (Integer) session.getAttribute("id");
        Connection con=DB.getInstance().getConnection();
        Statement stmt = con.createStatement();
        String upit="select * from event where id='"+id+"';";
        ResultSet rs = stmt.executeQuery(upit);
        if (rs.next()) {
            if (rs.getDate("vremepocetak").before(Calendar.getInstance().getTime()))
            {
                porukaPrijava.put(id, "Ne mozete se prijaviti na ovaj događaj.");
                DB.getInstance().putConnection(con);
                stmt.close();
                return;
            }
            
            upit="insert into linkuserevent(user,event) values ('"+id+"','"+userid+"');";
            stmt.executeUpdate(upit);
            DB.getInstance().putConnection(con);
            stmt.close();
            return;
        }
        
    }
    
    
    
   
    
    public String oceni(int id) throws SQLException {
        reset();
        for (int i=0; i<eventovi.size();i++)  porukaOcena.put(eventovi.get(i).getId(), "");
        if(ocena.equals(("-"))) {
              porukaOcena.put(id, "Unesite ocenu.");
              return "programkonf";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        Integer userid  = (Integer) session.getAttribute("id");
        Connection con=DB.getInstance().getConnection();
        Statement stmt=con.createStatement();
        String upit="select * from linkuserevent where idevent='"+id+"' and iduser='"+userid+"';";
        ResultSet rs = stmt.executeQuery(upit);
        if (rs.next()) 
        {
            upit = "select * from event where id='"+id+"';";
            rs = stmt.executeQuery(upit);
            if (rs.next())
            {
                if (rs.getDate("vremekraj").after(Calendar.getInstance().getTime()))
                {
                    porukaOcena.put(id, "Događaj nije završen i ne može se još oceniti");
                    DB.getInstance().putConnection(con);
                    stmt.close();
                    return "programkonf";
                }
                else 
                {
                  upit="select * from ocene where iduser='"+userid+"' and idevent='"+id+"';";
                  rs = stmt.executeQuery(upit);
                    if (rs.next())
                  {
                     upit="update ocene set ocena='"+ocena+"' where iduser='"+userid+"';";
                     stmt.executeUpdate(upit);
                     porukaOcena.put(id, "Događaj je ocenjen sa ocenom "+ ocena);
                     DB.getInstance().putConnection(con);
            stmt.close();
            return "programkonf";
                  }
                  else 
                  {
                     upit="insert into ocene (idevent,iduser,ocena) values ('"+id+"','"+userid+"','"+ocena+"');";
                     stmt.executeUpdate(upit);
                     porukaOcena.put(id, "Događaj je ocenjen sa ocenom "+ ocena);
                     DB.getInstance().putConnection(con);
            stmt.close();
            return "programkonf";
                  }
                }
        }
        else {
            porukaOcena.put(id, "Niste prisustvovali ovom dogadjaju.");
           
            DB.getInstance().putConnection(con);
            stmt.close();
            return "programkonf";
        }
        
    }
         porukaOcena.put(id, "Niste prisustvovali ovom dogadjaju.");
           
            DB.getInstance().putConnection(con);
            stmt.close();
            return "programkonf";
        
    }
    
   
    
    
    public String like(int id) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
       Integer userid  = (Integer) session.getAttribute("id");
        Connection con=DB.getInstance().getConnection();
        Statement stmt=con.createStatement();
        String upit = "select * from likes where idevent='"+id+"' and iduser='"+userid+"';";
        ResultSet rs = stmt.executeQuery(upit);
        if (rs.next()) {
            setPorukaLike("Vec ste lajkovali ovaj događaj.");
            DB.getInstance().putConnection(con);
            stmt.close();
            return "programkonf";
        }
        upit="insert into likes (iduser,idevent) values ('"+userid+"','"+id+"');";
        stmt.executeUpdate(upit);
        setPorukaLike("Liked! ");
        DB.getInstance().putConnection(con);
        stmt.close();
        return "programkonf";
    }
    
  public boolean liked(int id) throws SQLException {
       FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
       Integer userid  = (Integer) session.getAttribute("id");
        Connection con=DB.getInstance().getConnection();
        Statement stmt=con.createStatement();
        String upit = "select * from likes where idevent='"+id+"' and iduser='"+userid+"';";
        ResultSet rs = stmt.executeQuery(upit);
        if (rs.next()) {
            DB.getInstance().putConnection(con);
            stmt.close();
            return false;
        }
        DB.getInstance().putConnection(con);
            stmt.close();
        return true;
      
  }
        
    
    public boolean ocenjiv(int id) {
        
        Event e=new Event();
        for (int i=0;i<eventovi.size();i++) 
        {
            if (eventovi.get(i).getId()==id) e=eventovi.get(i);
            
            
        }
        boolean b = e.isOcenjiv();
        return b;
    }
    

    
    
    public void onLoad() throws SQLException {
        setPoruka("");
       // porukaOcena=new ArrayList<>();
        
        sesije = new ArrayList<>();
        eventovi = new ArrayList<>();
      
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        idKonf=(int) session.getAttribute("idKonf");
        Connection con = DB.getInstance().getConnection();
        Statement stmt=con.createStatement();
        String upit = "select * from event where idkonf='"+idKonf+"';";
        ResultSet rs = stmt.executeQuery(upit);boolean imaeventova=false;
        while (rs.next())
        {
            imaeventova=true;
            Event e = new Event();
            e.setId(rs.getInt("id"));
            
            e.setTip(rs.getString("tip"));
            e.setIdKonf(rs.getInt("idkonf"));
            e.setIdSesija(rs.getInt("idsesija"));
            e.setVremePocetak(rs.getString("vremepocetak"));
            e.setVremeKraj(rs.getString("vremekraj"));
            e.setDatumPocetak(rs.getString("datumpocetak"));
            e.setSala(rs.getString("sala"));
            e.setNaziv(rs.getString("naziv"));
            e.setOcenjiv(false);
            if ((e.getTip().equals("Predavanje"))||(e.getTip().equals("Radionica")))
            {
                e.setOcenjiv(true);
                
                Statement stmt2 = con.createStatement();
                String upit2 ="select * from linkpredavacevent where idevent='"+e.getId()+"';";
                ResultSet rs2 = stmt2.executeQuery(upit2); String predavaci="";
                while (rs2.next())
                {
                  String pred1 = rs2.getString("imeprezime");
                  String pred2 = rs2.getString("iduser");
                  if (pred2 != null) {
                      Statement stmt3 = con.createStatement();
                      String upit3 = "select ime,prezime from user where id='"+pred2+"';";
                      ResultSet rs3 = stmt3.executeQuery(upit3);
                      if (rs3.next()) {
                          pred2 = rs3.getString("ime")+" "+ rs3.getString("prezime");
                          
                      }
                      stmt3.close();
                  }
                 
                  if (predavaci.equals("")){
                      if (pred1==null) predavaci=pred2;
                      else predavaci=pred1;
                  }
                  else 
                  {
                  if (pred1 == null) predavaci=predavaci +", "+pred2;
                  else predavaci=predavaci+", "+pred1;
                  }
                
                }
                  
                   e.setPredavaci(predavaci);
                   stmt2.close();
                
            }
            eventovi.add(e);
        }
        
        //upit = "select * from sesija where idkonf='"+idKonf+"';";
        
        
        if(!imaeventova) {
            setPoruka("Jos uvek nije uredjen program konferencije. Pogledajte opet kasnije.");
        }
        
        
        
        
        
        DB.getInstance().putConnection(con);stmt.close();
       
    }

    public Integer getRenderid() {
        return renderid;
    }

    public void setRenderid(Integer renderid) {
        this.renderid = renderid;
    }
    

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String getNazivKonf() {
        return nazivKonf;
    }

    public void setNazivKonf(String nazivKonf) {
        this.nazivKonf = nazivKonf;
    }

    public int getIdKonf() {
        return idKonf;
    }

    public void setIdKonf(int idKonf) {
        this.idKonf = idKonf;
    }

    public List<Sesija> getSesije() {
        return sesije;
    }

    public void setSesije(List<Sesija> sesije) {
        this.sesije = sesije;
    }

    public List<Event> getEventovi() {
        return eventovi;
    }

    public void setEventovi(List<Event> eventovi) {
        this.eventovi = eventovi;
    }

    public String getPorukaAgenda() {
        return porukaAgenda;
    }

    public void setPorukaAgenda(String porukaAgenda) {
        this.porukaAgenda = porukaAgenda;
    }

    public List<String> getPredavaci() {
        return predavaci;
    }

    public void setPredavaci(List<String> predavaci) {
        this.predavaci = predavaci;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public String getOcena() {
        return ocena;
    }

    public void setOcena(String ocena) {
        this.ocena = ocena;
    }

    public String getPorukaLike() {
        return porukaLike;
    }

    public void setPorukaLike(String porukaLike) {
        this.porukaLike = porukaLike;
    }

    public HashMap getPorukaOcena() {
        return porukaOcena;
    }

    public void setPorukaOcena(HashMap porukaOcena) {
        this.porukaOcena = porukaOcena;
    }

    public HashMap getPorukaPrijava() {
        return porukaPrijava;
    }

    public void setPorukaPrijava(HashMap porukaPrijava) {
        this.porukaPrijava = porukaPrijava;
    }

  
    
    
    
 
}