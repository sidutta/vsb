/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.chorevolution.vsb.playgrounds.clientserver.coap.test;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.chorevolution.vsb.playgrounds.clientserver.coap.ObservableServer;
import eu.chorevolution.vsb.playgrounds.clientserver.coap.test.utils.Parameters;

public class StartExperiment {

	public static boolean experimentRunning = true;
	public static HashMap<Long, Long> startTimeMap = new HashMap<Long, Long>();
	public static HashMap<Long, Long> endTimeMap = new HashMap<Long, Long>();
	public static Long messagesReceived = 0L;
	public static boolean DEBUG = true;
	public static long experimentStartTime = 0l;
	
	public static void main(String[] args) throws Exception {

		// just to ensure initialization so that no time wasted in first msg sent
		StartExperiment.startTimeMap.put(-1L, -1L);
		StartExperiment.endTimeMap.put(-1L, -1L);
////
		StartServer server = new StartServer();
		System.err.println("server started!!");
//
		Thread.sleep(2000);
		
		StartClient client = new StartClient(StartExperiment.endTimeMap);
		System.err.println("client connected!!");
		
//		Thread.sleep(20000);
		
		
//		Thread.sleep(20000);
		
		experimentStartTime = System.nanoTime();

		new Thread(new NetworkToggle()).start();

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				StartExperiment.experimentRunning = false;

				try {
					Thread.sleep(2000);
				} catch (InterruptedException ex) {
					Logger.getLogger(StartExperiment.class.getName()).log(Level.SEVERE, null, ex);
				}
				
				System.out.println("Packets Sent: " + ObservableServer.counter);
				System.out.println("Packets Received: " + StartExperiment.messagesReceived);
				System.out.println("Packet Loss: " + (ObservableServer.counter - StartExperiment.messagesReceived));

				Long dur = 0L;

				if(StartExperiment.endTimeMap.containsKey(0L))
					StartExperiment.endTimeMap.remove(0L);
				
				for(java.util.Map.Entry<Long, Long> e: StartExperiment.endTimeMap.entrySet()) {
					dur += (e.getValue() - StartExperiment.startTimeMap.get(e.getKey()));
				}

				System.out.println("Average time: " + dur.doubleValue()/StartExperiment.endTimeMap.size());

				System.out.println("on duration: " + NetworkToggle.onParameter.average());
				System.out.println("off duration: " + NetworkToggle.offParameter.average());
				System.out.println("msgs: " + ObservableServer.waitDuration.average());
				
			}
		}, Parameters.experimentDuration);
	}

}