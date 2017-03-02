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
import javax.swing.ImageIcon;

public class  ObjectArcade {
	
    // Attributes
    int x, y, p;
    double centreX, centreP;
    double ecart;
    double h, l;
    double angle = Math.PI/2;
    double direction;
    double directionX; // = direction*Math.cos(theta)
    double directionY; // = direction*Math.sin(theta)
    double directionP; // = directionY*Math.cos(angle)
    double vitesse;
    double vitessemax;
    Image image;
    Rectangle BoxObjectRectangle;
    boolean actif;
    ImageIcon Image_icon;
    boolean touched; 
    
    public ObjectArcade(String NomImage, int posx, int posy, double av, double dir)    {
        
        ecart = (l/2)*Math.cos(Math.PI/4);
        x=posx;
        y=posy;			
        Toolkit T=Toolkit.getDefaultToolkit();
        image=T.getImage(NomImage);
        Image_icon = new ImageIcon(NomImage);
        h = Image_icon.getIconWidth();
        l= Image_icon.getIconHeight();
	
        p =(int) ((y+h)*Math.cos(Math.PI/4));
        direction = dir; //direction : 0 <= angle <= 2*PI
        centreX = x+h/2; //center X coordinates
        centreP = p+h/2; //center P coordinates
        BoxObjectRectangle = new Rectangle(x, y, (int) h,(int) l);
        vitesse=av;
        vitessemax = 0;
        actif=true;
        touched=false;
        
    }
    
    boolean Collision(ObjectArcade O) {
		
        return (this.BoxObjectRectangle.intersects(O.BoxObjectRectangle) && (this.p >= O.p-ecart || this.p <= O.p+ecart));
        
    }
    
    //Change direction of hit quill by moving quill : movingquill.intersectionQuill(hit quill)
    public void intersectionQuill(ObjectArcade O){
		
    	double dirinit = this.direction;
    	double vitinit = this.vitesse;
    	
    	//direction of hit quill
    	O.directionX = O.centreX - this.centreX;
    	O.directionP = O.centreP - this.centreP;
    	O.directionY =  (O.directionP/(Math.sqrt(2)/2));
    	O.direction =  Math.atan(O.directionX*33/O.directionY);
    	
    	//direction of moving quill
    	this.direction =  (2*dirinit + O.direction);
    	
    	//speed of both quills
    	this.vitesse =  (vitinit*(this.direction-dirinit)/(O.direction+this.direction));
    	O.vitesse =  (vitinit*(O.direction+dirinit)/(O.direction+this.direction));
    	
    }
    
  //Change direction of hit quill by ball : ball.intersectionBall(quill)
    public void intersectionBall(ObjectArcade O){
		
    	double dirtheo;
    	//direction of hit quill
    	O.directionX = O.centreX - this.centreX;
    	O.directionP = O.centreP - this.centreP;
    	O.directionY = ((O.directionP)/(Math.sqrt(2)/2));
    	O.direction =  Math.PI/2 + Math.atan(O.directionX*30/O.directionY);
    	
    	//theoretical direction of ball if it changed : for calculation only
    	dirtheo = 2*this.direction + O.direction;
    	
    	//speed of hit quill
    	O.vitesse =  (this.vitesse*(O.direction+this.direction)/(O.direction+dirtheo));
    	O.vitessemax = O.vitesse;
    	
    } 
    
    boolean Tombe(){
		
    	if(this.vitessemax > 0){
    		return true;
    	}
    	else {
    		return false;
    	}
    }
   
    public void move() {
		
        x-=(int)(vitesse*Math.cos(direction)); 
        y-=(int)(vitesse*Math.sin(direction));
        	 
        centreX = x+h/2;
        centreP = p+h/2;
        
        BoxObjectRectangle.setLocation(x,y);
        BoxObjectRectangle.setSize((int)l,(int)h);
        
        if((y<150) && (touched==true)) actif=false;
        if(x<50 || x > 2000) actif =false;
        
    }
    
}
