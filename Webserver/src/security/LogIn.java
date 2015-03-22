package security;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LogIn {

	private InputStreamReader reader;
	private OutputStreamWriter writer;
	private char[] buffer;
	
	private static final String USER_HASH = "e55d7dac9520d4225db4e314526b3a855f96815a4ca7fd4713e46582a61c7b7e9d62ba5c85a668cedd0100869f904b322662f09b1654f97c4895e365484c0212";	// hash for "konstantin"
	private static final String PW_HASH = "ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413";	// hash for "123456"
	
	public LogIn(InputStreamReader r, OutputStreamWriter w) {
		reader = r;
		writer = w;
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
		try {
			while(i < 3) {
				writer.write("Please send your username");
				writer.flush();
				int l = reader.read(buffer);
				if(l == -1)
					continue;
				if(String.valueOf(buffer).substring(0, l).equals(USER_HASH)) {
					System.out.println("User tries to log in!");
					return true;
				}
				i++;
			}
			writer.write("Wrong username");
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	private boolean checkPassword(int i) {
		try {
			while(i < 3) {
				writer.write("Please send your password");
				writer.flush();
				int l = reader.read(buffer);
				if(l == -1)
					continue;
				else if(String.valueOf(buffer).substring(0, l).equals(PW_HASH)) {
					System.out.println("User successfully logged in!");
					return true;
				}
				i++;
			}
			writer.write("Wrong password");
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
}
