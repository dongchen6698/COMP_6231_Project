package Front_End;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import org.omg.CORBA.ORB;

import Front_End.FRONT_END_CORBA.Front_EndPOA;

public class Front_End_Impl extends Front_EndPOA{

	private ORB orb;
	
	/**
	 * Set ORB function
	 * @param orb_val
	 */
	public void setORB(ORB orb_val) {
	    this.orb = orb_val;
	}
	
	@Override
	public String createDRecord(String managerId, String firstName, String lastName, String address, String phone,
			String specialization, String location) {	
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "001";
		String manager_id = managerId;
		
		String content = firstName+"\n"+lastName+"\n"+address+"\n"+phone+"\n"+specialization+"\n"+location;
		
		String message = request_id+"\n"+function_id+"\n"+manager_id+"\n"+content;
		String result = sendMessageToPrimaryServer(message);
		
		return result;
	}

	@Override
	public String createNRecord(String managerId, String firstName, String lastName, String designation, String status,
			String statusDate) {
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "002";
		String manager_id = managerId;
		
		String content = firstName+"\n"+lastName+"\n"+designation+"\n"+status+"\n"+statusDate;
		
		String message = request_id+"\n"+function_id+"\n"+manager_id + "\n"+content;
		String result = sendMessageToPrimaryServer(message);
		
		return result;
	}

	@Override
	public String getRecordCounts(String managerId, String recordType) {
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "003";
		String manager_id = managerId;
		
		String content = recordType;
		String message = request_id+"\n"+function_id+"\n"+manager_id + "\n"+content;
		
		String result = sendMessageToPrimaryServer(message);

		return result;
	}

	@Override
	public String editRecord(String managerId, String recordID, String fieldName, String newValue) {
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "004";
		String manager_id = managerId;
		String content = recordID + "\n" + fieldName + "\n" + newValue;
		
		String message = request_id+"\n"+function_id+"\n"+manager_id+"\n"+content;
		String result = sendMessageToPrimaryServer(message);
		return result;
	}

	@Override
	public String transferRecord(String managerId, String recordID, String remoteClinicServerName) {
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "005";
		String manager_id = managerId;
		
		String content = recordID + "\n" + remoteClinicServerName;
		
		String message = request_id+"\n"+function_id+"\n"+manager_id+"\n"+content;
		String result = sendMessageToPrimaryServer(message);
		return result;
	}
	
	/**
	 * This function for get request id from a server which generate the request number. 
	 * @return
	 */
	public static String getNumberFromRequestIDGenerator(){
		DatagramSocket socket = null;
	    try {
	    	socket = new DatagramSocket();
	    	byte[] message = (new String("getRequestIdNumber")).getBytes();
	    	InetAddress host = InetAddress.getByName(Front_End_Config.REQUESTID_GENERATOR_IP);
	    	DatagramPacket request = new DatagramPacket(message, message.length, host, Front_End_Config.REQUESTID_GENERATOR_PORT);
	    	socket.send(request);
	    	byte[] buffer = new byte[100];
	    	DatagramPacket reply = new DatagramPacket(buffer, buffer.length); 
	    	socket.receive(reply);
	    	String result = new String(reply.getData()).trim();
	    	return result;
	    }
	    catch(Exception e){
	    	System.out.println("Socket: " + e.getMessage()); 
	    	}
		finally{
			if(socket != null){
				socket.close();
				}
			}
		return null; 
	}
	
	public static String sendMessageToPrimaryServer(String n_message){
		/*
		 * new_message is like
		 * 0001 ---> requestID
		 * 001 ----> function_ID
		 * mtl10000 ---> managerID
		 * dong ----> first_name
		 * chen -----> last_name
		 * montreal,downtown ---> address
		 * 5145899900 ---> phone
		 * mtl ---> location
		 */
		
		// put the request in the hash table first
		// key = requestID, value = message itself
		System.out.println("Add request: "+ n_message.split("\n")[0] +" to hashtable");
		Front_End_Config.REQUEST_HASH_TABLE.put(n_message.split("\n")[0], n_message);
		
		DatagramSocket socket = null;
	    try {
	    	socket = new DatagramSocket();
	    	byte[] message = (new String(n_message)).getBytes();
	    	InetAddress host = InetAddress.getByName(Front_End_Config.PRIMARY_SERVER_IP);
	    	DatagramPacket request = new DatagramPacket(message, message.length, host, Front_End_Config.PRIMARY_SERVER_PORT);
	    	
	    	socket.send(request);
	    	socket.setSoTimeout(5000);
	    	byte[] buffer = new byte[1000];
	    	DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	    	while(true){
	    		try {
	    			socket.receive(reply);
	    			String result = new String(reply.getData()).trim();
	    			if(result.equals("OK")){
	    	    		// if acknowledgement is OK then remove the request from hash map;
	    	    		System.out.println("delete request: "+ n_message.split("\n")[0] +" from hashtable");
	    	    		Front_End_Config.REQUEST_HASH_TABLE.remove(n_message.split("\n")[0]);
	    	    	}else{
	    	    		return result;
	    	    	}
	    		} catch (SocketTimeoutException e) {
	    			InetAddress host_resend = InetAddress.getByName(Front_End_Config.PRIMARY_SERVER_IP);
	    			DatagramPacket request_resend = new DatagramPacket(message, message.length, host_resend, Front_End_Config.PRIMARY_SERVER_PORT);
	    			socket.send(request_resend);
	    		}
	    	}
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
		finally{
			if(socket != null){
				socket.close();
				}
			}
		return null; 
	}
}
