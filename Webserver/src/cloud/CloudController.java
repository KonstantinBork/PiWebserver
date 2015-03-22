package cloud;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import security.LogIn;

// Controller
public class CloudController implements Runnable {

	private CloudService service;
	
	private InputStreamReader reader;
	private OutputStreamWriter writer;
	private char[] buffer;
	
	public CloudController(InputStreamReader reader, OutputStreamWriter writer, char[] buffer, CloudService cs) {
		this.reader = reader;
		this.writer = writer;
		this.buffer = buffer;
		service = cs;
	}

	@Override
	public void run() {
		LogIn logIn = new LogIn(reader, writer);
		if(!logIn.logIn()) return;
		
	}

}
