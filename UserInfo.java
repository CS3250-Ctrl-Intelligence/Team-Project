import java.util.HashMap;
/**
 * 
 * @author Matthew White
 *class ID and Passwords holds a hashmap of username / password key value pairs. This is called in the constructor of LoginPage for user authentication.
 *HashMap was selected because it does not allow duplicate keys, and allows most data types.
 */
public class UserInfo {
	
	HashMap<String, String> logininfo = new HashMap<String, String>(); // constructs a new hashmap called logininfo, usernames should be stored as keys, and the password would be stored as the corresponding value.
	
	UserInfo(){
		
		logininfo.put("Matthew", "m2022");
		logininfo.put("Le","l2022" );
		logininfo.put("Seth", "s2022");

	}
	
	protected HashMap getLoginInfo(){ // get method is protected as we do not want just anything to be able to get the information from the hashmap since it contains log in info.
		return logininfo;
		
	}
}
