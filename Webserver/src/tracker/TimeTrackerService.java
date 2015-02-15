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

public class TimeTrackerService {

	private InputStreamReader reader;
	private OutputStreamWriter writer;
	private char[] buffer;
	private File trackingFile;
	
	private static final String USER_HASH = "e55d7dac9520d4225db4e314526b3a855f96815a4ca7fd4713e46582a61c7b7e9d62ba5c85a668cedd0100869f904b322662f09b1654f97c4895e365484c0212";
	private static final String PW_HASH = "ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413";
	
	public TimeTrackerService(InputStreamReader reader, OutputStreamWriter writer, char[] buffer) {
		this.reader = reader;
		this.writer = writer;
		this.buffer = buffer;
		trackingFile = new File(System.getProperty("user.home") + "/TimeTracker/konstantin.tt");
	}
	
	public void run() {
		int i = 0;
		boolean checkUser = checkUserName(i);
		if(!checkUser) return;
		i = 0;
		boolean checkPW = checkPassword(i);
		if(!checkPW) return;
		short fileWrite = checkTracking();
		writeFile(fileWrite);
	}
	
	private boolean checkUserName(int i) {
		try {
			while(i < 3) {
				writer.write("Please send your username");
				writer.flush();
				reader.read(buffer);
				if(String.valueOf(buffer) == USER_HASH)
					return true;
				i++;
			}
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
				reader.read(buffer);
				if(String.valueOf(buffer) == PW_HASH)
					return true;
				i++;
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	private short checkTracking() {
		try {
			writer.write("Do you want to track start (0) or end (1) ?");
			writer.flush();
			reader.read(buffer);
			return Short.parseShort(String.valueOf(buffer));
		} catch (IOException e) {
			return -1;
		}
	}
	
	private void writeFile(short i) {
		try {
			int bufferSize = 1024 * 1024;
			char[] fb = new char[bufferSize]; 
			FileReader fr = new FileReader(trackingFile);
			fr.read(fb);
			FileWriter fw = new FileWriter(trackingFile);
			String fbToString = String.valueOf(fb);
			if(fbToString == "") {
				fbToString = "<tt>\n\t<user>\n\t\tkonstantin\n\t</user>\n";
			}
			else {
				fbToString = fbToString.replace("\n</tt>", "");
			}
			if(i == 0) {
				fbToString = fbToString + "\t<date>\n\t\t" + currentTimeForFile(0)
						+ "\n\t\t<startTime>\n\t\t\t" + currentTimeForFile(1) 
						+ "\n\t\t</startTime>\n\t</date>\n</tt>";
			}
			else if(i == 1) {
				fbToString = fbToString.replace("</date>", "");
				fbToString = fbToString + "\t<endTime>\n\t\t\t" + currentTimeForFile(1) 
						+ "\n\t\t</endTime>\n\t</date>\n</tt>";
			}
			else {
				fbToString = fbToString + "\n</tt>";
			}
			fw.write(fbToString);
			fw.flush();
			fw.close();
			fr.close();
		} catch (IOException e) {

		}
	}
	
	private String currentTimeForFile(int i) {
		if(i == 0) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			return dateFormat.format(date);
		}
		else if(i == 1) {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			return dateFormat.format(date);
		}
		else
			return "";
	}
	
}
