import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
 * @author Matthew White
 *Class LoginPage takes HashMap <String, String> for constructor.
 *This HashMap should contain the User name / Password key value pairs, as they are used for user authentication
 *This class displays and executes user login.
 *Displaying user login fields to user, taking in inputs and checking them against the HashMap of usernames/passwords
 *Currently moves to welcome page if username and password are correct, and closes login page.
 *Displays incorrect username or password if EITHER the username does not appear in the HashMap, or the Password does not match the value of the corresponding key.
 */
public class LoginPage implements ActionListener{
	
	JFrame frmLogIn = new JFrame(); // instantiates the JFrame
	JButton loginButton = new JButton("Login"); //instantiates a button, string passed in the constructor is displayed as text on the button.
	JButton resetButton = new JButton("Reset");
	JTextField userIDField = new JTextField(); // instantiates a field that can be typed in
	JPasswordField userPasswordField = new JPasswordField(); // same as field above but text typed in is displayed as dots for security.
	JLabel userIDLabel = new JLabel("User Name:"); // instantiates a label, essentially a text box of sorts, passing a string in the constructor displays the string as text. Text can be changed later.
	JLabel userPasswordLabel = new JLabel("Password:");
	JLabel messageLabel = new JLabel(); //to display a log in success/fail message
	
	HashMap<String, String> logininfo = new HashMap<String, String>();
	
	LoginPage(HashMap<String, String> loginInfoOriginal){
		
		logininfo = loginInfoOriginal;
		
		userIDLabel.setBounds(26, 75, 75, 75); // sets placement of label x-cord, y-cord, height, width
		userPasswordLabel.setBounds(26, 150, 75, 25);
		
		messageLabel.setBounds(120, 250, 250, 35);
		messageLabel.setFont(new Font(null, Font.PLAIN,20)); // changes font, and text size. If text size overflows the bounds set earlier, it will display as "Overfl...".
		
		userIDField.setBounds(92, 100, 200, 25);
		userPasswordField.setBounds(92, 150, 200, 25);
		
		loginButton.setBounds(142, 200, 100, 25);
		loginButton.setFocusable(false); //removes the dotted line around text when you click on the button.
		loginButton.addActionListener(this);//listens for action in this class.
		
		frmLogIn.getContentPane().add(userIDLabel); // frame.add(element) adds an element we created earlier to the JFrame named frame.
		frmLogIn.getContentPane().add(userPasswordLabel);
		frmLogIn.getContentPane().add(messageLabel);
		frmLogIn.getContentPane().add(userIDField);
		frmLogIn.getContentPane().add(userPasswordField);
		frmLogIn.getContentPane().add(loginButton);
		frmLogIn.getContentPane().add(resetButton);
		frmLogIn.setTitle("Log In");
		frmLogIn.setAlwaysOnTop(true);
		frmLogIn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogIn.setSize(400, 400); // Sets frame size H x W
		frmLogIn.getContentPane().setLayout(null); // could use a pre-made layout like card, grid, etc. in this case we are setting everything manually so we entered null.
		
		JLabel lblNewLabel = new JLabel("Welcome!");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
		lblNewLabel.setBounds(147, 30, 95, 30);
		frmLogIn.getContentPane().add(lblNewLabel);
		frmLogIn.setVisible(true); // allows frame to be seen.
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==loginButton) { // if the login button is clicked then...
			
			String username = userIDField.getText(); // store the text that was input in the userIDField when the button was clicked in the variable, userID. Will use this to check against hashmap.
			String password = String.valueOf(userPasswordField.getPassword()); // extra step since it is password field and only dots are displayed, this gets the actual text that was entered
			
			
			if(logininfo.containsKey(username)) { // checks if username is a key in hashmap
				if(logininfo.get(username).equals(password)) { // checks if password matches value for that key.
					frmLogIn.dispose(); // delete the login JFrame, closing it because we do not need it anymore.
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								Table window = new Table();
								window.frmAdmin.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

				}
				else { // username does not appear in hashmap, so display message.
					messageLabel.setForeground(Color.red);
					messageLabel.setText("Incorrect User Name or Password");
				}
				
			}
			else { //password does not match value pair of key, so display message.
				messageLabel.setForeground(Color.red);
				messageLabel.setText("Incorrect User Name or Password");
			}
		}
	}
}
