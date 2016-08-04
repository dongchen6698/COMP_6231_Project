package Server_Group.Replica_3.UDP_Replica_Manager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;

public class Failure_Detection_Thread implements Runnable{
	private Thread sender;
	private Thread receiver;
	public Failure_Detection_Thread() {
		this.sender = new Thread(new Runnable() {
			
			@Override
			public void run() {
				DatagramSocket socket = null;
			    try {
			    	socket = new DatagramSocket();
			    	socket.setSoTimeout(5000);
			    	String ask = "Do you alive?";
			    	InetAddress host = InetAddress.getByName(Replica_Manager_Config.HOST_NAME);
			    	while(true){
			    		Thread.sleep(2000);
			    	for(Entry<Integer, String> entry: Replica_Manager_Config.FAILURE_DETECTION_PORT.entrySet()){
			    		if(entry.getKey() == Replica_Manager_Config.LOCAL_FAILURE_DETECTION_PORT){
			    			continue;
			    		}else{
			    			DatagramPacket request = new DatagramPacket(ask.getBytes(), ask.getBytes().length,host, entry.getKey());
			    			socket.send(request);
				    		byte[] buffer = new byte[100];
					    	DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					    	try {
								socket.receive(reply);
								String result = new String(reply.getData()).trim();
								System.out.println(entry.getValue()+" is: "+ result);
							} catch (SocketTimeoutException e) {
								System.out.println(entry.getValue() + " is crushed");
								Integer leader_port = checkWhoIsLeader();
								for(Entry<Integer, String> main: Replica_Manager_Config.MAIN_LISTENING_PORT.entrySet()){
									if(main.getKey().equals(leader_port)){
										if(main.getValue().equals(entry.getValue())){
											System.out.println(leader_port+" leader is crushed");
											Integer new_port = Bully_Algorithm(leader_port);
											if(new_port.equals(Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT)){
												send_New_Leader_To_FE(new_port);
											}
										}
									}
								}
							}
			    		}
			    	}
			    	}
			    }catch (Exception e){
			    	e.printStackTrace();
			    }
			}
		});
		
		this.receiver = new Thread(new Runnable() {
			
			@Override
			public void run() {
				DatagramSocket socket = null;
				try{
					socket = new DatagramSocket(Replica_Manager_Config.LOCAL_FAILURE_DETECTION_PORT); // port: 6500
					while(true){
						byte[] buffer = new byte[1000]; 
						DatagramPacket request = new DatagramPacket(buffer, buffer.length);
						socket.receive(request);
						
						String acknowledgement = "OK";
						DatagramPacket reply = new DatagramPacket(acknowledgement.getBytes(),acknowledgement.getBytes().length, request.getAddress(), request.getPort());
						socket.send(reply);
					}	
				}catch(Exception e){
					e.printStackTrace();
				}
				finally{
					if(socket != null) socket.close();
				}
			}
		});
	}

	@Override
	public void run() {
		this.sender.start();
		this.receiver.start();
		
	}
	
	public static Integer checkWhoIsLeader(){
		DatagramSocket socket = null;
		Integer result = 0;
	    try {
	    	socket = new DatagramSocket();
	    	String ask = "who is leader?";
	    	InetAddress host = InetAddress.getByName(Replica_Manager_Config.HOST_NAME);
	    	DatagramPacket request = new DatagramPacket(ask.getBytes(), ask.getBytes().length, host, Replica_Manager_Config.FE_LISTENING_PORT);
	    	socket.send(request);
		    byte[] buffer = new byte[100];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			result = Integer.parseInt(new String(reply.getData()).trim());
			} catch (Exception e) {
				e.printStackTrace();
	    	}
		return result;
	}
	
	
	public Integer Bully_Algorithm(Integer present_leader){
		ArrayList<Integer> new_port_set = new ArrayList<>();
		for(Integer key: Replica_Manager_Config.MAIN_LISTENING_PORT.keySet()){
			if(key.equals(present_leader)){
				continue;
			}
			new_port_set.add(key);
		}
		Integer min_port = Collections.min(new_port_set);
		return min_port;
	}
	
	public void send_New_Leader_To_FE(Integer port){
		DatagramSocket socket = null;
	    try {
	    	socket = new DatagramSocket();
	    	String ask = Integer.toString(port);
	    	InetAddress host = InetAddress.getByName(Replica_Manager_Config.HOST_NAME);
	    	DatagramPacket request = new DatagramPacket(ask.getBytes(), ask.getBytes().length, host, Replica_Manager_Config.FE_LISTENING_PORT);
	    	socket.send(request);
		    byte[] buffer = new byte[100];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			String result = (new String(reply.getData()).trim());
			if(result.equals("OK")){
				System.out.println("update new leader success");
			}
			}catch (Exception e) {
				e.printStackTrace();
	    	}
	}
}
