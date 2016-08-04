package Server_Group.Replica_1.Server_LVL;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.omg.CORBA.ORB;

import Server_Group.Replica_1.DSMS_CORBA.DSMSPOA;
import Server_Group.Replica_1.Record_Type.DoctorRecord;
import Server_Group.Replica_1.Record_Type.NurseRecord;
import Server_Group.Replica_1.Record_Type.RecordInfo;


public class Clinic_LVL_Impl extends DSMSPOA{

	private ORB orb;
	private int recordID = 10000;
	/**
	 * Set ORB function
	 * @param orb_val
	 */
	public void setORB(ORB orb_val) {
	    this.orb = orb_val;
	}
	
	@Override
	public String createDRecord(String managerId, String firstName, String lastName, String address, String phone,
			String specialization, String location) {
		if(!checkLocation(location)){
			return "Location is not right. Please input (mtl,lvl or ddo).\n";
		}
		Character capital_lastname = lastName.charAt(0);
		if(Server_LVL_Config.HASH_TABLE.containsKey(capital_lastname)){
			Server_LVL_Config.RECORD_LIST = Server_LVL_Config.HASH_TABLE.get(capital_lastname);
		}else{
			Server_LVL_Config.RECORD_LIST = new ArrayList<RecordInfo>();
		}
		DoctorRecord doc_recorde = new DoctorRecord(firstName, lastName, address, phone, specialization, location);
		String recordID = "DR" + Integer.toString(Server_LVL_Config.RECORD_ID++);
		RecordInfo doc_recorde_with_recordID = new RecordInfo(recordID, doc_recorde);
		
		synchronized (this) {
			Server_LVL_Config.RECORD_LIST.add(doc_recorde_with_recordID);
			Server_LVL_Config.HASH_TABLE.put(capital_lastname, Server_LVL_Config.RECORD_LIST);
		}
		System.out.println(Server_LVL_Config.LOGGER);
		Server_LVL_Config.LOGGER.info("Manager: "+ managerId + " Creat Doctor Record: "+ "\n" +doc_recorde_with_recordID.toString());
		return "Doctor Record Buid Succeed !" + "\n" +doc_recorde_with_recordID.toString();
	}

	@Override
	public String createNRecord(String managerId, String firstName, String lastName, String designation, String status,
			String statusDate) {
		if(!checkDesignation(designation)){
			return "Designation is not right. Please input (junior or senior).\n";
		}
		if(!checkStatus(status)){
			return "Status is not right. Please input (active or terminated).\n";
		}
		if(!checkStatusDate(statusDate)){
			return "Status Date is not right. Please input the right format of date (yyyy/mm/dd).";
		}
		Character capital_lastname = lastName.charAt(0);
		if(Server_LVL_Config.HASH_TABLE.containsKey(capital_lastname)){
			Server_LVL_Config.RECORD_LIST = Server_LVL_Config.HASH_TABLE.get(capital_lastname);
		}else{
			Server_LVL_Config.RECORD_LIST = new ArrayList<RecordInfo>();
		}
		NurseRecord nur_recorde = new NurseRecord(firstName, lastName, designation, status, statusDate);
		String recordID = "NR" + Integer.toString(Server_LVL_Config.RECORD_ID++);
		RecordInfo nur_recorde_with_recordID = new RecordInfo(recordID, nur_recorde);
		
		synchronized (this) {
			Server_LVL_Config.RECORD_LIST.add(nur_recorde_with_recordID);
			Server_LVL_Config.HASH_TABLE.put(capital_lastname, Server_LVL_Config.RECORD_LIST);
		}
		Server_LVL_Config.LOGGER.info("Manager: "+ managerId + " Creat Nurse Record: "+ "\n" +nur_recorde_with_recordID.toString());
		return "Nurse Record Buid Succeed !" + "\n" +nur_recorde_with_recordID.toString();
	}

