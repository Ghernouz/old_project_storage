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
import javax.swing.JTextArea;

public class Bowling extends JFrame implements ActionListener, KeyListener, MouseListener{
	
	int statutEntree = 0;
	boolean toucheGauche;
	boolean toucheDroite;
	boolean toucheEntree;
	boolean toucheEspace;
	boolean toucheEchap;
	boolean sourisClique;
	boolean strike;
	boolean spare;
	boolean spam = false;
	boolean scoreCompte;
	int nbBoule = 0;
	int TempsTimer_ms = 100;
	Timer Montimer;
	boolean yAtIlDesQuillesQuiTournent=false;
	long Temps;
	long TempsFinPartie;
	long TempsRestartPartie;
	long Delay;
	Object[] Quille;
	Object Boule;
	Object Boule_init;
	float correctif_droite;
	Image Quille_image;
	int nbRangeesQuille=4;
	int nbQuille = (nbRangeesQuille*(nbRangeesQuille+1))/2;
	int ecartQuille_x=35;
	int ecartQuille_y=15;
	int xSouris;
	int ySouris;
	int nbClic;
	int round;
	BufferedImage Boule_statique;
	Image Fond;
	Image QuilleTombe;
	Rectangle Ecran;
	Case[] TableScore;
	Case[] TableTotal;
	BufferedImage ArrierePlan;
	Graphics buffer;
	ImageIcon ImageFond;
	int scoreManche;
	int scoreManchePrecedente;
	boolean timerfin = true;
	boolean finpartie = false;
	
	
	public Bowling () {
		Toolkit T=Toolkit.getDefaultToolkit();
		Fond = T.getImage("./Images/fondpiste.png");
		QuilleTombe = T.getImage("./Images/Rotating_quill.gif");
		ImageFond = new ImageIcon("./Images/fondpiste.png");
		this.setTitle("Space Bowling Disco Mania");
		this.setLayout(null);
		this.setSize(ImageFond.getIconWidth(),ImageFond.getIconHeight());
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addMouseListener(this);
		
		round = 1;
		nbClic = 0;
		
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
		Boule = new Object ("./Images/boule.gif", (int)(Ecran.width/2-Boule_statique.getWidth(null)/2), (int)(Ecran.height-150), 0, ((float)(Math.PI/2)/*+correctif_droite*/), false);
		Quille = new Object[nbQuille];
		for (int i=0;i<nbRangeesQuille;i++){
			for(int j=(i*(i+1))/2 ; j<((i+1)*(i+2))/2 ; j++){
				int x_temp = (int)((Ecran.width/2-25 + ( 0.5*(-i*(i+2)+1) + j ) * ecartQuille_x)) ;
				int y_temp = (Ecran.height/2 - i*ecartQuille_y - 85);
				Quille[j]=new Object("./Images/quille_test.png", x_temp, y_temp,0.0, 0.0, true);
			}
		}
		TableScore = new Case[10];
		for(int i = 0; i < 10; i++){
			TableScore[i] = new Case(" ", i*40+1180, 880, 40);
		}
		TableTotal = new Case[10];
		for(int i = 0; i < 10; i++){
			TableTotal[i] = new Case(" ", i*40+1200, 900, 20);
		}
		this.addKeyListener(this);
		Montimer = new Timer(TempsTimer_ms,this);
		Montimer.start();
		this.setVisible(true);
	}
	
	public void paint(Graphics g){
		
		buffer.drawImage(Fond,0,0,this);
		for(int i=Quille.length-1;i>=0;i--){
			if(Quille[i].actif){
				buffer.drawImage(Quille[i].image,Quille[i].x,Quille[i].y,this);
			}
			if(Quille[i].tempstomber > 0){
				buffer.drawImage(QuilleTombe, Quille[i].x-17, Quille[i].y, this);
			}
		}
		if(nbClic < 2){
			buffer.drawImage(Boule.image,Boule.x,Boule.y,this);
		}
		buffer.setColor(Color.RED);
		for(int i = 0; i < 10; i++){
			buffer.drawRect(TableScore[i].posx, TableScore[i].posy, TableScore[i].cote, TableScore[i].cote);
		}
		for(int i = 0; i < 10; i++){
			buffer.drawRect(TableTotal[i].posx, TableTotal[i].posy, TableTotal[i].cote, TableTotal[i].cote);
		}
		
		/*buffer.setColor(Color.GREEN);
		buffer.fillRect(Boule.x, Boule.coorRectY, (int) Boule.l, (int) (Boule.h/4));
		for(int i = 0; i < nbQuille; i++){
			buffer.fillRect(Quille[i].x, Quille[i].coorRectY, (int) Quille[i].l, (int) (Quille[i].h/4));
		}
		buffer.setColor(Color.red);
		buffer.fillOval((int) Boule.centreX, (int) Boule.centreY, 5, 5);
		for(int i = 0; i < nbQuille ; i++){
			buffer.fillOval((int) Quille[i].centreX, (int) Quille[i].centreY, 5, 5);
		}*/
		g.drawImage(ArrierePlan,0,0,this);
	}
	
	public void actionPerformed(ActionEvent e){
		
		setTitle("Space Bowling Disco Mania --- Round : "+round);
		
		gestionBoule();
		gestionQuille();
		gestionPartie();
		score();
		repaint();
		Temps++;
		
	}
	
