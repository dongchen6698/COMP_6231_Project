package Server_Group.Replica_2.UDP_Replica_Manager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import Server_Group.Replica_1.DSMS_CORBA.DSMS;
import Server_Group.Replica_1.DSMS_CORBA.DSMSHelper;
import Server_Group.Replica_2.ClinicServer.ClinicServerInt;
import Server_Group.Replica_2.ClinicServer.ClinicServerIntHelper;

public class UDP_CORBA_Connection_Thread extends Thread{
	DatagramSocket socket = null;
	DatagramPacket request = null;
	String result = null;
	public UDP_CORBA_Connection_Thread(DatagramSocket n_socket, DatagramPacket n_request) {
		this.socket = n_socket;
		this.request = n_request;
		
		String function_ID = new String(request.getData()).trim().split("\n")[1];
		String manager_ID = new String(request.getData()).trim().split("\n")[2];
		
		Replica_Manager_Config.DSMS_CORBA_IMPL = getServerReferrence(manager_ID);
		
		switch (function_ID) {
		case "001":
			String firstName_1 = new String(request.getData()).trim().split("\n")[3];
			String lastName_1 = new String(request.getData()).trim().split("\n")[4];
			String address_1 = new String(request.getData()).trim().split("\n")[5];
			String phone_1 = new String(request.getData()).trim().split("\n")[6];
			String specialization_1 = new String(request.getData()).trim().split("\n")[7];
			String location_1 = new String(request.getData()).trim().split("\n")[8];
			
			result = Replica_Manager_Config.DSMS_CORBA_IMPL.createDRecord(manager_ID, firstName_1, lastName_1, address_1, phone_1, specialization_1, location_1);
			break;
		case "002":
			String firstName_2 = new String(request.getData()).trim().split("\n")[3];
			String lastName_2 = new String(request.getData()).trim().split("\n")[4];
			String designation_2 = new String(request.getData()).trim().split("\n")[5];
			String status_2 = new String(request.getData()).trim().split("\n")[6];
			String statusDate_2 = new String(request.getData()).trim().split("\n")[7];
			
			result = Replica_Manager_Config.DSMS_CORBA_IMPL.createNRecord(manager_ID, firstName_2, lastName_2, designation_2, status_2, statusDate_2);
			break;
		case "003":
			String recordType_3 = new String(request.getData()).trim().split("\n")[3];
			
			result = Replica_Manager_Config.DSMS_CORBA_IMPL.getRecordCounts(manager_ID, recordType_3);
			break;
		case "004":
			String recordID_4 = new String(request.getData()).trim().split("\n")[3];
			String fieldName_4 = new String(request.getData()).trim().split("\n")[4];
			String newValue_4 = new String(request.getData()).trim().split("\n")[5];
			
			result = Replica_Manager_Config.DSMS_CORBA_IMPL.editRecord(manager_ID, recordID_4, fieldName_4, newValue_4);
			break;
		case "005":
			String recordID_5 = new String(request.getData()).trim().split("\n")[3];
			String remoteClinicServerName_5 = new String(request.getData()).trim().split("\n")[4];
			
			result = Replica_Manager_Config.DSMS_CORBA_IMPL.transferRecord(manager_ID, recordID_5, remoteClinicServerName_5);
			break;
		}
		this.start();
	}
	
	@Override
	public void run() {
		try {
			if(socket.getLocalPort() == Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT){
				System.out.println(result);
				DatagramPacket reply = new DatagramPacket(result.getBytes(),result.getBytes().length, request.getAddress(), request.getPort());
				socket.send(reply);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ClinicServerInt getServerReferrence(String n_managerID){
		try {
			//initial the port number of 1050;
			Properties props = new Properties();
	        props.put("org.omg.CORBA.ORBInitialPort", Replica_Manager_Config.ORB_INITIAL_PORT);
	        
			// create and initialize the ORB
	        String[] ar = null;
			ORB orb = ORB.init(ar, props);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			// Use NamingContextExt instead of NamingContext. This is 
			// part of the Interoperable naming Service.  
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			if(n_managerID.substring(0, 3).equalsIgnoreCase("mtl")){
				return ClinicServerIntHelper.narrow(ncRef.resolve_str(Replica_Manager_Config.LOCAL_MTL_SERVER_NAME));
			}else if(n_managerID.substring(0, 3).equalsIgnoreCase("lvl")){
				return ClinicServerIntHelper.narrow(ncRef.resolve_str(Replica_Manager_Config.LOCAL_LVL_SERVER_NAME));
			}else if(n_managerID.substring(0, 3).equalsIgnoreCase("ddo")){
				return ClinicServerIntHelper.narrow(ncRef.resolve_str(Replica_Manager_Config.LOCAL_DDO_SERVER_NAME));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
}
