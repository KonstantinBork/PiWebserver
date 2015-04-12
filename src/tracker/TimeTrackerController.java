package tracker;

import security.LogIn;
import utils.interfaces.Interaction;

public class TimeTrackerController implements Runnable {

	private TimeTrackerService service;
	private Interaction<String> messenger;
	
	public TimeTrackerController(Interaction<String> m, TimeTrackerService ts) {
		service = ts;
		messenger = m;
	}
	
	@Override
	public void run() {
		LogIn logIn = new LogIn(messenger);
		if(!logIn.logIn()) return;
		short fileWrite = checkTracking();
		boolean writtenFile = service.writeFile(fileWrite);
		if(writtenFile)
			messenger.send("Tracking was successful!");
		else
			messenger.send("Tracking was not successful!");
		System.out.println("User disconnected");	
	}
	
	private short checkTracking() {
		messenger.send("Do you want to track start (0), end (1), break start (2) or break end (3) ?");
		String res = messenger.receive();
		if(res == null)
			return -1;
		return Short.parseShort(res);
	}

}
