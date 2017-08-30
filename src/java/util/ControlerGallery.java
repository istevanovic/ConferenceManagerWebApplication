package util;


import beans.Image;
import util.Data;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
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
public class ControlerGallery implements Serializable {
    private String controlerGalleryName;
    private boolean loadCanvas=false;
    private List<Image> images;
    private byte[] trenutnaslika;
    private List<Image> imagesforday;
    private String porukaGallery;
    private List<String> dani;
    private int dan;
    private int currentimage;
    
    
   public String changePictureLeft() 
   {
       if (currentimage==0)
       {
           currentimage=imagesforday.size()-1;
        
       }
       else currentimage=currentimage-1;
          trenutnaslika=imagesforday.get(currentimage).getImage();
       return "galerija";
   }
   
    public String changePictureRight() 
   {
       if (currentimage==imagesforday.size()-1)
       {
           currentimage=0;
        
       }
       else currentimage=currentimage+1;
          trenutnaslika=imagesforday.get(currentimage).getImage();
       return "galerija";
   }
    
    
    public String changeDay(int day) 
    {
        imagesforday=new ArrayList<>();
        for (int i=0;i<images.size();i++) 
        {
            if (images.get(i).getDan()==day) imagesforday.add(images.get(i));
            
            
        }
        if (imagesforday.size()==0)
        {
            setPorukaGallery("Nema slika za ovaj dan.");
            setLoadCanvas(false);
        }
        return "galerija";
    }
    
    public String loadGallery(int idKonf) throws SQLException {
    images=new ArrayList<>();
    currentimage=0;
    imagesforday=new ArrayList<>();
    dani=new ArrayList<>();
   Connection con = DB.getInstance().getConnection();
    Statement stmt = con.createStatement();
    String upit="select naziv from konferencija where id='"+idKonf+"';";
    ResultSet rs = stmt.executeQuery(upit);
    setLoadCanvas(false);
    if (rs.next()) 
    {   
       
        setcontrolerGalleryName(rs.getString("naziv"));
        
    }
        
        
        upit="select * from galerija where idkonf='"+idKonf+"';";
        rs=stmt.executeQuery(upit); dan=0;
        while(rs.next()) {
             if(dan!=rs.getInt("dan")) 
             {
                 dan=rs.getInt("dan");
                 dani.add("Dan "+dan);
             }
             setLoadCanvas(true);
             Image I = new Image();
             I.setDan(rs.getInt("dan"));
             I.setImage(rs.getBytes("slika"));
             I.setIdKonf((rs.getInt("idkonf")));
             images.add(I);
             if (I.getDan()== 1) imagesforday.add(I);
             
             
        }
        setTrenutnaslika(imagesforday.get(0).getImage());
        DB.getInstance().putConnection(con);
        stmt.close();
        return "galerija";
    }

    public boolean isLoadCanvas() {
        return loadCanvas;
    }

    public void setLoadCanvas(boolean loadCanvas) {
        this.loadCanvas = loadCanvas;
    }
    
    

    public String getcontrolerGalleryName() {
        return controlerGalleryName;
    }

    public void setcontrolerGalleryName(String icontrolerGalleryName) {
        this.controlerGalleryName = icontrolerGalleryName;
    }

    public String getControlerGalleryName() {
        return controlerGalleryName;
    }

    public void setControlerGalleryName(String controlerGalleryName) {
        this.controlerGalleryName = controlerGalleryName;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public byte[] getTrenutnaslika() {
        return trenutnaslika;
    }

    public void setTrenutnaslika(byte[] trenutnaslika) {
        this.trenutnaslika = trenutnaslika;
    }

    public List<Image> getImagesforday() {
        return imagesforday;
    }

    public void setImagesforday(List<Image> imagesforday) {
        this.imagesforday = imagesforday;
    }

    public String getPorukaGallery() {
        return porukaGallery;
    }

    public void setPorukaGallery(String porukaGallery) {
        this.porukaGallery = porukaGallery;
    }

    public List<String> getDani() {
        return dani;
    }

    public void setDani(List<String> dani) {
        this.dani = dani;
    }

    public int getDan() {
        return dan;
    }

    public void setDan(int dan) {
        this.dan = dan;
    }
    
    
    
    
 
}