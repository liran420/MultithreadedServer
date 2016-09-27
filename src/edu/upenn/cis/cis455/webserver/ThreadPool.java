package edu.upenn.cis.cis455.webserver;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ThreadPool {
	private BlockingQueue taskQueue;
	private static List<ThreadWorker> threads = new ArrayList<ThreadWorker>();
	private static Map<ThreadWorker, String> status = new HashMap<>();
	
	public ThreadPool(int threadLimit, int taskLimit, String homeDirectory) {
	    taskQueue = new BlockingQueue(taskLimit);
	    
	    // add threads to the threads list
	    for(int i = 0; i < threadLimit; i++) {
	    	ThreadWorker worker = new ThreadWorker(taskQueue);
	    	ThreadWorker.setHome(homeDirectory);
	    	threads.add(worker);
	    }
	    // let the thread worker in the threads list start work
	    for(ThreadWorker thread : threads){
	    	System.out.println("Thread is running");
	    	thread.start();
	    }    
	}
	
	 // add tasks to the task queue
	 public void handleSocket(Socket s) {
	    try {
	      taskQueue.enqueue(s);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	 }
	 
	  // shutdown threads
	  public synchronized static void closeThreads() throws IOException {
	      for(ThreadWorker thread: threads) {
	    	  thread.closeSocket();  /* Socket listening for incoming request should be closed by socket closing */
	          thread.interrupt();
	      }
	  }
	  
	  public static Map<ThreadWorker, String> getStatus(){
		  for(ThreadWorker thread: threads) {
			  status.put(thread, thread.getState().toString().equalsIgnoreCase("Waiting") ? "Waiting" : thread.getPath());
		  }
		  return status;
	  }
}