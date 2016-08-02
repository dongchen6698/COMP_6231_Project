package Front_End;

import java.util.HashMap;
import java.util.Map;


public class Front_End_Config {
	// Front end CORBA information
	static String HOST_NAME = "127.0.0.1";
	static String ORB_INITIAL_PORT = "1050";
	static String SERVER_NAME = "front_end";
	
	// Request Generator IP and Port information
	static String REQUESTID_GENERATOR_IP = "127.0.0.1";
	static int REQUESTID_GENERATOR_PORT = 3000;
	

	static int LOCAL_LISTENING_PORT = 3500;
	
	// Leader Host IP and Port
	static String PRIMARY_SERVER_IP = "127.0.0.1";
	static int PRIMARY_SERVER_PORT = 5000;
	
	// Front end hash table for storing the request information in case of message lost.
	// First String is requestID, second String is request message for sending to leader host.like below
	// key:0001    value: 0001
	//   				  001
	//					  firstName
	//					  ....
	static Map<String, String> REQUEST_HASH_TABLE = new HashMap<String, String>();

}