	public void gestionQuille(){
		
		for(int i = 0; i<nbQuille; i++){
			if(Quille[i].vitesse < 0.1){
				if(Boule.Collision(Quille[i]) && Quille[i].actif) Boule.intersectionBall(Quille[i]);
			}
			for(int k=0;k<nbQuille; k++){
				if(i!=k && Quille[i].vitesse < 0.1){
					if(Quille[k].Collision(Quille[i]) && Quille[k].actif && Quille[i].actif) Quille[k].intersectionQuill(Quille[i]);
				}
			}
			Quille[i].move();
			if(Quille[i].tombe && Quille[i].actif){
				Quille[i].actif = false;
				Quille[i].tempstomber = 10;
			}
			if(Quille[i].tempstomber > 0){
				Quille[i].tempstomber--;
			}
			
		}
	}
	
	public void gestionBoule(){
			Boule.move();
			int newSize = 50*Boule.y/400;     
			Boule.image = resize(Boule.image,newSize,newSize);
			if(statutEntree==0){
				if(toucheDroite) Boule.x+=10;
				if(toucheGauche) Boule.x-=10;
			}
			if(sourisClique&& spam == false){
				float angle =(float) ((Math.atan2((Math.abs((Boule.x+Boule.h/2)-xSouris)),Math.abs(((Boule.y+Boule.h/2)-ySouris))))/*+Math.PI*/);
				if(xSouris<Boule.x+Boule.h/2) Boule.direction=(float)((Math.PI/2)-angle);
				else Boule.direction=(float)((Math.PI/2)+angle);
				Boule.vitesse=20;
				spam = true;
				nbBoule ++;
			}
			
			if(!Boule.actif){
				Delay=Temps;
				nbClic++;
				Boule=new Object ("./Images/boule.gif", (int)(Ecran.width/2-Boule_statique.getWidth(null)/2), (int)(Ecran.height-150), 0, ((float)(Math.PI/2)), false);
				spam = false;
				
				
			}
	}
	
	public void gestionPartie() {
		int Q=0;
		for(int i = 0; i < nbQuille; i++){
			if(Quille[i].actif==false)Q ++ ;
		}
		if (nbQuille - Q <= 0 && timerfin){
			TempsFinPartie = Temps;
			timerfin = false;
			finpartie = true;
			Boule.actif = false;
		}
		if(nbClic >= 2 && timerfin){
			TempsFinPartie = Temps;
			timerfin = false;
			finpartie = true;
			Boule.actif = false;
		}
		if (nbBoule==1 &&  (TempsFinPartie + 40) <= Temps){
			score();
		}
		if(finpartie && (TempsFinPartie + 40) <= Temps){
			resetQuille();
			round++;
			nbClic = 0;
			
			//strike = false;
			//spare = false;
			timerfin = true;
			finpartie = false;
			Boule.actif = true;
		}
		
		
	}
	
	public void resetQuille(){
		for (int i=0;i<nbRangeesQuille;i++){
			for(int j=(i*(i+1))/2 ; j<((i+1)*(i+2))/2 ; j++){
				int x_temp = (int)((Ecran.width/2-25 + ( 0.5*(-i*(i+2)+1) + j ) * ecartQuille_x)) ;
				int y_temp = (Ecran.height/2 - i*ecartQuille_y - 85);
				Quille[j]=new Object("./Images/quille_test.png", x_temp, y_temp,0.0, 0.0, true);
			}
		

	}
	nbBoule = 0;
}

	
	public void score(){
		System.out.println(nbBoule);
		int Q = 0;
		Delay = Temps;
		for(int i=0;i<nbQuille;i++)if(!Quille[i].actif)Q++;
		scoreManche = Q;
		if (strike == true && nbBoule==3){
			scoreManchePrecedente += Q;
			strike=false;
		}
		if (spare==true && nbBoule==1 && spam == false &&  Temps < Delay + 40){
			scoreManchePrecedente += Q;
			spare=false;
		}
		if (scoreManche==10 && nbBoule ==1 && spam ==false  && Temps < Delay + 40){
			strike=true;
			//nbBoule =0;
			//scoreManchePrecedente = 10;
		}
		if (scoreManche==10 && (nbBoule ==2||nbBoule==3)){
			spare=true;
			//nbBoule =0;
		//	scoreManchePrecedente = 10;
		}
		if ((nbBoule == 2 && spam==false)|| nbBoule==3 && Temps < Delay + 40){
			nbBoule =3;
			scoreManchePrecedente=Q;
		}
		System.out.println(scoreManche +"////"+ scoreManchePrecedente);
		System.out.println("Spare?"+spare);
		System.out.println("Strike?"+strike);
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
		
		if(toucheEntree){
			if (statutEntree != 1) statutEntree ++;
			else statutEntree =0;
			
		}	
		
		if(toucheEchap)//
		
		switch (statutEntree){
			case 0:
			if (toucheGauche); //
			if (toucheDroite); //
			break;
			case 1:
			if (toucheGauche) //
			if (toucheDroite) //
			break;
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
		
		Bowling monBowling = new Bowling() ;
		
	}

}

