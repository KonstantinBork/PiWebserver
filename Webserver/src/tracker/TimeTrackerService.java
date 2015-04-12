package tracker;

import java.io.File;
import java.io.IOException;

import utils.interfaces.Interaction;

public class TimeTrackerService implements Runnable {

	private Interaction<String> messenger;
	private File trackingFile;
	private FileHandler handler;
	
	public TimeTrackerService(Interaction<String> m) {
		messenger = m;
		trackingFile = new File(System.getProperty("user.home") + "/TimeTracker/konstantin.tt");
		if(!trackingFile.exists()) {
			try {
				trackingFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		handler = new FileHandler(trackingFile);
	}
	
	public void run() {
		TimeTrackerController controller = new TimeTrackerController(messenger, this);
		Thread controllerThread = new Thread(controller);
		controllerThread.start();
		try {
			controllerThread.join();
		} catch (InterruptedException e) {
			System.err.println("Controller Thread was interrupted!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the tracking file.
	 * @param i Mode to track either the start time (0), the end time (1), the break start time (2) the break end time (3) .
	 */
	public boolean writeFile(short i) {
		return handler.writeFile(i);
	}
	
}
