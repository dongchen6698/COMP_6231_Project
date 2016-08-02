package Server_Group.Replica_2.MTL;

import java.text.SimpleDateFormat;
import java.util.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.*;

import Server_Group.Replica_2.ClinicServer.*;

import org.omg.CORBA.*;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;


import Server_Group.Replica_2.ClinicServer.ClinicServerIntPOA;

	public class ClinicServerMTL extends ClinicServerIntPOA implements Runnable{
		private ORB orb;
		//Key: Fist Letter of Last Name,
		private HashMap<String, List<String>> ReferenceTable = new HashMap<String, List<String>>();
		//Key: ID string
		private HashMap<String, List<String>> DetailTable = new HashMap<String, List<String>>();
		//Key: ID string, Value: First Letter of last name
		private HashMap<String,String> IndexTable = new HashMap<String, String>();
		//To create ID for nurse and doctor
		private Integer countDoctor = 0;
		private Integer countNurse = 0;
		private Integer lastDoctorKey = 10000;
		private Integer lastNurseKey = 10000;
		private Integer MTLPort = 2020;
		private Integer MTL_UDPPort = 1234;
		private Integer LVL_UDPPort = 1235;
		private Integer DDO_UDPPort = 1236;
		private String[] args;
		private Thread t;

		public ClinicServerMTL(String[] arg)
		{
			this.args = arg;
		}

		public void run() {

		    try {
		    	Properties props = new Properties();
		    	props.put("org.omg.CORBA.ORBInitialPort", "1050");
		    	
				ORB orb = ORB.init(args, props);
				POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
				rootPOA.the_POAManager().activate();
				
				setORB(orb);
				
				org.omg.CORBA.Object ref = rootPOA.servant_to_reference(this);
				ClinicServerInt href = ClinicServerIntHelper.narrow(ref);
				
				org.omg.CORBA.Object OBJref = orb.resolve_initial_references("NameService");
				NamingContextExt ncRef = NamingContextExtHelper.narrow(OBJref);
				
				NameComponent Path[] = ncRef.to_name("MTL");
				ncRef.rebind(Path, href);
				listener();
				System.out.println("MTL server is running...");
				
				for(;;){
					orb.run();
				}
				
			}
			catch(Exception e)
			{
				System.out.println("Error: " + e);
				e.printStackTrace(System.out);
			}

		}
		public void start() {

		    System.out.println("Thread MTL Starting!");
		    if (t == null)
		        t = new Thread(this, "MTL");
		    t.start();

		}
		
		public void setORB(ORB orb_val)
		{
			orb = orb_val;
		}
		
		public String createDRecord (String managerID, String firstName, String lastName, String address, String phone, String specialization, String location)
		{
			try{
				List<String> list = new ArrayList<String>();
				list.add(firstName);
				list.add(lastName);
				list.add(address);
				list.add(phone);
				list.add(specialization);
				list.add(location);
				String RecordID = "DR" + lastDoctorKey;
				String Key = lastName.substring(0, 1);
				synchronized(this)
				{
					if(ReferenceTable.containsKey(Key)){
						ReferenceTable.get(Key).add(RecordID);
					}else{
						List<String> tempList = new ArrayList<String>();
						tempList.add(RecordID);
						ReferenceTable.put(Key, tempList);
					}

					DetailTable.put(RecordID, list);
					IndexTable.put(RecordID, Key);					
				}

				countDoctor++;
				lastDoctorKey++;
				ServerLog("add", "Doctor: "+lastName+" with RecordD: "+ RecordID + " has been added.");
				return "Succeed: " + RecordID + " has been added to list";
			}catch(Exception e){
				ServerLog("Fail: Create Doctor",e.toString());
				return "Fail: Create Doctor ||"+ e.toString();
			}
		}

		public String createNRecord (String managerID, String firstName, String lastName, String designation, String status,String statusDate)
		{
			try{
				List<String> list = new ArrayList<String>();
				list.add(firstName);
				list.add(lastName);
				list.add(designation);
				list.add(status);
				list.add(statusDate);
				String RecordID = "NR" + lastNurseKey;
				String Key = lastName.substring(0, 1);
				synchronized(this){
					if(ReferenceTable.containsKey(Key)){
						ReferenceTable.get(Key).add(RecordID);
					}else{
						List<String> tempList = new ArrayList<String>();
						tempList.add(RecordID);
						ReferenceTable.put(Key, tempList);
					}

					DetailTable.put(RecordID, list);
					IndexTable.put(RecordID, Key);
					countNurse++;
					lastNurseKey++;
					
				}

				ServerLog("add", "Nurse "+lastName+" with RecordD "+ RecordID + " has been added.");
				return "Succeed " + RecordID + " has been added to list";
			}catch(Exception e){
				ServerLog("Fail: Create Nurse",e.toString());
				return "Fail: Create Nurse ||"+ e.toString();
			}
		}

		public String getRecordCounts (String managerID, String recordType)
		{
			String count;
			switch (recordType.toUpperCase()){
			case "DR":
				count = ""+countDoctor;
				break;
			case "NR":
				count = ""+countNurse;
				break;
			default:
				count = "";
				break;
			}
			ServerLog("getRecordCount","Server sends the request to other servers to get "+recordType+" field count:" + count);
			return "MTL " + count + " ," + sendReq("get","LVL","","",recordType) + " ," + sendReq("get","DDO","","",recordType);
		}
		
		public String editRecord (String managerID, String recordID, String fieldName, String newValue)
		{
			if(IndexTable.containsKey(recordID)){
				String type = recordID.substring(0, 2);
				int index = validation(type, fieldName, newValue);
				if(index != 0){					
					try{
						synchronized(recordID){
							List<String> list = DetailTable.get(recordID);
							String temp = list.get(index);
							list.set(index, newValue);
							ServerLog("editRecord", "Succeed: record: " + recordID + " field: " + fieldName + " previous value: " + temp + " new value:" + newValue);
							return "Succeed: The Field has been updated.";							
						}
					}catch(Exception e){
						ServerLog("Fail: Edit Record","Record: " + recordID + " field: " + fieldName + " new value:" + newValue+ " || "+ e.toString());
						return "Fail: Edit Record, "+ recordID + " field: " + fieldName + " new value:" + newValue+ " || "+ e.toString();
					}
				}else{
					ServerLog("editRecord", "Fail: "+recordID+": The new value was not correct." + "\"" + newValue + "\"");
					return "Fail: The new value was not correct.";
				}
			}else{
				ServerLog("editRecord", "Fail: The user with recordID: " + recordID + "does not exist");
				return "Fail: The user with recordID: " + recordID + "does not exist";
			}
		}
		
		public String transferRecord(String managerID,String recordID, String remoteClinicServerName){
			if(DetailTable.containsKey(recordID))
			{
				try{
					synchronized(recordID){
						String type = recordID.substring(0, 2);
						String result = sendReq("trs", remoteClinicServerName, managerID, recordID, type);
						ServerLog("transferRecord", result);			
						return result;															
					}
				}catch(Exception e){
					return "Fail: Transfer "+e.getMessage();
				}
			}else{
				String result = "The record for "+recordID+" does not exist in MTL Server";
				ServerLog("transferRecord", result);			
				return result;
			}
		}
		
		private int validation(String type, String field, String newValue){
			ServerLog("Validation","Type: " + type + " Field: " + field + " New Value:" + newValue);
			int result = 0;
			switch (type.toUpperCase()){
				case "DR":
					switch (field.toLowerCase()){
						case "address":
							result = 2;
							break;
						case "phone":
							result = (newValue.matches("^+?\\d+$")) ? 3 : 0;
							break;
						case "location":
							switch(newValue.toUpperCase().trim()){
							case "MTL":
							case "LVL":
							case "DDO":
								result = 5;
								break;
							default:
								break;
							}
							break;
					}
					break;
				case "NR":
					switch(field.toLowerCase()){
						case "designation":
							switch(newValue.toLowerCase().trim()){
							case "senior":
							case "junior":
								result = 2;
								break;
							default:
								break;
							}
							break;
						case "status":
							switch(newValue.toLowerCase().trim()){
							case "active":
							case "terminated":
								result = 3;
								break;
							default:
								break;
							}
							break;
						case "statusdate":
						case "status date":
							result = 4;
							break;
					}
					break;
			}
			String resultTemp = (result!=0)?"True":"False";
			ServerLog("Validation","The resualt of validation:" + resultTemp);
			return result;
		}
		
		private void ServerLog(String Method, String Description)
		{
			Date date = new Date();

			try(FileWriter fw = new FileWriter("Server_Side_Log/host_2_mtl.txt", true);
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
				{
					SimpleDateFormat DF = new SimpleDateFormat("[ss:mm:hh dd-MM-YYYY]"); 
				    out.println(DF.format(date) + " Method: " + Method +" -> " + Description);
				} catch (IOException e) {
				    e.printStackTrace();
				}
		}
		
		private String sendReq(String type, String server, String managerID, String recordID, String RecordType)
		{
			int port = 0000;
			String result;
			
			switch(server){
			case "LVL":
				port = LVL_UDPPort;
				break;
			case "DDO":
				port = DDO_UDPPort;
				break;
			}

			DatagramSocket aSocket = null;
			switch(type){
			case "get":
				try{
					ServerLog("sendReq : get count :","Server sends a request to server "+server+" on the port " + port);
					aSocket = new DatagramSocket();
					String RawMessage = type +
										"MTL" + 
										RecordType;
					byte[] message = RawMessage.getBytes();
					InetAddress aHost =InetAddress.getByName("localhost");
					
					DatagramPacket request = new DatagramPacket(message, message.length, aHost, port);
					aSocket.send(request);
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					ServerLog("sendReq : get count :","Server is waiting for a response");
					aSocket.receive(reply);
					ServerLog("sendReq : get count :","Server received a reply");
					result = server + " " + new String(reply.getData()).substring(0, reply.getLength());
					
				}catch(SocketException e){result = "Fail: Socket: " + e.getMessage();
				}catch(IOException e){result = "Fail: IO: " + e.getMessage();
				}finally{if(aSocket != null) aSocket.close();}
				ServerLog("sendReq : get count :","Result: "+result);
				return result;				
			case "trs":
				try{
					ServerLog("sendReq : transfer :","Server sends a request to server "+server+" on the port " + port);
					aSocket = new DatagramSocket();
					String RawMessage = type + // Message Type
										"MTL" + // Sender Server
										RecordType + // Record Type
										managerID + // Manager ID
										DetailTable.get(recordID); // RecordID
					byte[] message = RawMessage.getBytes();
					InetAddress aHost =InetAddress.getByName("localhost");
					
					DatagramPacket request = new DatagramPacket(message, message.length, aHost, port);
					aSocket.send(request);
					byte[] buffer = new byte[1000];
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					ServerLog("sendReq : transfer :","Server is waiting for a response");
					aSocket.receive(reply);
					result = server + " " + new String(reply.getData()).substring(0, reply.getLength());
					ServerLog("sendReq : transfer :","Server received a reply"+reply);
					try{
						List<String> list =DetailTable.get(recordID);
						String Key = list.get(1).substring(0, 1);
						ReferenceTable.get(Key).remove(recordID);
						DetailTable.remove(recordID);
						IndexTable.remove(recordID);
						switch(RecordType){
						case "DR":
							countDoctor--;
							break;
						case "NR":
							countNurse--;
							break;
						}
					}
					catch(Exception e)
					{
						result = "Fail: Remove from hashmap"+e.getMessage();
					}
					
				}catch(SocketException e){result = "Fail: Socket: " + e.getMessage();
				}catch(IOException e){result = "Fail: IO: " + e.getMessage();
				}finally{if(aSocket != null) aSocket.close();}
				ServerLog("sendReq","Result: "+result);
				return result;
			default:
				ServerLog("sendReq","Wrong type");
				return "Fail: Wrong type";
			}
		}
		
		private void listener(){
			DatagramSocket aSocket = null;
			try{
				aSocket = new DatagramSocket(MTL_UDPPort);
				byte[] buffer = new byte[1000];
				while(true){
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
					aSocket.receive(request);
					
					String type = new String(request.getData()).substring(0, 3);
					String senderServer = new String(request.getData()).substring(3, 6);
					String recordType = new String(request.getData()).substring(6, 8).toUpperCase();
					DatagramPacket reply = null;
					
					switch(type){
					case "get":
						ServerLog("Listener", "A request received from server " + 
								senderServer+
								" for "+ 
								new String(request.getData()).substring(6, request.getLength()));
								
						Integer totalCount = 0;
						switch(recordType){
						case "DR":
							totalCount = countDoctor;
							break;
						case "NR":
							totalCount = countNurse;
							break;
						default:
							totalCount = 0;
							break;
						}
						reply = new DatagramPacket(totalCount.toString().getBytes(), 
													totalCount.toString().length(),
													request.getAddress(),
													request.getPort());
						break;
					case "trs":
						String managerID = new String(request.getData()).substring(8, 15);
						ServerLog("Listener", "A request received from server " + 
								senderServer+
								" ManagerID:"+
								managerID+
								" for transfer record "+
								new String(request.getData()).substring(15, request.getLength()));
						String requestString = new String(request.getData()).substring(15, request.getLength());
						List<String> list = converter(recordType,requestString);
						String result = "";
						try{
							switch(recordType){
							case "DR":
								result = this.createDRecord(managerID, list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5));								
								break;
							case "NR":
								result = this.createNRecord(managerID, list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));																
								break;
							default:
								result = "Fail: wrong record type :" + recordType+":";
								break;
							}
						}
						catch(Exception e)
						{
							result = "Fail:"+e.getStackTrace();
						}
						reply = new DatagramPacket(result.getBytes(), 
													result.length(),
													request.getAddress(),
													request.getPort());
						break;
					default:
						String ErrorMessage = "The request type unknown";
						reply = new DatagramPacket(ErrorMessage.getBytes(), 
								ErrorMessage.length(),
								request.getAddress(),
								request.getPort());
						break;
					}
					
					aSocket.send(reply);
					ServerLog("Listener", "Reply to request from server " + new String(request.getData()).substring(0, request.getLength()));
				}
			}catch(SocketException e){System.out.println("Socket: " + e.getMessage());
			}catch(IOException e){System.out.println("IO: " + e.getMessage());
			}finally{if(aSocket != null)aSocket.close();}
		}
		
		private List<String> converter(String recordType, String stringList)
		{
			int from = stringList.indexOf("[")+1;
			int end = stringList.indexOf("]");
			
			List<String> list = new ArrayList<String>();
			List<Integer> arg = new ArrayList<Integer>();
			for (int i = -1; (i = stringList.indexOf(",", i + 1)) != -1; ) {
				arg.add(i);
			}

			for(int i=0;i < arg.size(); i++){
				int next = arg.get(i);
				list.add(stringList.substring(from, next));
				from = next + 2;
			}
			switch(recordType){
			case "DR":
				list.add("MTL");
				break;
			case "NR":
				list.add(stringList.substring(from, end));
				break;
			}
			return list;
		}

	}