	@Override
	public String getRecordCounts(String managerId, String recordType) {
		String lvl_hash_size = sendMessageToOtherServer(Server_LVL_Config.SERVER_PORT_MTL, recordType, "002");
		String ddo_hash_size = sendMessageToOtherServer(Server_LVL_Config.SERVER_PORT_DDO, recordType, "002");
		String mtl_hash_size = getLocalHashSize(recordType);
		String result = mtl_hash_size + "\n" + lvl_hash_size + "\n" + ddo_hash_size + "\n";
		Server_LVL_Config.LOGGER.info("Manager: "+ managerId + " search RecordCounts: "+ "\n" + result);
		return result;
	}

	@Override
	public String editRecord(String managerId, String recordID, String fieldName, String newValue) {
		for(Map.Entry<Character, ArrayList<RecordInfo>> entry:Server_LVL_Config.HASH_TABLE.entrySet()){
			for(RecordInfo record:entry.getValue()){
				if(recordID.equalsIgnoreCase(record.getRecordID())){
					if(recordID.contains("DR")||recordID.contains("dr")){
						if(fieldName.equalsIgnoreCase("Address")){
							record.getDoctorRecord().setAddress(newValue);
							Server_LVL_Config.LOGGER.info("Manager: "+ managerId + " edit the Address of Doctor Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}else if(fieldName.equalsIgnoreCase("Phone")){
							record.getDoctorRecord().setPhone(newValue);
							Server_LVL_Config.LOGGER.info("Manager: "+ managerId + " edit the phone of Doctor Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}else if (fieldName.equalsIgnoreCase("Location")){
							if(!checkLocation(newValue)){
								return "Location is not right. Please input (mtl,lvl or ddo).\n";
							}
							record.getDoctorRecord().setLocation(newValue);
							Server_LVL_Config.LOGGER.info("Manager: "+ managerId + " edit the Location of Doctor Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}
					}else if(recordID.contains("NR")||recordID.contains("nr")){
						if(fieldName.equalsIgnoreCase("Designation")){
							if(!checkDesignation(newValue)){
								return "Designation is not right. Please input (junior or senior).\n";
							}
							record.getNurseRecord().setDesignation(newValue);
							Server_LVL_Config.LOGGER.info("Manager: "+ managerId + " edit the Designation of Nurse Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}else if(fieldName.equalsIgnoreCase("Status")){
							if(!checkStatus(newValue)){
								return "Status is not right. Please input (active or terminated).\n";
							}
							record.getNurseRecord().setStatus(newValue);
							Server_LVL_Config.LOGGER.info("Manager: "+ managerId + " edit the Status of Nurse Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}else if (fieldName.equalsIgnoreCase("statusDate")){
							record.getNurseRecord().setStatusDate(newValue);
							Server_LVL_Config.LOGGER.info("Manager: "+ managerId + " edit the Status date of Nurse Record: "+ "\n" + record.toString());
							return "edit succeed !\n"+record.toString();
						}
						
					}
				}
			}
		}
		return "edit failed";
	}

	@Override
	public String transferRecord(String managerId, String recordID, String remoteClinicServerName) {
		if(!checkRecordIDExistOrNot(recordID)){
			return "RecordID is not right. Please input again.\n";
		}
		if(!remoteClinicServerName.equalsIgnoreCase("lvl")){
			if(!checkLocation(remoteClinicServerName)){
				return "Location is not right. Please input (mtl,lvl or ddo).\n";
			}
		}else{
			return "Location is not right. You can not transfer record to sever itself.";
		}
		String result = transferRecordToOtherServer(recordID, remoteClinicServerName);
		Server_LVL_Config.LOGGER.info("Manager: "+ managerId + " transfer recordID: "+ recordID + " to " + remoteClinicServerName + "success");
		return result;
	}
	
	/**
	 * Local check RecordID is right or not
	 * @param recordID
	 * @return
	 */
	public Boolean checkRecordIDExistOrNot(String recordID){
		for(Map.Entry<Character, ArrayList<RecordInfo>> entry:Server_LVL_Config.HASH_TABLE.entrySet()){
			for(RecordInfo record:entry.getValue()){
				if(recordID.equalsIgnoreCase(record.getRecordID())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Local check Location is right or not.
	 * @param location
	 * @return
	 */
	public Boolean checkLocation(String location){
		for(Server_LVL_Config.D_LOCATION d_location: Server_LVL_Config.D_LOCATION.values()){
			if(location.equals(d_location.toString())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Local check Designation is right or not.
	 * @param designation
	 * @return
	 */
	public Boolean checkDesignation(String designation){
		for(Server_LVL_Config.N_DESIGNATION n_designation: Server_LVL_Config.N_DESIGNATION.values()){
			if(designation.equals(n_designation.toString())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Local check Status is right or not.
	 * @param status
	 * @return
	 */
	public Boolean checkStatus(String status){
		for(Server_LVL_Config.N_STATUS n_status: Server_LVL_Config.N_STATUS.values()){
			if(status.equals(n_status.toString())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Local check Status Date is right or not.
	 * @param date
	 * @return
	 */
	public Boolean checkStatusDate(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		try {
			format.setLenient(false);
			format.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;	
	}
	
	/**
	 * This function is for request other 2 server for their count of specific record type.
	 * @param server_port
	 * @param recordType
	 * @return
	 * 
	 */
	public String sendMessageToOtherServer(int serverPort, String content, String requestCode){
		DatagramSocket socket = null;
		String hostname = Server_LVL_Config.HOST_NAME;
		String requestcode = requestCode;
		byte[] message = null;
		
	    try {
	    	socket = new DatagramSocket();
	    	if(content.equals("getRecordIdNumber")){
	    		message = (new String(content)).getBytes();
	    	}else{
	    		message = (new String(requestcode+"\n"+content)).getBytes();
	    	}
	    	InetAddress Host = InetAddress.getByName(hostname);
	    	DatagramPacket request = new DatagramPacket(message, message.length, Host, serverPort);
	    	socket.send(request);
	    	byte[] buffer = new byte[1000];
	    	DatagramPacket reply = new DatagramPacket(buffer, buffer.length); 
	    	socket.receive(reply);
	    	String result = new String(reply.getData()).trim();
	    	return result;
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    	}
		finally{
			if(socket != null){
				socket.close();
				}
			}
		return null; 
	}
	
	/**
	 * Transfer record to other server, based on recordID and remote Clinic Server name.
	 * @param recordID
	 * @param remoteClinicServerName
	 * @return
	 */
	public String transferRecordToOtherServer(String recordID, String remoteClinicServerName){
		int serverPort = 0;
		
		if(remoteClinicServerName.equalsIgnoreCase("mtl")){
			serverPort = Server_LVL_Config.SERVER_PORT_MTL;
		}else if(remoteClinicServerName.equalsIgnoreCase("lvl")){
			serverPort = Server_LVL_Config.SERVER_PORT_LVL;
		}else if(remoteClinicServerName.equalsIgnoreCase("ddo")){
			serverPort = Server_LVL_Config.SERVER_PORT_DDO;
		}
		
		for(Map.Entry<Character, ArrayList<RecordInfo>> entry:Server_LVL_Config.HASH_TABLE.entrySet()){
			for(RecordInfo record:entry.getValue()){
				if(recordID.equalsIgnoreCase(record.getRecordID())){
					String result = sendMessageToOtherServer(serverPort, record.toString(), "003");
					if(result.contains("success")){
						entry.getValue().remove(record);
					}
					return result;
				}
			}
		}
		return "fail";
	}
	
	/**
	 * Check local hash table size and return the value.
	 * @param recordType
	 * @return
	 */
	public synchronized String getLocalHashSize(String recordType){
		int dr_num = 0;
		int nr_num = 0;
		
		for(Map.Entry<Character, ArrayList<RecordInfo>> entry:Server_LVL_Config.HASH_TABLE.entrySet()){
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
			return "LVL "+"DR: "+dr_num;
		}else if(recordType.equalsIgnoreCase("nr")){
			return "LVL "+"NR: "+nr_num;
		}else{
			return "LVL "+"ALL: "+(dr_num+nr_num);
		}
	}
}
