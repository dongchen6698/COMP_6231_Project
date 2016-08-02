package Server_Group.Replica_1.Server_LVL;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import Client_Side.ManagerClients;
import Server_Group.Replica_1.DSMS_CORBA.DSMS;
import Server_Group.Replica_1.DSMS_CORBA.DSMSHelper;

public class Start_LVL_Server {
	public static void main(String[] args) {
		System.out.println("Initial Logger Of Server Laval...");
		initLogger(Server_LVL_Config.SERVER_NAME);
		System.out.println("Initial UDP Listener Of Server Laval...");
		openUDPListener();
		System.out.println("Initial CORBA Of Server Laval...");
		initCORBA(args);		
	}
	
	/**
	 * Initial the Logger function.
	 * @param server_name
	 */
	public static void initLogger(String server_name){
		try {
			String dir = "Server_Side_Log/";
			Server_LVL_Config.LOGGER = Logger.getLogger(ManagerClients.class.getName());
			Server_LVL_Config.LOGGER.setUseParentHandlers(false);
			Server_LVL_Config.FH = new FileHandler(dir+server_name+".log",true);
			Server_LVL_Config.LOGGER.addHandler(Server_LVL_Config.FH);
			SimpleFormatter formatter = new SimpleFormatter();
			Server_LVL_Config.FH.setFormatter(formatter);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open UDP Listener to receive message from other server.
	 */
	public static void openUDPListener(){
		Thread udp_listener = new Thread(new Clinic_LVL_UDP_Listener());
		udp_listener.start();
	}
	
	/**
	 * Initial the CORBA stuff.
	 * @param args
	 */
	public static void initCORBA(String[] args){
		try {
			//initial the port number of 1050;
			Properties props = new Properties();
	        props.put("org.omg.CORBA.ORBInitialPort", Server_LVL_Config.ORB_INITIAL_PORT);
	        
			// create and initialize the ORB
			ORB orb = ORB.init(args, props);

			// get reference to rootpoa & activate the POAManager
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			Clinic_LVL_Impl mtl_Impl = new Clinic_LVL_Impl();
			mtl_Impl.setORB(orb); 

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(mtl_Impl);
			DSMS href = DSMSHelper.narrow(ref);
			    
			// get the root naming context
			// NameService invokes the name service
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			// Use NamingContextExt which is part of the Interoperable
			// Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// bind the Object Reference in Naming
			String name = Server_LVL_Config.SERVER_NAME;
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, href);

			System.out.println("Clinic Montreal Server Ready And Waiting ...");

			// wait for invocations from clients
			orb.run();
		} catch (Exception e) {
			System.err.println("ERROR: " + e);
	        e.printStackTrace(System.out);
		}
		System.out.println("Clinic Montreal Server Exiting ...");
	}
}
