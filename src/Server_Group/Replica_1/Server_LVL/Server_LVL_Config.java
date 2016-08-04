package Server_Group.Replica_1.Server_LVL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import Server_Group.Replica_1.Record_Type.NurseRecord;
import Server_Group.Replica_1.Record_Type.RecordInfo;


public class Server_LVL_Config {
	static enum D_LOCATION{
		mtl,lvl,ddo
	}	
	static enum N_DESIGNATION{
		junior,senior
	}
	static enum N_STATUS{
		active,terminated
	}
	static Map<Character, ArrayList<RecordInfo>> HASH_TABLE = new HashMap<Character, ArrayList<RecordInfo>>(){
		{
			put('W', new ArrayList<RecordInfo>(Arrays.asList(new RecordInfo("NR00003", new NurseRecord("seven", "Wang", "junior", "active", "2005/09/12")))));
			put('H', new ArrayList<RecordInfo>(Arrays.asList(new RecordInfo("NR00004", new NurseRecord("eight", "Hu", "junior", "active", "2016/09/12")))));
		}
	};
	static int RECORD_ID = 10000;
	static ArrayList<RecordInfo> RECORD_LIST = null;
	static String HOST_NAME = "127.0.0.1";
	static String ORB_INITIAL_PORT = "1050";
	static String SERVER_NAME = "host_1_lvl";
	static int LOCAL_LISTENING_PORT = 4002;
	static Logger LOGGER = null;
	static FileHandler FH = null;
	
	static int SERVER_PORT_RECORDID_ASSIGN = 4500;
	static int SERVER_PORT_MTL = 4001;
	static int SERVER_PORT_LVL = 4002;
	static int SERVER_PORT_DDO = 4003;
}
