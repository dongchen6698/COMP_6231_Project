package Server_Group.Replica_3.UDP_Replica_Manager;

import java.util.HashMap;
import java.util.Map;

import Server_Group.Replica_1.DSMS_CORBA.DSMS;

public class Replica_Manager_Config {
	static DSMS DSMS_CORBA_IMPL = null;
	static String ORB_INITIAL_PORT = "1050";
	static String HOST_NAME = "127.0.0.1";
	static Map<String, String> REQUEST_HASH_TABLE = new HashMap<String, String>();
	
	static int FE_LISTENING_PORT = 3500;
	// Send this port to FE to update the primary leader port;
	static int LOCAL_FRONT_END_LISTENING_PORT = 6000;
	// This port is only for broadcast.
	static int LOCAL_BROAD_CAST_LISTENING_PORT = 6100;
	// This port is for failure detection.
	static int LOCAL_FAILURE_DETECTION_PORT = 6500;
	static String LOCAL_MTL_SERVER_NAME = "host_3";
	static String LOCAL_LVL_SERVER_NAME = "host_3";
	static String LOCAL_DDO_SERVER_NAME = "host_3";

	static HashMap<Integer, String> MAIN_LISTENING_PORT = new HashMap<Integer, String>(){{
		put(4000, "Host_1");
		put(5000, "Host_2");
		put(6000, "Host_3");
	}};
	
	static HashMap<Integer, String> BROADCAST_PORT = new HashMap<Integer, String>(){{
		put(4100, "Host_1");
		put(5100, "Host_2");
		put(6100, "Host_3");
	}};
	
	static HashMap<Integer, String> FAILURE_DETECTION_PORT = new HashMap<Integer, String>(){{
		put(4500, "Host_1");
		put(5500, "Host_2");
		put(6500, "Host_3");
	}};
	
}
