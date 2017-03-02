import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.text.Position;
 
public class Objet{
	int x,y;  
	int h,l;
	Image image;
	float vitesse;
	float direction;
	boolean actif;
	
	public Objet (String cheminImage, int pos_x, int pos_y, float vit, float dir){
		
		actif=true;
		x=pos_x;
		y=pos_y;
		vitesse=vit;
		direction=dir;
		Toolkit T=Toolkit.getDefaultToolkit();
		image=T.getImage(cheminImage);
		h= image.getHeight(null);   
        l= image.getWidth(null);
	}
	
	public void move (){
		x-=(int)(vitesse*Math.cos(direction)); 
        y-=(int)(vitesse*Math.sin(direction));
        	 
        if(y<350) actif=false;
	}
	
	
	
	
}
