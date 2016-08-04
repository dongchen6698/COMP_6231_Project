package Server_Group.Replica_2.UDP_Replica_Manager;

import java.util.HashMap;
import java.util.Map;

import Server_Group.Replica_2.ClinicServer.ClinicServerInt;

public class Replica_Manager_Config {
	static ClinicServerInt DSMS_CORBA_IMPL = null;
	static String ORB_INITIAL_PORT = "1050";
	static String HOST_NAME = "127.0.0.1";
	static Map<String, String> REQUEST_HASH_TABLE = new HashMap<String, String>();
	
	static int FE_LISTENING_PORT = 3500;
	// Send this port to FE to update the primary leader port;
	static int LOCAL_FRONT_END_LISTENING_PORT = 5000;
	// This port is only for broadcast.
	public static int LOCAL_BROAD_CAST_LISTENING_PORT = 5100;
	// This port is for failure detection.
	public static int LOCAL_FAILURE_DETECTION_PORT = 5500;
	static String LOCAL_MTL_SERVER_NAME = "MTL";
	static String LOCAL_LVL_SERVER_NAME = "LVL";
	static String LOCAL_DDO_SERVER_NAME = "DDO";

	static HashMap<Integer, String> BROADCAST_PORT = new HashMap<Integer, String>(){{
		put(4100, "Host_1");
		put(5100, "Host_2");
		put(6100, "Host_3");
	}};
}
