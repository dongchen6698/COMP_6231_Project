package Server_Group.Replica_2.UDP_Replica_Manager;

import Front_End.Front_End_Config;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Map.Entry;
import java.net.*;
import java.util.logging.Logger;

public class Front_End_Listener_Thread implements Runnable {
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Front_End_Listener_Thread() {

    }

    public static Boolean Broad_Cast_Request(String request_ID, InetAddress address, Integer port) {
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(2000);

            // Send the request just to the alive replicas
            for (Entry<Integer, String> entry : Front_End_Config.liveHostsByName.entrySet()) {
                if (entry.getKey() == Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT) {
                    System.out.println("equal local broadcast port , skip");
                    continue;
                } else {
                    String currentRequestId = request_ID;
                    Boolean retransfer = true;
                    do {
                        logger.info("Broadcast the request number: " + currentRequestId + " to host: " + entry.getValue());
                        String message = Replica_Manager_Config.REQUEST_HASH_TABLE.get(currentRequestId);

                        DatagramPacket new_request = new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
                        new_request.setPort(entry.getKey());
                        socket.send(new_request);
                        byte[] buffer = new byte[100];
                        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

                        try {
                            socket.receive(reply);
                            currentRequestId = new String(reply.getData()).trim();
                            if (Replica_Manager_Config.REQUEST_HASH_TABLE.containsKey(currentRequestId)) {
                                logger.info("The host: " + entry.getValue() + " asked for require number: " + currentRequestId);
                                retransfer = true;
                            } else {
                                logger.info("The request is added to the host:" + entry.getValue());
                                retransfer = false;
                            }

                        } catch (SocketTimeoutException e) {
                            logger.info("The host " + entry.getValue() + " is unreachable. try to resend ...");
                        }
                    } while (retransfer);

                }
            }
        } catch (Exception e) {
            System.out.println("Socket: " + e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
        return true;
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT); // port: 4000
            while (true) {
                System.out.println("start fe listener");
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String request_ID = new String(request.getData()).trim().split("\n")[0];
                if (!request_ID.equals("0000")) {
                    System.out.println("add request: " + request_ID + " to hashtable");
                    Replica_Manager_Config.REQUEST_HASH_TABLE.put(request_ID, new String(request.getData()).trim());

                    String acknowledgement = "OK";
                    DatagramPacket reply = new DatagramPacket(acknowledgement.getBytes(), acknowledgement.getBytes().length, request.getAddress(), request.getPort());
                    Boolean bcr = Broad_Cast_Request(request_ID, request.getAddress(), request.getPort());
                    if (bcr) {
                        System.out.println("Send acknowledgement back to FE." + reply.getPort());
                        socket.send(reply);
                        System.out.println("Create new thread to handle the request from FE");
                        new UDP_CORBA_Connection_Thread(socket, request);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }
}
