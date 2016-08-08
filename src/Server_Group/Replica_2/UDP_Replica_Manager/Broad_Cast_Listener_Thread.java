package Server_Group.Replica_2.UDP_Replica_Manager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;

public class Broad_Cast_Listener_Thread implements Runnable {
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Broad_Cast_Listener_Thread() {

    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(Replica_Manager_Config.LOCAL_BROAD_CAST_LISTENING_PORT);
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                // send back the request ID
                String requestID = new String(request.getData()).trim().split("\n")[0];
                String FIFO = FIFO(request);
                Integer nextRequestID = Integer.parseInt(requestID);
                String nextRequestIDSting = String.format("%04d", (nextRequestID + 1));
                if (nextRequestIDSting.equals(FIFO)) {
                    socket.send(new DatagramPacket(FIFO.getBytes(), FIFO.getBytes().length, request.getAddress(), request.getPort()));
                    new UDP_CORBA_Connection_Thread(socket, request);

                } else {
                    // Request for previous request
                    socket.send(new DatagramPacket(FIFO.getBytes(), FIFO.getBytes().length, request.getAddress(), request.getPort()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }

    private String FIFO(DatagramPacket request) {
        String stringRequestID = new String(request.getData()).trim().split("\n")[0];
        Integer requestID = Integer.parseInt(stringRequestID);

        // The request is first request
        if (!Replica_Manager_Config.FIFO_HASH_TABLE.isEmpty()) {
            Integer lastRequestID = Replica_Manager_Config.FIFO_HASH_TABLE.get("lastID");

            if (lastRequestID < requestID) {
                // the new request must be one more than the lastID
                if (lastRequestID.equals(requestID - 1)) {
                    logger.info("The request number: " + requestID + " is saved.");
                    Replica_Manager_Config.FIFO_HASH_TABLE.remove("lastID");
                    Replica_Manager_Config.FIFO_HASH_TABLE.put("lastID", requestID);
                    return String.format("%04d", (requestID + 1));
                }
                // Some request is missed
                // send back the missed request number
                else {
                    logger.info("The request number: " + (Replica_Manager_Config.FIFO_HASH_TABLE.get("lastID") + 1) + " is missed. Ask for send it again..");
                    return String.format("%04d", (Replica_Manager_Config.FIFO_HASH_TABLE.get("lastID") + 1));
                }

            }else{
                logger.info("The request number: " + requestID + "is duplicate");
                return String.format("%04d", (requestID + 1));
            }
        }
        // First request
        else {
            logger.info("The FIFO hash table is updated with request number: " + requestID);
            Replica_Manager_Config.FIFO_HASH_TABLE.put("lastID", requestID);
            return String.format("%04d",(requestID + 1));
        }
    }
}
