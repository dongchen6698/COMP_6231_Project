package Front_End;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import Front_End.FRONT_END_CORBA.Front_End;
import Front_End.FRONT_END_CORBA.Front_EndHelper;
import Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config;
import Server_Group.Replica_1.UDP_Replica_Manager.UDP_CORBA_Connection_Thread;


public class Front_End_Server {
	public static void main(String[] args) {
		System.out.println("Initial the front end corba part");
		init_Front_End_CORBA(args);
		System.out.println("open a listener for update the config file of leader info.");
		open_UDP_Listener_For_Update_Leader_info();
	}
	

	public static void init_Front_End_CORBA(String[] args){
		try {
			//initial the port number of 1050;
			Properties props = new Properties();
	        props.put("org.omg.CORBA.ORBInitialPort", Front_End_Config.ORB_INITIAL_PORT);
	        
			// create and initialize the ORB
			ORB orb = ORB.init(args, props);

			// get reference to rootpoa & activate the POAManager
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			Front_End_Impl fe_Impl = new Front_End_Impl();
			fe_Impl.setORB(orb); 

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(fe_Impl);
			Front_End href = Front_EndHelper.narrow(ref);
			    
			// get the root naming context
			// NameService invokes the name service
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			// Use NamingContextExt which is part of the Interoperable
			// Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// bind the Object Reference in Naming
			String name = Front_End_Config.SERVER_NAME;
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, href);

			System.out.println("Front End Server Ready And Waiting ...");

			// wait for invocations from clients
			orb.run();
		} catch (Exception e) {
			System.err.println("ERROR: " + e);
	        e.printStackTrace(System.out);
		}
		System.out.println("Front End Server Exiting ...");
	}
	
	public static void open_UDP_Listener_For_Update_Leader_info(){
		Thread update = new Thread(new Runnable() {
			@Override
			public void run() {
				DatagramSocket socket = null;
				try{
					socket = new DatagramSocket(Front_End_Config.LOCAL_LISTENING_PORT); // port: 3500
					while(true){
						System.out.println("start update primary leader listener");
						byte[] buffer = new byte[1000]; 
						DatagramPacket request = new DatagramPacket(buffer, buffer.length);
						socket.receive(request);
						int leader_port = Integer.parseInt(new String(request.getData()).trim());
						Front_End_Config.PRIMARY_SERVER_PORT = leader_port;
						String acknowledgement = "OK";
						DatagramPacket reply = new DatagramPacket(acknowledgement.getBytes(),acknowledgement.getBytes().length, request.getAddress(), request.getPort());
						socket.send(reply);
						}	
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					if(socket != null) socket.close();
				}
			}
		});
		update.start();
	}
}
