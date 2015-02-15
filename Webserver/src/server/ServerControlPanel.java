package server;

import java.util.Scanner;

public class ServerControlPanel implements Runnable {

	private WebServer server;
	
	public ServerControlPanel(WebServer server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		Scanner input = new Scanner(System.in);
		boolean running = true;
		String in;
		while(running) {
			in = input.nextLine().toLowerCase();
			if(in.matches("exit")) {
				System.out.println("Stopping server. Good Bye!");
				running = false;
				server.close();
				input.close();
				server = null;
				return;
			}
		}
		input.close();
	}

}
