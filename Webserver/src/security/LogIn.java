package security;

import utils.interfaces.Interaction;

public class LogIn {

	private Interaction<String> messenger;
	
	private static final String USER_HASH = "e55d7dac9520d4225db4e314526b3a855f96815a4ca7fd4713e46582a61c7b7e9d62ba5c85a668cedd0100869f904b322662f09b1654f97c4895e365484c0212";	// hash for "konstantin"
	private static final String PW_HASH = "ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413";	// hash for "123456"
	
	public LogIn(Interaction<String> m) {
		messenger = m;
	}
	
	public boolean logIn() {
		int i = 0;
		boolean checkUser = checkUserName(i);
		if(!checkUser) return false;
		i = 0;
		boolean checkPW = checkPassword(i);
		if(!checkPW) return false;
		return true;
	}
	
	private boolean checkUserName(int i) {
		while(i < 3) {
			messenger.send("Please send your username");
			String message = messenger.receive();
			if(message == null)
				continue;
			if(message.equals(USER_HASH)) {
				System.out.println("User tries to log in!");
				return true;
			}
			i++;
		}
		messenger.send("Wrong username");
		return false;
	}
	
	private boolean checkPassword(int i) {
		while(i < 3) {
			messenger.send("Please send your password");
			String message = messenger.receive();
			if(message == null)
				continue;
			else if(message.equals(PW_HASH)) {
				System.out.println("User successfully logged in!");
				return true;
			}
			i++;
		}
		messenger.send("Wrong password");
		return false;
	}
	
}
