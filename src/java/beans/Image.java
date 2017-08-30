/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.mysql.jdbc.Blob;

/**
 *
 * @author Ilija
 */
public class Image {
    
    private byte[] image;
    private int idKonf;
    private int dan;

    public Image() {
    }
    
    

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getIdKonf() {
        return idKonf;
    }

    public void setIdKonf(int idKonf) {
        this.idKonf = idKonf;
    }

    public int getDan() {
        return dan;
    }

    public void setDan(int dan) {
        this.dan = dan;
    }
    
    
    
}
