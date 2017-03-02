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

public class  Object {
	
    // Attributes
    int x, y, coorRectY;
    int tempstomber;
    boolean quille;
    double centreX, centreY;
    double h, l;
    double angle = Math.PI/4;
    double direction;
    double directionX; // = direction*Math.cos(angle)
    double directionY; // = direction*Math.sin(angle)
    double vitesse;
    double vitessemax;
    Image image;
    Rectangle BoxObjectRectangle;
    boolean actif;
    boolean tombe;
    ImageIcon Image_icon;
     
    public Object(String NomImage, int posx, int posy, double av, double dir, boolean b)    {

    	quille = b;
        x=posx;
        y=posy;
        Toolkit T=Toolkit.getDefaultToolkit();
        image=T.getImage(NomImage);
        Image_icon = new ImageIcon(NomImage);
        h = Image_icon.getIconHeight();
        l= Image_icon.getIconWidth();
        coorRectY = (int) (y+(3*h/4));
        direction = dir; //direction : 0 <= angle <= 2*PI
        centreX = x+(l/2); //center X coordinates
        centreY = (int) (coorRectY+(h/8)); //center Y coordinates
        BoxObjectRectangle = new Rectangle(x, coorRectY, (int) l, (int) (h/4));
        vitesse=av;
        vitessemax = 0;
        tempstomber = 0;
        tombe = false;
        actif=true;
        
    }
    
    boolean Collision(Object O) {
		
        return (this.BoxObjectRectangle.intersects(O.BoxObjectRectangle));
        
    }
    
    //Change direction of hit quill by moving quill : movingquill.intersectionQuill(hit quill)
    public void intersectionQuill(Object O){
		
    	double dirinit = this.direction;
    	double vitinit = this.vitesse;
    	
    	//direction of hit quill
    	O.directionX = O.centreX - this.centreX;
    	O.directionY = O.centreY - this.centreY;
    	O.direction =  Math.atan(O.directionX/O.directionY);
    	
    	//direction of moving quill
    	this.direction =  (2*dirinit - O.direction);
    	
    	//speed of both quills
    	this.vitesse =  (vitinit*(this.direction-dirinit)/(O.direction+this.direction));
    	O.vitesse =  (vitinit*(O.direction+dirinit)/(O.direction+this.direction));
    	if(O.vitesse > O.vitessemax){
    		O.vitessemax = O.vitesse;
    	}
    	
    }
    
  //Change direction of hit quill by ball : ball.intersectionBall(quill)
    public void intersectionBall(Object O){
		
    	double dirtheo;
    	//direction of hit quill
    	O.directionX = O.centreX - this.centreX;
    	O.directionY = O.centreY - this.centreY;
    	O.direction =  Math.atan(O.directionX/O.directionY)+Math.PI/2;
    	
    	//theoretical direction of ball if it changed : for calculation only
    	dirtheo = 2*this.direction - O.direction;
    	
    	//speed of hit quill
    	O.vitesse =  (this.vitesse*(O.direction+this.direction)/(O.direction+dirtheo));
    	if(O.vitesse > O.vitessemax){
    		O.vitessemax = O.vitesse;
    	}
    	
    } 
   
    public void move() {

        x-=(int)(vitesse*Math.cos(direction)); 
        y-=(int)(vitesse*Math.sin(direction));
        
        if(quille && vitesse != 0){
        	vitesse -= 0.3;
        }
        if(quille && vitesse < 0.3){
    		vitesse = 0;
    	}
        
        coorRectY = (int) (y+(3*h/4));
        centreX = x+(l/2);
        centreY = (int) (coorRectY+(h/8));
        
        BoxObjectRectangle.setLocation(x,coorRectY);
        BoxObjectRectangle.setSize((int)l,(int) (h/4));
        
        //Out of bounds
        if(centreY < (int) (-2.23*centreX + 2057) || centreY < (int) (2.26*centreX - 1548)){
        	if(quille && vitesse == 0){
        		tombe = true; //Quill
        	} else {
        		actif = false; // Ball
        	}
        }
        if(quille && vitesse == 0 && centreY < 400){
        	tombe = true;
        }
        if(quille && vitesse == 0 && vitessemax > 6){
        	tombe = true;
        }
    }
    
}
