package tracker;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import utils.interfaces.Interaction;

public class TimeTrackerService implements Runnable {

	private Interaction<String> messenger;
	private File trackingFile;
	
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
		try {
			int bufferSize = 1024 * 1024;	// size of 1 MB
			char[] fb = new char[bufferSize]; 
			FileReader fr = new FileReader(trackingFile);
			int l = fr.read(fb);
			FileWriter fw = new FileWriter(trackingFile);
			String fbToString = "";
			if(l == -1)
				fbToString = initializeFile();
			else
				fbToString = String.valueOf(fb).substring(0, l);
			fbToString = addDate(i, fbToString);
			fw.write(fbToString);
			fw.flush();
			fw.close();
			fr.close();
			System.out.println("User tracked time");
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Initializes file if it does not exist.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String initializeFile() {
		JSONObject tt = new JSONObject();
		JSONObject user = new JSONObject();
		user.put("user", "Konstantin");
		tt.put("tt", user);
		return tt.toString();
	}
	
	@SuppressWarnings("unchecked")
	private String addDate(short i, String currentValue) {
		JSONObject obj = (JSONObject) JSONValue.parse(currentValue);
		JSONArray days = (JSONArray) ((JSONObject) obj.get("tt")).get("days");
		JSONObject day;
		if(days == null) {
			days = new JSONArray();
		}
		int l = days.size();
		switch(i) {
			case 0:
				day = new JSONObject();
				day.put("date", currentTimeForFile(0));
				day.put("startTime", currentTimeForFile(1));
				days.add(day);
				break;
			case 1:
				day = (JSONObject) days.get(l - 1);
				day.put("endTime", currentTimeForFile(1));
				break;
			case 2:
				day = (JSONObject) days.get(l - 1);
				day.put("breakStartTime", currentTimeForFile(1));
				break;
			case 3:
				day = (JSONObject) days.get(l - 1);
				day.put("breakEndTime", currentTimeForFile(1));
				break;
			default:
				day = null;
		}
		((JSONObject) obj.get("tt")).put("days", days);
		return obj.toString();
	}
	
	/**
	 * Returns the current time.
	 * @param i Mode to give the date (0) or the time (1).
	 * @return
	 */
	private String currentTimeForFile(int i) {
		if(i == 0) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			return dateFormat.format(date);
		}
		else if(i == 1) {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm");
			Date date = new Date();
			return dateFormat.format(date);
		}
		else
			return "";
	}
	
}
