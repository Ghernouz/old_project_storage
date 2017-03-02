
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.image.*; 
import java.awt.event.*;
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
public class Arcade extends JFrame implements ActionListener, KeyListener, MouseListener{
	
	int statutEntree = 0;
	boolean toucheGauche;
	boolean toucheDroite;
	boolean toucheEntree;
	boolean toucheEspace;
	boolean toucheEchap;
	boolean sourisClique;
	boolean winVisible;
	boolean loseVisible;
	int TempsTimer_ms = 50;
	Timer Montimer;
	long Temps;
	ObjectArcade[] Quille;
	ObjectArcade Boule;
	ObjectArcade Boule_init;
	float correctif_droite;
	Image Quille_image;
	int nbRangeesQuille=10;
	int nbQuille = (nbRangeesQuille*(nbRangeesQuille+1))/2;
	int ecartQuille_x=35;
	int ecartQuille_y=15;
	int xSouris;
	int ySouris;
	int a = 0;
	BufferedImage Boule_statique;
	Image Fond;
	Rectangle Ecran;
	BufferedImage ArrierePlan;
	Graphics buffer;
	ImageIcon ImageFond;
	ImageIcon Win;
	ImageIcon Lose;
	ImageIcon ExplicationImage;
	JFrame framelose;
	JFrame framewon;
	
	public Arcade () {
		Toolkit T=Toolkit.getDefaultToolkit();
		Fond = T.getImage("./Images/bloggif_569a7084548c5.jpeg");
		ImageFond = new ImageIcon("./Images/fondpiste.png");
		Lose = new ImageIcon("./Images/en-route-vers-l-etoile-noire.jpg");
		Win= new ImageIcon("./Images/2317288_orig2.jpg");
		ExplicationImage = new ImageIcon("./Images/Yodapush.png");
		this.setTitle("Space Bowling Disco Mania");
		this.setLayout(null);
		this.setSize(ImageFond.getIconWidth(),ImageFond.getIconHeight());
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addMouseListener(this);
		framelose= new JFrame("You lose");
		framelose.setSize(Lose.getIconWidth(),Lose.getIconHeight());
		framelose.setResizable(false);
		framelose.setLocation(500,200);
		framelose.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		JLabel labellose = new JLabel(Lose);
		framelose.add(labellose);
		framelose.setVisible(false);
		framewon = new JFrame("You Win");
		framewon.setSize(Win.getIconWidth(),Win.getIconHeight());
		framewon.setResizable(false);
		framewon.setLocation(500,200);
		framewon.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		JLabel labelwin = new JLabel(Win);
		framewon.add(labelwin);
		framewon.setVisible(false);
		Ecran=new Rectangle(0,0,getSize().width,getSize().height);
		
		
		Quille_image = T.getImage("./Images/quille_temp.jpg");
		try {
             Boule_statique = ImageIO.read(new File("./Images/boule.gif"));
             } 
         catch(Exception err) 
             {		
            System.out.println(" introuvable !");            
            System.exit(0);    
            }
		
		ArrierePlan=new BufferedImage(Ecran.width,Ecran.height,BufferedImage.TYPE_INT_RGB);
		buffer = ArrierePlan.getGraphics();
		//correctif_droite=(float)(Math.atan2(5024,41085));
		Boule = new ObjectArcade ("./Images/boule.gif", (int)(Ecran.width/2-Boule_statique.getWidth(null)/2), (int)(Ecran.height-150), 0, ((float)(Math.PI/2)/*+correctif_droite*/));
		 Boule.touched=true;
		Quille=new ObjectArcade[nbQuille];
		for (int i=0;i<nbRangeesQuille;i++){
			for(int j=(i*(i+1))/2 ; j<((i+1)*(i+2))/2 ; j++){
				int x_temp = (int)((Ecran.width/2-25 + ( 0.5*(-i*(i+2)+1) + j ) * ecartQuille_x)) ;
				int y_temp = (Ecran.height/2 - i*ecartQuille_y - 85);
				Quille[j]=new ObjectArcade("./Images/quille_test.png", x_temp, y_temp,0.0, 0.0);
			}
		}
		
		this.addKeyListener(this);
		Montimer = new Timer(TempsTimer_ms,this);
		Montimer.start();
		this.setVisible(true);
		JFrame Explication= new JFrame();
			Explication.setSize(Lose.getIconWidth(),Lose.getIconHeight());
			Explication.setResizable(false);
			Explication.setLocation(500,200);
			Explication.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			JLabel expli = new JLabel(ExplicationImage);
			Explication.add(expli);
			Explication.setVisible(true);
		
	}
	
