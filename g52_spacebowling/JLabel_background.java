import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

class JLabel_background extends JLabel{
	
	BufferedImage display;
	
	public JLabel_background( BufferedImage A){
		super(new ImageIcon(A));
		display = A;
	}
	
	public void paint( Graphics g ) {
		g.drawImage(display, 0, 0, null);
	}	
	
}
