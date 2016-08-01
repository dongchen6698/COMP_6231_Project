package Server_Group.Record_ID_Dispatcher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RecordID_Dispatcher_Server {
	private static int startNumber = 10000;
	
	/**
	 * This method is get the number of the ID
	 * @return int
	 */
	public static synchronized int getSartNumber(){
		return startNumber++;
	}
	
	public static void main(String[] args) {
		System.out.println("RecordID Dispatcher Server Ready And Waiting ...");
		openUDPListener();
	}
	
	/**
	 * Open UDP listening port to assign unique recordID number.
	 * 
	 */
	public static void openUDPListener(){
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket(Config_RecordID_Dispatcher_Server.LOCAL_LISTENING_PORT);
			while(true){
				byte[] buffer = new byte[100]; 
				DatagramPacket request = new DatagramPacket(buffer, buffer.length); 
				socket.receive(request);
				new Connection(socket, request);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(socket != null) socket.close();
		}
	}
	
	/**
	 * New thread to handle the newly request
	 * @author AlexChen
	 *
	 */
	static class Connection extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		String result = null;
		public Connection(DatagramSocket n_socket, DatagramPacket n_request) {
			this.socket = n_socket;
			this.request = n_request;
			String content = new String(request.getData()).trim();
			System.out.println(content);
			switch (content) {
			case "getRecordIdNumber":
				result = Integer.toString(RecordID_Dispatcher_Server.getSartNumber());
				break;
			}
			this.start();
		}
		
		@Override
		public void run() {
			try {
				DatagramPacket reply = new DatagramPacket(result.getBytes(),result.getBytes().length, request.getAddress(), request.getPort()); 
				socket.send(reply);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
