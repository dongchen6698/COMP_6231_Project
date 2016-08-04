package Server_Group.Replica_3.UDP_Replica_Manager;

import Server_Group.Replica_3.com.ClinicTasks;
import Server_Group.Replica_3.com.ClinicTasksHelper;
import Server_Group.Replica_3.com.SuperRecord;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import java.io.IOException;
import java.net.*;

/**
 * Created by Mahdiye on 8/1/2016.
 */
public class ReplicaManager{

    String[] args = null;
    String result = null;

    public ReplicaManager(String[] args) {
        this.args = args;
//        FailureDetection failureDetector = new FailureDetection();
//        failureDetector.replyToPing(Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT);
        open_UDP_Listener();
    }

    public static void main(String[] args) {
        ReplicaManager clinicClient = new ReplicaManager(args);
    }

    public void open_UDP_Listener() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT);
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);
                new Connection(socket, request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }

    public ClinicTasks selectProperClinicServer(String serverPrifix) {
        ClinicTasks server = null;
        ORB orb = ORB.init(args, null);
        Object objRef = null;
        NamingContextExt ncRef = null;
        try {
            objRef = orb.resolve_initial_references("NameService");
            ncRef = NamingContextExtHelper.narrow(objRef);
            if (serverPrifix.equals("MTL")) {
                server = (ClinicTasks) ClinicTasksHelper.narrow(ncRef.resolve_str("mtl"));
            } else if (serverPrifix.equals("LVL")) {
                server = (ClinicTasks) ClinicTasksHelper.narrow(ncRef.resolve_str("lvl"));
            } else if (serverPrifix.equals("DDO")) {
                server = (ClinicTasks) ClinicTasksHelper.narrow(ncRef.resolve_str("ddo"));
            }
        } catch (InvalidName invalidName) {
            invalidName.printStackTrace();
        } catch (NotFound notFound) {
            notFound.printStackTrace();
        } catch (CannotProceed cannotProceed) {
            cannotProceed.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName invalidName) {
            invalidName.printStackTrace();
        }
        return server;
    }


    class Connection extends Thread {

        ClinicTasks clinicServer = null;
        DatagramSocket socket = null;
        DatagramPacket request = null;

        public Connection(DatagramSocket n_socket, DatagramPacket n_request) {
            this.socket = n_socket;
            this.request = n_request;

            String request_ID = new String(request.getData()).trim().split("\n")[0];
            String function_ID = new String(request.getData()).trim().split("\n")[1];
            String manager_ID = new String(request.getData()).trim().split("\n")[2];
            String serverName = manager_ID.substring(0, 3);
            clinicServer = selectProperClinicServer(serverName);

            switch (function_ID) {
                case "001":
                    String firstName_1 = new String(request.getData()).trim().split("\n")[3];
                    String lastName_1 = new String(request.getData()).trim().split("\n")[4];
                    String address_1 = new String(request.getData()).trim().split("\n")[5];
                    String phone_1 = new String(request.getData()).trim().split("\n")[6];
                    String specialization_1 = new String(request.getData()).trim().split("\n")[7];
                    String location_1 = new String(request.getData()).trim().split("\n")[8];

                    SuperRecord drRecord = buildDRecord(manager_ID, firstName_1, lastName_1, address_1, phone_1, specialization_1, location_1);
                    result = clinicServer.createRecord(manager_ID, drRecord);
                    break;
                case "002":
                    String firstName_2 = new String(request.getData()).trim().split("\n")[3];
                    String lastName_2 = new String(request.getData()).trim().split("\n")[4];
                    String designation_2 = new String(request.getData()).trim().split("\n")[5];
                    String status_2 = new String(request.getData()).trim().split("\n")[6];
                    String statusDate_2 = new String(request.getData()).trim().split("\n")[7];

                    SuperRecord nurseRecord = buildNRecord(manager_ID, firstName_2, lastName_2, designation_2, status_2, statusDate_2);
                    result = clinicServer.createRecord(manager_ID, nurseRecord);
                    break;
                case "003":
                    String recordType_3 = new String(request.getData()).trim().split("\n")[3];
                    String countResult = "";

                    if (serverName.equals("MTL")) {
                        result = clinicServer.getRecordCounts(manager_ID, recordType_3, 6790, 6791);
                    } else if (serverName.equals("LVL")) {
                        result = clinicServer.getRecordCounts(manager_ID, recordType_3, 6789, 6791);
                    } else if (serverName.equals("DDO")) {
                        result = clinicServer.getRecordCounts(manager_ID, recordType_3, 6789, 6790);
                    }
                    break;
                case "004":
                    String recordID_4 = new String(request.getData()).trim().split("\n")[3];
                    String fieldName_4 = new String(request.getData()).trim().split("\n")[4];
                    String newValue_4 = new String(request.getData()).trim().split("\n")[5];

                    result = clinicServer.editRecord(manager_ID, recordID_4, fieldName_4, newValue_4);
                    break;
                case "005":
                    String recordID_5 = new String(request.getData()).trim().split("\n")[3];
                    String remoteClinicServerName_5 = new String(request.getData()).trim().split("\n")[4];

                    result = clinicServer.transferRecord(manager_ID, recordID_5, remoteClinicServerName_5);
                    break;
            }
            this.start();
        }

        @Override
        public void run() {
            try {
                DatagramPacket reply = new DatagramPacket(result.getBytes(), result.getBytes().length, request.getAddress(), request.getPort());
                socket.send(reply);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SuperRecord buildDRecord(String managerId, String firstName_1, String lastName_1, String address_1, String phone_1, String specialization_1, String location_1) {
        SuperRecord doctorRecord = new SuperRecord();
        doctorRecord.recordID = "-";
        doctorRecord.recType = "DR";
        doctorRecord.managerID = managerId;
        doctorRecord.statusDate = "-";
        doctorRecord.firstName = firstName_1;
        doctorRecord.lastName = lastName_1;
        doctorRecord.address = address_1;
        doctorRecord.phoneNumber = phone_1;
        if (specialization_1.equalsIgnoreCase("Surgeon")) {
            doctorRecord.specialization = 0;
        } else if (specialization_1.equalsIgnoreCase("Orthopaedic")) {
            doctorRecord.specialization = 1;
        } else if (specialization_1.equalsIgnoreCase("Paediatric")) {
            doctorRecord.specialization = 2;
        } else if (specialization_1.equalsIgnoreCase("Optometrist")) {
            doctorRecord.specialization = 3;
        }

        if (location_1.equals("mtl")) {
            doctorRecord.location = 1;
        } else if (location_1.equals("lvl")) {
            doctorRecord.location = 2;
        } else if (location_1.equals("ddo")) {
            doctorRecord.location = 3;
        }

        return doctorRecord;
    }

    public SuperRecord buildNRecord(String managerId, String firstName_2, String lastName_2, String designation_2, String status_2, String statusDate_2) {
        SuperRecord nurseRecord = new SuperRecord();
        nurseRecord.recType = "NR";
        nurseRecord.managerID = managerId;
        nurseRecord.recordID = "-";
        nurseRecord.address = "-";
        nurseRecord.phoneNumber = "-";
        nurseRecord.firstName = firstName_2;
        nurseRecord.lastName = lastName_2;
        if (designation_2.equalsIgnoreCase("junior")) {
            nurseRecord.designation = 1;
        } else if (designation_2.equalsIgnoreCase("senior")) {
            nurseRecord.designation = 1;
        }
        if (status_2.equalsIgnoreCase("active")) {
            nurseRecord.status = 1;
        } else if (status_2.equalsIgnoreCase("terminated")) {
            nurseRecord.status = 0;
        }
        nurseRecord.statusDate = statusDate_2;
        return nurseRecord;
    }


    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

}
