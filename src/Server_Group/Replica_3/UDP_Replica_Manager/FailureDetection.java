package Server_Group.Replica_3.UDP_Replica_Manager;

import Front_End.Front_End_Config;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by Mahdiye on 8/3/2016.
 */
public class FailureDetection extends TimerTask {

    Timer timer = new Timer();

    public FailureDetection() {

        timer.scheduleAtFixedRate(this, new Date(), Replica_Manager_Config.interval);
    }

    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    @Override
    public void run() {

        System.out.println(System.currentTimeMillis() + " : Begin to PING the replica with port : " + Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT);
        logger.info(System.currentTimeMillis() + " : Begin to PING the replica with port : " + Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT);

        doPing(Replica_Manager_Config.HOST_NAME, Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT, "001");

        System.out.println("Ping to replica with port : " + Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT + " : is done!");
        logger.info("Ping to replica with port : " + Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_FRONT_END_LISTENING_PORT + " : is done!");

        System.out.println(System.currentTimeMillis() + " : Begin to PING the replica with port : " + Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT);
        logger.info(System.currentTimeMillis() + " : Begin to PING the replica with port : " + Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT);

        doPing(Replica_Manager_Config.HOST_NAME, Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT, "001");

        System.out.println("Ping to replica with port : " + Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT + " : is done!");
        logger.info("Ping to replica with port : " + Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT + " : is done!");

    }

    public String doPing(String targetAddress, int targetPort, String msg) {
        DatagramSocket pingSocket = null;
        try {
            pingSocket = new DatagramSocket();
            pingSocket.connect(new InetSocketAddress(targetAddress, targetPort));
            pingSocket.setSoTimeout(Replica_Manager_Config.timeout);
        } catch (Exception e) {
            System.out.println("Unable to initialize the datagram socket:  " + e);
            logger.info("Unable to initialize the datagram socket:  " + e);
        }

        byte[] txBytes = msg.getBytes();
        DatagramPacket txPacket = new DatagramPacket(txBytes, 100);

        byte[] rxBytes = new byte[100];
        DatagramPacket rxPacket = new DatagramPacket(txBytes, 100);

        try {
            txPacket.setData(txBytes);
            pingSocket.send(txPacket);

            while (true) {
                pingSocket.receive(rxPacket);
                rxBytes = rxPacket.getData();
            }
        } catch (SocketTimeoutException ste) {
            System.out.println("PING " + targetAddress + " Timed Out");
            if (targetPort == Front_End_Config.PRIMARY_SERVER_PORT) {
                int newLeaderIndex = elect(Replica_Manager_Config.rep[3]);
                String newLeaderPort = String.valueOf(Replica_Manager_Config.priority[newLeaderIndex]);
                doPing(Replica_Manager_Config.HOST_NAME, Server_Group.Replica_2.UDP_Replica_Manager.Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT, newLeaderPort);
            }
        } catch (Exception e) {
            System.out.println("ERROR:  Unable to send request -- " + e);
            logger.info("ERROR:  Unable to send request -- " + e);
        }

        return rxBytes.toString();

    }


    //always ready to reply to ping request
    public void replyToPing(final int portNum) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                DatagramSocket aSocket = null;
                try {
                    aSocket = new DatagramSocket(portNum);
                    byte[] buffer = new byte[1000];

                    while (true) {
                        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                        aSocket.receive(request);
                        String response = "I am alive!";
                        DatagramPacket reply = new DatagramPacket(response.getBytes(), response.length(), request.getAddress(), request.getPort());
                        aSocket.send(reply);
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
        initiator = initiator - 1;
        int newCoordinator = initiator + 1;
        for (int i = 0; i < 2; i++) {
            if (Replica_Manager_Config.priority[initiator] < Replica_Manager_Config.priority[i]) {
                System.out.println("Election message is sent from " + (initiator + 1) + " to " + (i + 1));
                logger.info("Election message is sent from " + (initiator + 1) + " to " + (i + 1));
                if (doPing(Replica_Manager_Config.HOST_NAME, Replica_Manager_Config.priority[Replica_Manager_Config.rep[i]], "001") != null)
                    elect(i + 1);
            }
        }
        return newCoordinator;
    }

}
