package server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import tracker.TimeTrackerService;

public class WebServer implements Runnable {

	private final int port;
	private ServerSocket serverSocket;
	private ServerControlPanel controller;
	private Thread controllerThread;
	private boolean isRunning;
	
	private ConcurrentLinkedQueue<Socket> connectedClients;
	
	public WebServer(int port) {
		this.port = port;
		createServer();
	}
	
	private void createServer() {
		try {
			serverSocket = new ServerSocket(port);
			controller = new ServerControlPanel(this);
			connectedClients = new ConcurrentLinkedQueue<Socket>();
			System.out.println("Server started!");
		} catch (IOException e) {
			System.err.println("Error at creating server!");
		}
	}
	
	@Override
	public void run() {
		controllerThread = new Thread(controller);
		controllerThread.start();
		isRunning = true;
		runServer();
	}
	
	private void runServer() {
		try {
			Socket connection;
			char[] inputMessage = new char[1024];
			while(isRunning) {
				connection = serverSocket.accept();
				connectedClients.add(connection);
				InputStreamReader fromClientReader = new InputStreamReader(connection.getInputStream());
				OutputStreamWriter toClientWriter = new OutputStreamWriter(connection.getOutputStream());
				System.out.println("New client connected");
				int i = fromClientReader.read(inputMessage);
				if(String.valueOf(inputMessage).substring(0, i).equals("time")) {
					TimeTrackerService timeTracker = new TimeTrackerService(fromClientReader, toClientWriter, inputMessage);
					new Thread(timeTracker).start();
				}
			}
		} catch (IOException e) {
			if(connectedClients.isEmpty())
				System.out.println("Server closed");
			else {
				close();
				System.err.println("Error at running server!");
			}
		}
	}
	
	public synchronized void close() {
		try {
			controller = null;
			isRunning = false;
			connectedClients.clear();
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("Error at shutting down server!");
		}
	}
	
}
