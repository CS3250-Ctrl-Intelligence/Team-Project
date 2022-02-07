import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * 
 * @author Matthew White
 * 
 * WelcomePage class creates and displays a generic welcome page using swing.
 * Is displayed after successfully "logging in". Will likely be changed.
 *
 */

public class WelcomePage {
	
	JFrame frame = new JFrame();
	JLabel welcomeLabel = new JLabel("Welcome to CTRL inc.");
	
	WelcomePage(){
		
		welcomeLabel.setBounds(150, 10, 450, 50);
		welcomeLabel.setFont(new Font(null, Font.PLAIN, 10));
		
		frame.add(welcomeLabel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 700);
		frame.setLayout(null);
		frame.setVisible(true);
	}

}
