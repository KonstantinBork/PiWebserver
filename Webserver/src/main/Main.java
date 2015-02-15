package main;

import server.WebServer;

public class Main {

	public static void main(String[] args) {
		WebServer server = new WebServer(25523);
		Thread serverThread = new Thread(server);
		serverThread.start();
		try {
			serverThread.join();
			System.exit(0);
		} catch (InterruptedException e) {
		
		}
	}
}