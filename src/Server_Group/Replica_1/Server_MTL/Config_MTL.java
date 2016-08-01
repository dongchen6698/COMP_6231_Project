package Server_Group.Replica_1.Server_MTL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import Server_Group.Replica_1.Record_Type.DoctorRecord;
import Server_Group.Replica_1.Record_Type.RecordInfo;


public class Config_MTL {
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
			put('L', new ArrayList<RecordInfo>(Arrays.asList(new RecordInfo("DR00001", new DoctorRecord("one", "Li", "Montreal", "5141234567", "Surgery", "mtl")))));
			put('Z', new ArrayList<RecordInfo>(Arrays.asList(new RecordInfo("DR00002", new DoctorRecord("two", "Zhang", "Montreal", "5142345678", "Surgery", "mtl")))));
		}
	};
	static ArrayList<RecordInfo> RECORD_LIST = null;
	static String HOST_NAME = "127.0.0.1";
	static String ORB_INITIAL_PORT = "1050";
	static String SERVER_NAME = "rp_1_mtl";
	static int LOCAL_LISTENING_PORT = 4001;
	static Logger LOGGER = null;
	static FileHandler FH = null;
	
	static int SERVER_PORT_RECORDID_ASSIGN = 7000;
	static int SERVER_PORT_MTL = 4001;
	static int SERVER_PORT_LVL = 4002;
	static int SERVER_PORT_DDO = 4003;

}