	public void paint(Graphics g){
		
		buffer.drawImage(Fond,0,0,this);
		if(Boule.actif){

				buffer.drawImage(Boule.image,Boule.x,Boule.y,this);
			
		}
		for(int i=Quille.length-1;i>=0;i--){
			if(Quille[i].actif){
				buffer.drawImage(Quille[i].image,Quille[i].x,Quille[i].y,this);
			}
		}
		g.drawImage(ArrierePlan,0,0,this);
		//g.fillRect(Boule.x, Boule.y, (int)Boule.h, (int)Boule.l);
		//g.fillRect(Quille[0].x, Quille[0].y, (int)Quille[0].h, (int)Quille[0].l);
	}
	
	public void actionPerformed(ActionEvent e){
		
		setTitle("Space Bowling Disco Mania"+ Temps/10);
		
		if(framelose.isVisible() == false && framewon.isVisible()== false){
		gestionBoule();
		if(a!=0)gestionQuille();
		}
		gestionPartie();
		repaint();
		Temps++;
		
	}
	
	public void gestionQuille(){
		for(int i = 0; i<nbRangeesQuille*(nbRangeesQuille+1)/2; i++){

			if(Quille[i].touched==false ) (Quille[i].y)+=2;
			if((Quille[i].vitesse==0) /*&&(Boule.vitesse!=0)*/){
				if(Boule.Collision(Quille[i])) {
					Boule.intersectionBall(Quille[i]);
					Quille[i].touched=true;
				}
			}
			for(int k=0;k<(nbRangeesQuille*(nbRangeesQuille+1))/2; k++){
				if(i!=k){
				//if(Quille[k].Collision(Quille[i])) Quille[k].intersectionQuill(Quille[i]);
				}
			}
			int newsizex = 50*(Quille[i].x);     
			int newsizey = 50*(Quille[i].y);
			//Quille[i].image = resize(Quille[i].image,newsizex,newsizey);
			Quille[i].move();
		}
			
		
			/*System.out.println("DIRECTION X: "+Quille[0].directionX+" DIRECTION Y: "+Quille[0].directionY+" DIRECTION P: "+Quille[0].directionP);
			System.out.println("QUILECENTRE X: "+Quille[0].centreX+" CENTRE P: "+Quille[0].centreP);
			System.out.println("BOULECENTRE X: "+Boule.centreX+" CENTRE P: "+Boule.centreP);
			System.out.println(Quille[0].direction+"||||||"+Quille[0].vitesse);
			System.out.println(Quille[0].directionX/Quille[0].directionY);*/

	}
	
	public void gestionBoule(){
		//if(Boule.actif){
			Boule.move();
			//Boule.move();
			int newSize = 50*Boule.y/500;     
			//Boule.x+=1;	
			Boule.image = resize(Boule.image,newSize,newSize);
			//System.out.println(toucheEspace);	
			if(statutEntree==0){
				if(toucheDroite) Boule.x+=10;
				if(toucheGauche) Boule.x-=10;
			}
			if(sourisClique){
				float angle =(float) ((Math.atan2((Math.abs((Boule.x+Boule.h/2)-xSouris)),Math.abs(((Boule.y+Boule.h/2)-ySouris))))/*+Math.PI*/);
				if(xSouris<Boule.x+Boule.h/2) Boule.direction=(float)((Math.PI/2)-angle);
				else Boule.direction=(float)((Math.PI/2)+angle);
				Boule.vitesse=40;
				a+=1;
			}
			
			if(!Boule.actif){
				/*Boule.y=Ecran.height;
				Boule.vitesse=0;*/
				Boule=new ObjectArcade ("./Images/boule.gif", (int)(Ecran.width/2-Boule_statique.getWidth(null)/2), (int)(Ecran.height-150), 0, ((float)(Math.PI/2)));
				Boule.touched=true;
			}
						
		//}
	}
	
