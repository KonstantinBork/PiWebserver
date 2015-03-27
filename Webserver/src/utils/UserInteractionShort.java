package utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import utils.interfaces.Interaction;

public class UserInteractionShort implements Interaction<Short>{

	private InputStreamReader reader;
	private OutputStreamWriter writer;
	private char[] buffer;
	
	public UserInteractionShort(InputStreamReader r, OutputStreamWriter w) {
		reader = r;
		writer = w;
		buffer = new char[1024];
	}
	
	@Override
	public boolean send(Short message) {
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
	public Short receive() {
		try {
			int l = reader.read(buffer);
			if(l == -1)
				return null;
			String input = new String(buffer);
			return new Short(Short.parseShort(input));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
