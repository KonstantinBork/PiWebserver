package utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import utils.interfaces.Interaction;

public class UserInteractionString implements Interaction<String> {

	private InputStreamReader reader;
	private OutputStreamWriter writer;
	private char[] buffer;
	
	public UserInteractionString(InputStreamReader r, OutputStreamWriter w) {
		reader = r;
		writer = w;
		buffer = new char[1024];
	}
	
	@Override
	public boolean send(String message) {
		try {
			writer.write(message);
			writer.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String receive() {
		try {
			int l = reader.read(buffer);
			if(l == -1)
				return null;
			return new String(buffer).substring(0, l);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
