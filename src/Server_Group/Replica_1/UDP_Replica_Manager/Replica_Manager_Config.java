package Server_Group.Replica_1.UDP_Replica_Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Server_Group.Replica_1.DSMS_CORBA.DSMS;

public class Replica_Manager_Config {
    static DSMS DSMS_CORBA_IMPL = null;
    static String ORB_INITIAL_PORT = "1050";
    public static String HOST_NAME = "127.0.0.1";
    static Map<String, String> REQUEST_HASH_TABLE = new HashMap<String, String>();

    static int FE_LISTENING_PORT = 3500;
    // Send this port to FE to update the primary leader port;
    public static int LOCAL_FRONT_END_LISTENING_PORT = 4000;
    // This port is only for broadcast.
    public static int LOCAL_BROAD_CAST_LISTENING_PORT = 4100;
    static String LOCAL_MTL_SERVER_NAME = "host_1_mtl";
    static String LOCAL_LVL_SERVER_NAME = "host_1_lvl";
    static String LOCAL_DDO_SERVER_NAME = "host_1_ddo";


    static HashMap<Integer, String> BROADCAST_PORT = new HashMap<Integer, String>() {{
        put(4100, "Host_1");
        put(5100, "Host_2");
        put(6100, "Host_3");
    }};


    final static int n = 2;
    final static int REPLICA[] = {1, 2, 3};
    final static int priority[] = {4000, 5000, 6000};
    final static int TIMEOUT = 120000;
    final static int INTERVAL = 60000;
    final static int INITIALDELAY = 150000;

    static Map<String, Integer> FIFO_HASH_TABLE = new HashMap<String, Integer>();

}
