package Server_Group.Replica_1.Server_MTL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Map;

import Server_Group.Replica_1.Record_Type.DoctorRecord;
import Server_Group.Replica_1.Record_Type.NurseRecord;
import Server_Group.Replica_1.Record_Type.RecordInfo;
import Server_Group.Replica_1.Server_DDO.Server_DDO_Config;


public class Clinic_MTL_UDP_Listener implements Runnable{
	
	/**
	 * Default constructor.
	 */
	public Clinic_MTL_UDP_Listener() {
		
	}
	
	@Override
	public void run() {
		DatagramSocket socket = null;
		try{
			socket = new DatagramSocket(Server_MTL_Config.LOCAL_LISTENING_PORT);
			while(true){
				byte[] buffer = new byte[1000]; 
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				Server_MTL_Config.LOGGER.info("Get request: " + (new String(request.getData()).trim())+ "\n" + "Start a new thread to handle this.");
				
				
				new Connection(socket, request);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(socket != null) socket.close();
		}		
	}
	
	/**
	 * New thread to handle the newly request
	 * @author AlexChen
	 *
	 */
	static class Connection extends Thread{
		DatagramSocket socket = null;
		DatagramPacket request = null;
		String result = null;
		public Connection(DatagramSocket n_socket, DatagramPacket n_request) {
			this.socket = n_socket;
			this.request = n_request;
			String requestcode = new String(request.getData()).trim().substring(0, 3);
			switch (requestcode) {
			case "002":
				Server_MTL_Config.LOGGER.info("Request code: " + requestcode + ", " + "Search HashMap, SearchType: " + (new String(request.getData()).trim().substring(4)));
				result = getLocalHashSize(new String(request.getData()).trim().substring(4));
				break;
			case "003":
				Server_MTL_Config.LOGGER.info("Request code: " + requestcode + ", " + "Transfer Record, Record: " + (new String(request.getData()).trim().substring(4)));
				result = insertRecordInLocalHashMap(new String(request.getData()).trim().substring(4));
				break;
			}
			this.start();
		}
		
		@Override
		public void run() {
			try {
				DatagramPacket reply = new DatagramPacket(result.getBytes(),result.getBytes().length, request.getAddress(), request.getPort()); 
				socket.send(reply);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Check local hash table size and return the value.
	 * @param recordType
	 * @return
	 */
	public static synchronized String getLocalHashSize(String recordType){
		int dr_num = 0;
		int nr_num = 0;
		
		for(Map.Entry<Character, ArrayList<RecordInfo>> entry:Server_MTL_Config.HASH_TABLE.entrySet()){
			for(RecordInfo record:entry.getValue()){
				switch(record.getRecordID().substring(0, 2)){
				case "DR":
					dr_num++;
					break;
				case "NR":
					nr_num++;
					break;
				}
			}
		}
		if(recordType.equalsIgnoreCase("dr")){
			return "MTL "+"DR: "+dr_num;
		}else if(recordType.equalsIgnoreCase("nr")){
			return "MTL "+"NR: "+nr_num;
		}else{
			return "MTL "+"ALL: "+(dr_num+nr_num);
		}
	}
	
	/**
	 * Input new record which transfered from other server into local hash map
	 * @param recordInfo
	 * @return
	 */
	public static synchronized String insertRecordInLocalHashMap(String recordInfo){
		String[] record = recordInfo.split("\n");
		if(record[0].contains("DR")){
			if(Server_MTL_Config.HASH_TABLE.containsKey(record[2].split(": ")[1].charAt(0))){
				Server_MTL_Config.RECORD_LIST = Server_MTL_Config.HASH_TABLE.get(record[2].split(": ")[1].charAt(0));
			}else{
				Server_MTL_Config.RECORD_LIST = new ArrayList<RecordInfo>();
			}
			Server_MTL_Config.RECORD_LIST.add(new RecordInfo("DR"+Integer.toString(Server_MTL_Config.RECORD_ID++), new DoctorRecord(record[1].split(": ")[1], record[2].split(": ")[1], record[3].split(": ")[1], record[4].split(": ")[1], record[5].split(": ")[1], record[6].split(": ")[1])));
			Server_MTL_Config.HASH_TABLE.put(record[2].split(": ")[1].charAt(0), Server_MTL_Config.RECORD_LIST);
			return "Transfer doctor record success.";
		}else if(record[0].contains("NR")){
			if(Server_MTL_Config.HASH_TABLE.containsKey(record[2].split(": ")[1].charAt(0))){
				Server_MTL_Config.RECORD_LIST = Server_MTL_Config.HASH_TABLE.get(record[2].split(": ")[1].charAt(0));
			}else{
				Server_MTL_Config.RECORD_LIST = new ArrayList<RecordInfo>();
			}
			Server_MTL_Config.RECORD_LIST.add(new RecordInfo("NR"+Integer.toString(Server_MTL_Config.RECORD_ID++), new NurseRecord(record[1].split(": ")[1], record[2].split(": ")[1], record[3].split(": ")[1], record[4].split(": ")[1], record[5].split(": ")[1])));
			Server_MTL_Config.HASH_TABLE.put(record[2].split(": ")[1].charAt(0), Server_MTL_Config.RECORD_LIST);
			return "Transfer nurse record success.";
		}
		return "Transfer record fail.";
	}
}
