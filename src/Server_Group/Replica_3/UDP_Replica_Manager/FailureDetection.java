package Server_Group.Replica_3.UDP_Replica_Manager;

import Front_End.Front_End_Config;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by Mahdiye on 8/3/2016.
 */
public class FailureDetection extends TimerTask {

    public static ArrayList<String> liveHostsByName = new ArrayList<String>();

    Timer timer = new Timer();

    public FailureDetection() {

        liveHostsByName.add("Host_1");
        liveHostsByName.add("Host_2");
        liveHostsByName.add("Host_3");

        timer.scheduleAtFixedRate(this, Replica_Manager_Config.INITIALDELAY, Replica_Manager_Config.INTERVAL);

        int newLeaderIndex = elect(Replica_Manager_Config.REPLICA[2]);
        String newLeaderPort = "NEWLEADER".concat(String.valueOf(Replica_Manager_Config.priority[newLeaderIndex - 1]));
        doPing(Replica_Manager_Config.HOST_NAME, Front_End_Config.LOCAL_LISTENING_PORT, newLeaderPort);

    }

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    @Override
    public void run() {

        System.out.println(System.currentTimeMillis() + " : Begin to PING the replica with port : " + Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT);
        logger.info(System.currentTimeMillis() + " : Begin to PING the replica with port : " + Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT);

        doPing(Replica_Manager_Config.HOST_NAME, Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT, "0000" + "\n" + "006" + "\n" + "0000000");

        System.out.println("Ping to replica with port : " + Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT + " : is done!");
        logger.info("Ping to replica with port : " + Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT + " : is done!");

        System.out.println(System.currentTimeMillis() + " : Begin to PING the replica with port : " + Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT);
        logger.info(System.currentTimeMillis() + " : Begin to PING the replica with port : " + Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT);

        doPing(Replica_Manager_Config.HOST_NAME, Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT, "0000" + "\n" + "006" + "\n" + "0000000");

        System.out.println("Ping to replica with port : " + Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT + " : is done!");
        logger.info("Ping to replica with port : " + Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT + " : is done!");

    }


    public String doPing(String targetAddress, int targetPort, String msg) {
        DatagramSocket aSocket = null;
        DatagramPacket reply = null;
        System.out.println("++++++++++ function ping started by replica number one");
        logger.info("++++++++++ function ping started by replica number one");
        try {
            aSocket = new DatagramSocket();
            aSocket.setSoTimeout(Replica_Manager_Config.TIMEOUT);
            byte[] m = msg.getBytes();
            InetAddress aHost = InetAddress.getByName(targetAddress);

            DatagramPacket request = new DatagramPacket(m, msg.length(), aHost, targetPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            System.out.println("Reply: " + new String(reply.getData()));
        } catch (SocketTimeoutException ste) {
            System.out.println("PING " + targetAddress + "  :" + targetPort + " Timed Out");
            logger.info("PING " + targetAddress + "  :" + targetPort + " Timed Out");
            liveHostsByName.remove("Host_1");
            if (targetPort == Front_End_Config.PRIMARY_SERVER_PORT) {
                int newLeaderIndex = elect(Replica_Manager_Config.REPLICA[2]);
                String newLeaderPort = "NEWLEADER".concat(String.valueOf(Replica_Manager_Config.priority[newLeaderIndex - 1]));
                doPing(Replica_Manager_Config.HOST_NAME, Front_End_Config.LOCAL_LISTENING_PORT, newLeaderPort);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {

            if (aSocket != null)
                aSocket.close();

        }

        System.out.println("++++++++++ function ping finished by replica number one");
        logger.info("++++++++++ function ping finished by replica number one");

        return new String(reply.getData());

    }


    //always ready to reply to ping request
    public void replyToPing(final int portNum, final DatagramSocket aSocket) {

        System.out.println("Replica number two begin to listen on port : " + portNum);
        logger.info("Replica number two begin to listen on port : " + portNum);


        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    byte[] buffer = new byte[1000];

                    while (true) {
                        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                        aSocket.receive(request);
                        String response = "I am alive!";
                        DatagramPacket reply = new DatagramPacket(response.getBytes(), response.length(), request.getAddress(), request.getPort());
                        aSocket.send(reply);

                        System.out.println("Replica number three replied");
                        logger.info("Replica number three replied");

                    }
                } catch (SocketException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } finally {

                    if (aSocket != null)
                        aSocket.close();

                }
            }
        });
        t.start();

    }


    int elect(int initiator) {
        System.out.println("Bully Algorithm is going to run by initiator " + initiator);
        logger.info("Bully Algorithm is going to run by initiator " + initiator);
        initiator = initiator - 1;
        int newCoordinator = initiator + 1;
        for (int i = 0; i < Replica_Manager_Config.n; i++) {
            if (Replica_Manager_Config.priority[initiator] < Replica_Manager_Config.priority[i]) {
                System.out.println("Election message is sent from " + (initiator + 1) + " to " + (i + 1));
                logger.info("Election message is sent from " + (initiator + 1) + " to " + (i + 1));
                if (i + 1 < Server_Group.Replica_3.UDP_Replica_Manager.Replica_Manager_Config.n && doPing(Replica_Manager_Config.HOST_NAME, Replica_Manager_Config.priority[Replica_Manager_Config.REPLICA[i+1]], "0000" + "\n" + "006" + "\n" + "0000000") != null)
                    elect(i + 1);
            }
        }
        System.out.println("New Coordinator is elected " + newCoordinator);
        logger.info("New Coordinator is elected " + newCoordinator);
        return newCoordinator;
    }

}
