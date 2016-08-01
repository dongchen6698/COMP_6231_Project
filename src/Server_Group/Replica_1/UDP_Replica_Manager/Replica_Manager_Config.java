package Server_Group.Replica_1.UDP_Replica_Manager;

import Server_Group.Replica_1.DSMS_CORBA.DSMS;

public class Replica_Manager_Config {
	static DSMS DSMS_CORBA_IMPL = null;
	static String ORB_INITIAL_PORT = "1050";
	static String HOST_NAME = "127.0.0.1";
	
	static int LOCAL_FRONT_END_LISTENING_PORT = 4000;
	static int LOCAL_BROAD_CAST_LISTENING_PORT = 4100;
	static String LOCAL_MTL_SERVER_NAME = "rp_1_mtl";
	static String LOCAL_LVL_SERVER_NAME = "rp_1_lvl";
	static String LOCAL_DDO_SERVER_NAME = "rp_1_ddo";
	
//	static int REPLICA_1_FRONT_END_LISTENING_PORT = 4000;
//	static int REPLICA_1_BROAD_CAST_LISTENING_PORT = 4100;
//	static String REPLICA_1_MTL_SERVER_NAME = "rp_1_mtl";
//	static String REPLICA_1_LVL_SERVER_NAME = "rp_1_lvl";
//	static String REPLICA_1_DDO_SERVER_NAME = "rp_1_ddo";
//	
//	static int REPLICA_2_FRONT_END_LISTENING_PORT = 5000;
//	static int REPLICA_2_BROAD_CAST_LISTENING_PORT = 5100;
//	static String REPLICA_2_MTL_SERVER_NAME = "XXX";
//	static String REPLICA_2_LVL_SERVER_NAME = "XXX";
//	static String REPLICA_2_DDO_SERVER_NAME = "XXX";
//	
//	static int REPLICA_3_FRONT_END_LISTENING_PORT = 6000;
//	static int REPLICA_3_BROAD_CAST_LISTENING_PORT = 6100;
//	static String REPLICA_3_MTL_SERVER_NAME = "XXX";
//	static String REPLICA_3_LVL_SERVER_NAME = "XXX";
//	static String REPLICA_3_DDO_SERVER_NAME = "XXX";
}
