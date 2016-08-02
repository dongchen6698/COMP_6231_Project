package Server_Group.Replica_2.UDP_Replica_Manager;

import java.util.HashMap;
import java.util.Map;

import Server_Group.Replica_2.ClinicServer.ClinicServerInt;

public class Replica_Manager_Config {
	static ClinicServerInt DSMS_CORBA_IMPL = null;
	static String ORB_INITIAL_PORT = "1050";
	static String HOST_NAME = "127.0.0.1";
	static Map<String, String> REQUEST_HASH_TABLE = new HashMap<String, String>();
	
	// Send this port to FE to update the primary leader port;
	static int LOCAL_FRONT_END_LISTENING_PORT = 5000;
	// This port is only for broadcast.
	static int LOCAL_BROAD_CAST_LISTENING_PORT = 5100;
	static String LOCAL_MTL_SERVER_NAME = "MTL";
	static String LOCAL_LVL_SERVER_NAME = "LVL";
	static String LOCAL_DDO_SERVER_NAME = "DDO";
	
	static HashMap<Integer, String> PORT_HOST = new HashMap<Integer, String>(){{
		put(4100, "Host_1");
		put(5100, "Host_2");
		put(6100, "Host_3");
	}};
	
}
