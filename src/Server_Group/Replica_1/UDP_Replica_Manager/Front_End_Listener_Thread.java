package Server_Group.Replica_1.UDP_Replica_Manager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Map.Entry;

public class Front_End_Listener_Thread implements Runnable{

	public Front_End_Listener_Thread() {
		
	}
	
	public static Boolean Broad_Cast_Request(){
		DatagramSocket socket = null;
	    try {
	    	socket = new DatagramSocket();
	    	socket.setSoTimeout(5000);
	    	byte[] message = (new String("getRequestIdNumber")).getBytes();
	    	InetAddress host = InetAddress.getByName(Replica_Manager_Config.HOST_NAME);
	    	for(Entry<Integer, String> entry: Replica_Manager_Config.PORT_HOST.entrySet()){
	    		if(entry.getKey() == Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT){
	    			continue;
	    		}else{
	    			DatagramPacket request = new DatagramPacket(message, message.length, host, entry.getKey());
		    		socket.send(request);
		    		byte[] buffer = new byte[100];
			    	DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			    	try {
						socket.receive(reply);
						String result = new String(reply.getData()).trim();
						System.out.println(entry.getValue()+" is: "+ result);
					} catch (SocketTimeoutException e) {
						System.out.println(entry.getValue() + " is crushed");
					}
	    		}
	    	}
	    	return true;
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
	
	@Override
	public void run() {
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket(Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT); // port: 4000
			while(true){
				byte[] buffer = new byte[1000]; 
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				
				String request_ID = new String(request.getData()).trim().split("\n")[0];
				Replica_Manager_Config.REQUEST_HASH_TABLE.put(request_ID, new String(request.getData()).trim().toString());
				
				String acknowledgement = "OK";
				Boolean bcr = Broad_Cast_Request();
				if(bcr){
					System.out.println("delete request: "+ request_ID +" from hashtable");
					Replica_Manager_Config.REQUEST_HASH_TABLE.remove(request_ID);
					
					System.out.println("Send acknowledgement back to FE.");
					socket.send(new DatagramPacket(acknowledgement.getBytes(),acknowledgement.getBytes().length, request.getAddress(), request.getPort()));
					System.out.println("Create new thread to handle the request from FE");
					new UDP_CORBA_Connection_Thread(socket, request);
				}	
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(socket != null) socket.close();
		}
	}
}
