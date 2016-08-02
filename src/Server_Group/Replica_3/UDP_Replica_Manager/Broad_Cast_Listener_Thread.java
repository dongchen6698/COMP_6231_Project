package Server_Group.Replica_3.UDP_Replica_Manager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Broad_Cast_Listener_Thread implements Runnable{
	public Broad_Cast_Listener_Thread() {
		
	}
	
	@Override
	public void run() {
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket(Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT);
			while(true){
				byte[] buffer = new byte[1000]; 
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				socket.send(new DatagramPacket("OK".getBytes(), "OK".getBytes().length, request.getAddress(), request.getPort()));
				System.out.println("receive from leader, handle request, no reply back.");
				new UDP_CORBA_Connection_Thread(socket, request);
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