	public void gestionPartie() {
	int Q=0;
	int b = 0;
		for(int i = 0; i < (nbRangeesQuille*(nbRangeesQuille+1))/2; i++){
			if(Quille[i].actif==false)Q ++ ;
			if(Quille[i].y>Ecran.height-40) {
		
			framelose= new JFrame("You lose");
			framelose.setSize(Lose.getIconWidth(),Lose.getIconHeight());
			framelose.setResizable(false);
			framelose.setLocation(500,200);
			framelose.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			JLabel labellose = new JLabel(Lose);
			framelose.add(labellose);
			framelose.setVisible(true);
			nbRangeesQuille=10; 
			resetQuille();
			 b = 1;
		}
		}
		if (((nbRangeesQuille*(nbRangeesQuille+1))/2 - Q <= 0)&&(b!=1)){
		
			
			framewon = new JFrame("You Win");
			framewon.setSize(Win.getIconWidth(),Win.getIconHeight());
			framewon.setResizable(false);
			framewon.setLocation(500,200);
			framewon.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			JLabel labelwin = new JLabel(Win);
			framewon.add(labelwin);
			framewon.setVisible(true);
			nbRangeesQuille+=4; 
			resetQuille();
			
			
		}
	 
	
	}
	
	public void resetQuille(){
		//System.out.println(nbRangeesQuille);
		Quille=new ObjectArcade[(nbRangeesQuille*(1+nbRangeesQuille))/2];
		for (int i=0;i<nbRangeesQuille;i++){
			for(int j=(i*(i+1))/2 ; j<((i+1)*(i+2))/2 ; j++){
				int x_temp = (int)((Ecran.width/2-25 + ( 0.5*(-i*(i+2)+1) + j ) * ecartQuille_x)) ;
				int y_temp = (Ecran.height/2 - i*ecartQuille_y - 85);
				Quille[j]=new ObjectArcade("./Images/quille_test.png", x_temp, y_temp,0.0, 0.0);
			}
		}
	}
		
	
	
	public Image resize(Image originalImage, int newWidth, int newHeight){
		int type=BufferedImage.TYPE_INT_ARGB;
		BufferedImage resizedImage = new BufferedImage(newWidth,newHeight,type);
		Boule.h = newHeight;
		Boule.l = newWidth;
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(originalImage,0,0,newWidth,newHeight,this);
		g.dispose();
		return resizedImage;
	}
		
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_SPACE)toucheEspace=true;
		if(e.getKeyCode()==KeyEvent.VK_LEFT)toucheGauche=true;
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)toucheDroite=true;
		if(e.getKeyCode()==KeyEvent.VK_ENTER)toucheEntree=true;
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE)toucheEchap=true;
		if(e.getKeyCode()==KeyEvent.VK_P){
			if(Montimer.isRunning()) Montimer.stop();
			else Montimer.start();
		}
	}
			
	public void keyTyped(KeyEvent E){}
	
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_SPACE)toucheEspace=false;
		if(e.getKeyCode()==KeyEvent.VK_LEFT)toucheGauche=false;
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)toucheDroite=false;
		if(e.getKeyCode()==KeyEvent.VK_ENTER)toucheEntree=false;
		if(e.getKeyCode()==KeyEvent.VK_ESCAPE)toucheEchap=false;
	}
	
	public void mousePressed(MouseEvent e) {
       xSouris=e.getX();
       ySouris=e.getY();
       sourisClique=true;
    }
	
	public void mouseReleased(MouseEvent e) {
       sourisClique=false;
    }
    
	public void mouseClicked(MouseEvent e) {
    }
	
	public void mouseEntered(MouseEvent e) {
    }
	
	public void mouseExited(MouseEvent e) {
    }
	
	public static void main (String[] args){
		
		Arcade monBowling = new Arcade() ;
		
	}

}
