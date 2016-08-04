package Server_Group.Replica_3;

import com.ClinicTasks;
import com.ClinicTasksHelper;
import com.ClinicTasksPOA;
import com.SuperRecord;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Mahdiye on 7/31/2016.
 */
public class ClinicTasksServant extends ClinicTasksPOA implements Runnable {


    private String[] args;
    private Thread t;


    public ClinicTasksServant() {

    }

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public ORB getORB() {
        return orb;
    }

    @Override
    public void run() {

        try {
            System.out.println("Function run() in Clinic Server " + serverName + " is running!");
            logger.info("Function run() in Clinic Server " + serverName + " is running!");
            ORB orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            setORB(orb);
            Object ref = rootpoa.servant_to_reference(this);
            ClinicTasks href = ClinicTasksHelper.narrow(ref);
            Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            NameComponent path[] = ncRef.to_name(serverName);
            ncRef.rebind(path, href);
            remoteServerRecordCounts(udpListenerPort);
            replyToTransferRequest(udpTransferPort);
            System.out.println("Clinic Server " + serverName + " is up and working!");
            logger.info("Server " + serverName + " is up and working!");
            for (; ; ) {
                orb.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }

    }


    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String serverName;
    private int udpListenerPort;
    private int udpTransferPort;
    private int rmiPort;
    private ORB orb;

    private HashMap<Character, List<SuperRecord>> clinicHmap = new HashMap<Character, List<SuperRecord>>();

    public ClinicTasksServant(String serverName, int listenerPort, int transferPort, int rmiPort, String[] args) {
        setServerName(serverName);
        setUdpListenerPort(listenerPort);
        setUdpTransferPort(transferPort);
        setRmiPort(rmiPort);
        setArgs(args);
        setArgs(args);
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getUdpListenerPort() {
        return udpListenerPort;
    }

    public void setUdpListenerPort(int udpListenerPort) {
        this.udpListenerPort = udpListenerPort;
    }

    public int getUdpTransferPort() {
        return udpTransferPort;
    }

    public void setUdpTransferPort(int udpTransferPort) {
        this.udpTransferPort = udpTransferPort;
    }

    public int getRmiPort() {
        return rmiPort;
    }

    public void setRmiPort(int rmiPort) {
        this.rmiPort = rmiPort;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    @Override
    public String createRecord(String managerId, SuperRecord record) {

        System.out.println(serverName + " Server: is creating a record");
        logger.info(serverName + " Server: is creating a record");
        List<SuperRecord> retrievedList;
        Character criteriaChar = new Character(record.lastName.charAt(0));
        if (this.clinicHmap.containsKey(criteriaChar)) {
            retrievedList = this.clinicHmap.get(criteriaChar);
        } else {
            retrievedList = new ArrayList<>();
        }
        record.recordID = record.recType.concat(createID());
        synchronized (retrievedList) {
            retrievedList.add(record);
        }
        this.clinicHmap.put(criteriaChar, retrievedList);
        System.out.println(serverName + " Server: Record with record ID " + record.recordID + " added");
        logger.info(serverName + " Server: Record with record ID " + record.recordID + " added");
        if (record.recType.equals("DR"))
            return "Doctor Record with record id : " + record.recordID + " has been Built Successfully!";
        else
            return "Nurse Record with record id : " + record.recordID + "  has been Built Successfully!";

    }


    public int returnOwnRecordCounts(String recordType) {
        int recordCounter = 0;
        for (Map.Entry<Character, List<SuperRecord>> entry : this.clinicHmap.entrySet()) {
            List<SuperRecord> list = new ArrayList<>();
            list = entry.getValue();
            synchronized (list) {
                for (SuperRecord listElement : list) {
                    if (listElement.recType.equals(recordType))
                        recordCounter++;
                }
            }
        }
        return recordCounter;
    }

    //continuously listening to count
    public void remoteServerRecordCounts(final int portNum) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                System.out.println(serverName + ": UDP Server is listening on the port: " + portNum);
                logger.info(serverName + ": UDP Server is listening on the port: " + portNum);
                DatagramSocket aSocket = null;
                try {
                    aSocket = new DatagramSocket(portNum);
                    byte[] buffer = new byte[1000];

                    while (true) {
                        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                        aSocket.receive(request);
                        System.out.println("the present port in run is " + portNum);
                        String recordType = new String(request.getData(), "UTF-8");
                        int result = returnOwnRecordCounts(recordType.trim());
                        String response = String.valueOf(result);
                        DatagramPacket reply = new DatagramPacket(response.getBytes(), response.length(), request.getAddress(), request.getPort());
                        aSocket.send(reply);
                    }
                } catch (SocketException e) {
                    System.out.println(serverName + " Socket: " + e.getMessage());
                    logger.info(e.getMessage());
                } catch (IOException e) {
                    System.out.println(serverName + " IO: " + e.getMessage());
                    logger.info(e.getMessage());
                } finally {

                    if (aSocket != null)
                        aSocket.close();

                }
                logger.info(serverName + " UDP Server: replied");
                System.out.println(serverName + " UDP Server: replied");
            }
        });
        t.start();

    }

    //continuously listening to transfer
    public void replyToTransferRequest(final int transferPortNum) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                System.out.println(serverName + ": UDP Transfer Server is listening on the port: " + transferPortNum);
                logger.info(serverName + ": UDP Transfer Server is listening on the port: " + transferPortNum);
                DatagramSocket aSocket = null;
                try {
                    aSocket = new DatagramSocket(transferPortNum);
                    byte[] buffer = new byte[1000];
                    //returnedResponse format: DR11111OKJ
                    while (true) {
                        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                        aSocket.receive(request);
                        System.out.println(request);
                        System.out.println("the present port in run is " + transferPortNum);
                        SuperRecord receivedToken;
                        receivedToken = deserialize(request.getData());
                        byte[] response = new byte[1000];
                        createRecord(receivedToken.managerID, receivedToken);
                        response = ("OK").getBytes();
                        DatagramPacket reply = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
                        aSocket.send(reply);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    System.out.println(serverName + " Socket: " + e.getMessage());
                    logger.info(e.getMessage());
                } catch (IOException e) {
                    System.out.println(serverName + " IO: " + e.getMessage());
                    logger.info(e.getMessage());
                } finally {

                    if (aSocket != null)
                        aSocket.close();

                }
                logger.info(serverName + " UDP Transfer Server: replied");
                System.out.println(serverName + " UDP Transfer Server: replied");
            }
        });
        t.start();

    }

    @Override
    public String getRecordCounts(String managerId, String recordType, int udpIntrSrvsSenderPrt, int udpIntrSrvsReceiverPrt) {
        logger.info(serverName + " UDP Server: Start of requesting other server's count on the ports :" + udpIntrSrvsSenderPrt + " and " + udpIntrSrvsReceiverPrt);
        System.out.println(serverName + " UDP Server: Start of requesting other server's count on the ports :" + udpIntrSrvsSenderPrt + " and " + udpIntrSrvsReceiverPrt);

        String countResult = "MYSELF " + returnOwnRecordCounts(recordType) + ",SECOND " + otherServer(recordType, udpIntrSrvsReceiverPrt) + ",FIRST " + otherServer(recordType, udpIntrSrvsSenderPrt);
        System.out.println(countResult);

        System.out.println(serverName + " UDP Server: got the answer");
        logger.info(serverName + " UDP Server: got the answer");
        return countResult;
    }


    private String otherServer(String recordType, int serverPort) {
        logger.info("I am " + serverName + " and am receiving request on port " + serverPort);
        System.out.println("I am " + serverName + " and am receiving request on port " + serverPort);
        DatagramSocket aSocket = null;
        DatagramPacket reply = null;
        String result = "";

        try {
            aSocket = new DatagramSocket();
            byte[] message = recordType.getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(message, recordType.length(), aHost, serverPort);
            aSocket.send(request);
            byte[] firstBuffer = new byte[100];
            reply = new DatagramPacket(firstBuffer, firstBuffer.length);
            aSocket.receive(reply);
            result = new String(reply.getData(), 0, reply.getLength());
        } catch (SocketException e) {
            System.out.println(serverName + " Socket: " + e.getMessage());
            logger.info(e.getMessage());
        } catch (IOException e) {
            System.out.println(serverName + " IO: " + e.getMessage());
            logger.info(serverName + " IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
        logger.info(serverName + " is replied");
        System.out.println(serverName + " is replied");
        return result;
    }

    @Override
    public String editRecord(String managerId, String recordId, String fieldName, String newValue) {
        logger.info(serverName + " Server: is editing record with recordID :" + recordId + " and field: " + fieldName + " and new value :" + newValue + "");
        System.out.println(serverName + " Server: is editing record with recordID :" + recordId + " and field: " + fieldName + " and new value :" + newValue + "");
        int isValid = 1;
        for (Map.Entry<Character, List<SuperRecord>> entry : this.clinicHmap.entrySet()) {
            List<SuperRecord> list = new ArrayList<>();
            list = entry.getValue();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                SuperRecord superRecord = (SuperRecord) iterator.next();
                if (superRecord.recordID.equals(recordId)) {
                    if (superRecord.recType.equals("DR")) {
                        logger.info(serverName + " Server: recordID :" + recordId + " belongs to a Doctor Record");
                        System.out.println(serverName + " Server: recordID :" + recordId + " belongs to a Doctor Record");
                        isValid = validateDRFieldsToEdit(fieldName, newValue);
                        if (isValid == 1) {
                            for (Field field : superRecord.getClass().getDeclaredFields()) {
                                if (field.getName().equals(fieldName)) {
                                    try {
                                        synchronized (superRecord) {
                                            field.set(superRecord, newValue);
                                        }
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                        logger.info(e.getMessage());
                                    }

                                }
                            }
                            System.out.println(serverName + " Server: DoctorRecord with record ID " + superRecord.recordID + " is Edited");
                            logger.info(serverName + " Server: DoctorRecord with record ID " + superRecord.recordID + " is Edited");
                            return "Record with id : " + recordId + "  was edited Successfully!";
                        } else if (isValid == 0)
                            System.out.println("You are not allowed to change the field : " + fieldName);
                        else if (isValid == -1)
                            System.out.println("You are not allowed to change the location; to do so you have to use transfer record ;) ");
                    } else {
                        logger.info(serverName + " Server: recordID :" + recordId + " belongs to a Nurse Record");
                        System.out.println(serverName + " Server: recordID :" + recordId + " belongs to a Nurse Record");
                        isValid = validateNRFieldsToEdit(fieldName);
                        if (isValid == 1) {
                            for (Field field : superRecord.getClass().getDeclaredFields()) {
                                if (field.getName().equals(fieldName)) {
                                    try {
                                        synchronized (superRecord) {
                                            if (field.getType().getName().equals("int")) {
                                                int renewedVlaue = Integer.parseInt(newValue);
                                                field.set(superRecord, renewedVlaue);
                                            } else if (field.getType().getName().equals("java.util.Date")) {
                                                DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
                                                Date dateResult = null;
                                                try {
                                                    dateResult = formatter.parse(newValue);
                                                } catch (ParseException e) {
                                                    System.out.println("Please Enter Active Date and observe this format MM/dd/yy");
                                                    logger.info(e.getMessage());
                                                }
                                                field.set(superRecord, dateResult);
                                            } else
                                                field.set(superRecord, newValue);
                                        }
                                    } catch (IllegalAccessException exception) {
                                        exception.printStackTrace();
                                        logger.info(exception.getMessage());
                                    }

                                }
                            }
                            System.out.println(serverName + " Server: NurseRecord with record ID " + superRecord.recordID + " is Edited");
                            logger.info(serverName + " Server: NurseRecord with record ID " + superRecord.recordID + " is Edited");
                            return "";
                        } else if (isValid == 0)
                            System.out.println("You are not allowed to change the field : " + fieldName);
                        else if (isValid == -1)
                            System.out.println("You are not allowed to change the location; to do so you have to use transfer record ;) ");

                    }
                }
            }
        }
        return "Record with record id : " + recordId + "  was not edited Successfully!";
    }

    @Override
    public String transferRecord(final String managerId, final String recordId, final String remoteClinicServerName) {
        logger.info("I am " + serverName + " and am sending transfer request");
        System.out.println("I am " + serverName + " and am sending transfer request");
        DatagramSocket aSocket = null;
        DatagramPacket request;
        int destinationPortNum;
        DatagramPacket reply = null;

        try {
            aSocket = new DatagramSocket();
            byte[] message;
            SuperRecord superRecord = findRecordById(recordId);
            if (superRecord != null)
                synchronized (superRecord) {
                    message = serialize(superRecord);
                    InetAddress aHost = InetAddress.getByName("localhost");
                    if (remoteClinicServerName.equals("mtl")) {
                        destinationPortNum = 6797;
                    } else if (remoteClinicServerName.equals("lvl")) {
                        destinationPortNum = 6798;
                    } else {
                        destinationPortNum = 6799;
                    }
                    request = new DatagramPacket(message, message.length, aHost, destinationPortNum);
                    aSocket.send(request);

                    byte[] firstBuffer = new byte[1000];
                    reply = new DatagramPacket(firstBuffer, firstBuffer.length);
                    aSocket.receive(reply);

                    String acknowledgement = new String(reply.getData(), "UTF-8").trim();
                    char lastName = superRecord.lastName.charAt(0);
                    if (acknowledgement.equals("OK")) {
                        for (Map.Entry<Character, List<SuperRecord>> entry : clinicHmap.entrySet()) {
                            List<SuperRecord> list = new ArrayList<>();
                            SuperRecord currentSupRec = new SuperRecord();
                            list = entry.getValue();
                            Iterator iterator = list.iterator();
                            while (iterator.hasNext()) {
                                currentSupRec = (SuperRecord) iterator.next();
                                if (currentSupRec.recordID.equals(superRecord.recordID)) {
                                    list.remove(currentSupRec);
                                    if (list.isEmpty()) {
                                        clinicHmap.remove(lastName);
                                        System.out.println(serverName + " : " + "after receiving acknowledge, original record is removed!");
                                        logger.info(serverName + " : " + "after receiving acknowledge, original record is removed!");
                                    }
                                }
                                return "This record has been transferred successfully!";
                            }
                        }
                    }

                }
        } catch (SocketException e) {
            System.out.println(serverName + " Socket: " + e.getMessage());
            logger.info(e.getMessage());
        } catch (IOException e) {
            System.out.println(serverName + " IO: " + e.getMessage());
            logger.info(serverName + " IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
        logger.info(serverName + " is replied");
        System.out.println(serverName + " is replied");
        return "This record has not been transferred successfully!";

    }


    @Override
    public void shutdown() {
        orb.shutdown(false);
    }

    public void start() {

        System.out.println("Thread " + serverName + " Starting!");
        if (t == null)
            t = new Thread(this, serverName);
        t.start();

    }


    public int validateNRFieldsToEdit(String fieldName) {
        logger.info("Data are going to validate before Editing");
        if (!fieldName.equals("designation") && !fieldName.equals("status") && !fieldName.equals("statusDate"))
            return 0;
        return 1;

    }

    public int validateDRFieldsToEdit(String fieldName, String fieldValue) {
        logger.info("Data are going to validate before Editing");
        if (!fieldName.equals("address") && !fieldName.equals("phoneNumber") && !fieldName.equals("location"))
            return 0;
        if (fieldName.equals("location") && !fieldValue.equals("MTL") && !fieldValue.equals("LVL") && !fieldValue.equals("DDO"))
            return -1;
        return 1;
    }

    public SuperRecord findRecordById(String recordId) {

        for (Map.Entry<Character, List<SuperRecord>> entry : this.clinicHmap.entrySet()) {
            List<SuperRecord> list = new ArrayList<>();
            list = entry.getValue();
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                SuperRecord superRecord = (SuperRecord) iterator.next();
                if (superRecord.recordID.equals(recordId)) {
                    return superRecord;
                }
            }
        }
        return null;
    }

    public byte[] serialize(SuperRecord superRecord) throws IOException {
        String recordStr = "";
        String delim = ",";
        recordStr = recordStr.concat(superRecord.recordID);
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(superRecord.managerID);
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(superRecord.firstName);
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(superRecord.lastName);
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(superRecord.recType);
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(String.valueOf(superRecord.designation));
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(String.valueOf(superRecord.status));
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(superRecord.statusDate);
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(String.valueOf(superRecord.category));
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(superRecord.address);
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(superRecord.phoneNumber);
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(String.valueOf(superRecord.specialization));
        recordStr = recordStr.concat(delim);
        recordStr = recordStr.concat(String.valueOf(superRecord.location));
        recordStr = recordStr.concat(delim);

        return recordStr.getBytes();
    }

    public SuperRecord deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        String recStr = new String(bytes, "UTF-8");

        SuperRecord superRecord = new SuperRecord();
        String[] tokens = recStr.split(",");

        superRecord.recordID = tokens[0];
        superRecord.managerID = tokens[1];
        superRecord.firstName = tokens[2];
        superRecord.lastName = tokens[3];
        superRecord.recType = tokens[4];
        if (tokens[5] != null)
            superRecord.designation = Integer.parseInt(tokens[5]);
        if (tokens[6] != null)
            superRecord.status = Integer.parseInt(tokens[6]);
        superRecord.statusDate = tokens[7];
        if (tokens[8] != null)
            superRecord.category = Integer.parseInt(tokens[8]);
        superRecord.address = tokens[9];
        superRecord.phoneNumber = tokens[10];
        if (tokens[11] != null)
            superRecord.specialization = Integer.parseInt(tokens[11]);
        superRecord.location = Integer.parseInt(tokens[12]);

        return superRecord;

    }

    private static long idCounter = 0;

    public static synchronized String createID() {
        return String.format("%05d", ++idCounter);
    }


}

