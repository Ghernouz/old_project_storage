import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.io.File;
import java.io.FileInputStream;

public class FenetreMenu extends JFrame implements ActionListener{
	
	JPanel panneauBouton;
	JButton monBoutonClassic;
	JButton monBoutonArcade;
	JButton monBoutonExit;
	JLabel monImage;
	Bowling monBowling;
	Arcade monBowlingArcade;
	boolean Bowlinglaunched;
	boolean Arcadelaunched;

	public FenetreMenu (boolean visible){
		
		Bowlinglaunched=false;
		Arcadelaunched=false;
		
		this.setTitle("Welcome To Space Bowling !");
		this.setLayout(null);
		this.setSize(1344,840);
		this.setLocation(0,0);
		this.setResizable(false);
		this.setVisible(visible);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panneauBouton = new JPanel();
		panneauBouton.setBounds(0,0,1344,840);
		panneauBouton.setLayout(null);
		panneauBouton.setBackground(Color.white);
		
		monImage = new JLabel(new ImageIcon("./Images/spacebowling.jpg"));
		monImage.setBounds(0,0,1344,840);
		
		monBoutonClassic = new JButton("Classic Mode");
		Font police = new Font(" aspace_demo ",Font.PLAIN,18);
		monBoutonClassic.setFont(police);
		monBoutonClassic.setBounds(631,390,150,50);
		monBoutonClassic.setBackground(new Color(90,15,125));
		monBoutonClassic.setForeground(new Color(120,250,190));
		monBoutonClassic.addActionListener(this);
		
		monBoutonArcade = new JButton("Arcade Mode");
		monBoutonArcade.setFont(police);
		monBoutonArcade.setBounds(631,450,150,50);
		monBoutonArcade.setBackground(new Color(250,55,250));
		monBoutonArcade.setForeground(new Color(120,250,190));
		monBoutonArcade.addActionListener(this);
		
		monBoutonExit = new JButton("Exit");
		monBoutonExit.setFont(police);
		monBoutonExit.setBounds(631,510,150,50);
		monBoutonExit.setBackground(new Color(190,35,150));
		monBoutonExit.setForeground(new Color(120,250,190));
		monBoutonExit.addActionListener(this);
		
		panneauBouton.add(monBoutonExit);
		panneauBouton.add(monBoutonArcade);
		panneauBouton.add(monBoutonClassic);
		panneauBouton.add(monImage);
		
		
		this.setContentPane(panneauBouton);
		
	}
	
	public void actionPerformed(ActionEvent e){
		
		if (e.getSource() == monBoutonClassic && !Bowlinglaunched && !Arcadelaunched) {
			monBowling = new Bowling() ;
			this.dispose();
			Bowlinglaunched=true;
				
		}
			
		if (e.getSource() == monBoutonArcade && !Bowlinglaunched && !Arcadelaunched){
			monBowlingArcade = new Arcade();
			this.dispose();
			Arcadelaunched=true;
		}
		
		if (e.getSource() == monBoutonExit){
			this.dispose();
		}	
			
	}
		
}
