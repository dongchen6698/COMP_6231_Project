package Front_End;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.omg.CORBA.ORB;

import FRONT_END_CORBA.Front_EndPOA;

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
		String content = firstName+","+lastName+","+address+","+phone+","+specialization+","+location;
		String message = request_id+","+function_id+","+content;
		
		String result = sendMessageToPrimaryServer(managerId, message);
		

		return "success";
	}

	@Override
	public String createNRecord(String managerId, String firstName, String lastName, String designation, String status,
			String statusDate) {
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "002";
		String content = firstName+","+lastName+","+designation+","+status+","+statusDate;
		String message = request_id+","+function_id+","+content;
		
		String result = sendMessageToPrimaryServer(managerId, message);
		
		return "success";
	}

	@Override
	public String getRecordCounts(String managerId, String recordType) {
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "003";
		String content = recordType;
		String message = request_id+","+function_id+","+content;
		
		String result = sendMessageToPrimaryServer(managerId, message);

		return "success";
	}

	@Override
	public String editRecord(String managerId, String recordID, String fieldName, String newValue) {
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "004";
		
		System.out.println(managerId + " Edit Record");
		return "success";
	}

	@Override
	public String transferRecord(String managerId, String recordID, String remoteClinicServerName) {
		String request_id = getNumberFromRequestIDGenerator();
		String function_id = "005";
		
		System.out.println(managerId + " Transfer Record");
		return "success";
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
	
	public static String sendMessageToPrimaryServer(String managerId, String message){
		
		return null;
	}
}
