package tracker;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class TimeTrackerService implements Runnable {

	private InputStreamReader reader;
	private OutputStreamWriter writer;
	private char[] buffer;
	private File trackingFile;
	
	private static final String USER_HASH = "e55d7dac9520d4225db4e314526b3a855f96815a4ca7fd4713e46582a61c7b7e9d62ba5c85a668cedd0100869f904b322662f09b1654f97c4895e365484c0212";	// hash for "konstantin"
	private static final String PW_HASH = "ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413";	// hash for "123456"
	
	public TimeTrackerService(InputStreamReader reader, OutputStreamWriter writer, char[] buffer) {
		this.reader = reader;
		this.writer = writer;
		this.buffer = buffer;
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
		int i = 0;
		boolean checkUser = checkUserName(i);
		if(!checkUser) return;
		i = 0;
		boolean checkPW = checkPassword(i);
		if(!checkPW) return;
		short fileWrite = checkTracking();
		boolean writtenFile = writeFile(fileWrite);
		try {
			if(writtenFile)
				writer.write("Tracking was successful!");
			else
				writer.write("Tracking was not successful!");
			System.out.println("User disconnected");
			writer.flush();
			writer.close();
			reader.close();
		} catch (IOException e) {
		
		}
		
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
	
	private short checkTracking() {
		try {
			writer.write("Do you want to track start (0), end (1), break start (2) or break end (3) ?");
			writer.flush();
			int l = reader.read(buffer);
			if(l == -1)
				return -1;
			return Short.parseShort(String.valueOf(buffer).substring(0, l));
		} catch (IOException e) {
			return -1;
		}
	}
	
	/**
	 * Writes the tracking file.
	 * @param i Mode to track either the start time (0) or the end time (1).
	 */
	private boolean writeFile(short i) {
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
