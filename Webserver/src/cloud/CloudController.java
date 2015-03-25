package cloud;

import security.LogIn;
import utils.interfaces.Interaction;

// Controller
public class CloudController implements Runnable {

	private CloudService service;
	
	private Interaction<String> messenger;
	
	public CloudController(Interaction<String> m, CloudService cs) {
		messenger = m;
		service = cs;
	}

	@Override
	public void run() {
		LogIn logIn = new LogIn(messenger);
		if(!logIn.logIn()) return;
		while(true) {
			tick();
		}
	}
	
	private void tick() {
		String userInput = messenger.receive();
		messenger.send("File: ");
		if(userInput.equals("add")) {
			service.addFile(null);
		}
		else if(userInput.equals("delete")) {
			service.deleteFile(null);
		}
		else if(userInput.equals("move")) {
			service.move(null, null);
		}
		else if(userInput.equals("get")) {
			service.getFile(null);
		}
		else {
			
		}
	}

}
