package Server_Group.Replica_3.UDP_Replica_Manager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Map.Entry;

public class Front_End_Listener_Thread implements Runnable{

	public Front_End_Listener_Thread() {
		
	}
	
	public static Boolean Broad_Cast_Request(DatagramPacket request){
		DatagramSocket socket = null;
		String message = new String(request.getData()).trim();
		DatagramPacket new_request = new DatagramPacket(message.getBytes(), message.getBytes().length,request.getAddress(), request.getPort());
	    try {
	    	socket = new DatagramSocket();
	    	socket.setSoTimeout(2000);
	    	for(Entry<Integer, String> entry: Replica_Manager_Config.PORT_HOST.entrySet()){
	    		if(entry.getKey() == Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT){
	    			System.out.println("equal local broadcast port , skip");
	    			continue;
	    		}else{
	    			new_request.setPort(entry.getKey());
	    			socket.send(new_request);
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
	    }
	    catch(Exception e){
	    	System.out.println("Socket: " + e.getMessage()); 
	    }
		finally{
			if(socket != null){
				socket.close();
			}
		}
		return true;
	}
	
	@Override
	public void run() {
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket(Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT); // port: 4000
			while(true){
				System.out.println("start fe listener");
				byte[] buffer = new byte[1000]; 
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				
				String request_ID = new String(request.getData()).trim().split("\n")[0];
				System.out.println("add request: "+ request_ID +" to hashtable");
				Replica_Manager_Config.REQUEST_HASH_TABLE.put(request_ID, new String(request.getData()).trim().toString());
				
				String acknowledgement = "OK";
				DatagramPacket reply = new DatagramPacket(acknowledgement.getBytes(),acknowledgement.getBytes().length, request.getAddress(), request.getPort());
				Boolean bcr = Broad_Cast_Request(request);
				if(bcr){
					System.out.println("delete request: "+ request_ID +" from hashtable");
					Replica_Manager_Config.REQUEST_HASH_TABLE.remove(request_ID);
					
					System.out.println("Send acknowledgement back to FE."+reply.getPort());
					socket.send(reply);
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
