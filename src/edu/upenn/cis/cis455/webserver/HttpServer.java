package edu.upenn.cis.cis455.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class HttpServer {
	public static int port = 8080;
	public static String homeDirectory = "";
	public static boolean runFlag = true;
	public static ServerSocket server;
	
	public static void main(String[] args) {
		if( args.length != 2 ){
			System.out.println("Wrong number of arguments!");
			return;
		}
		port = Integer.valueOf(args[0]);
		homeDirectory = args[1];
		ThreadPool threadPool = new ThreadPool(10, 100, homeDirectory);
		
        try {
			server = new ServerSocket(port, 20000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		while(runFlag) {
			Socket client = null;
            try {
                client = server.accept();
                System.out.println("One client socket accepted");
                threadPool.handleSocket(client);

                //break;
            } catch (IOException e) {
                e.printStackTrace();
                //break;
            }
            System.out.println("Server Socket is listening another socket.");
		}
	}
	
    public static void closeSocket() {
        try {
            runFlag = false;
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
