package Server_Group.Replica_3;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Mahdiye on 7/31/2016.
 */
public class StartServer {
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    public static void main(String args[]) {

        ClinicTasksServant mtlClinicServer = null;
        ClinicTasksServant lvlClinicServer = null;
        ClinicTasksServant ddoClinicServer = null;

        try {
            CustomizedLogger.setup("ServerLogFile");
            logger.info("***************************start of application, let's create 3 object of the server with names mtl, lvl and ddo");
            mtlClinicServer = new ClinicTasksServant("mtl", 6789, 6797, 6001, args);
            mtlClinicServer.start();
            lvlClinicServer = new ClinicTasksServant("lvl", 6790, 6798, 6002, args);
            lvlClinicServer.start();
            ddoClinicServer = new ClinicTasksServant("ddo", 6791, 6799, 6003, args);
            ddoClinicServer.start();
            logger.info("===========================three different instances of servers are created and up!");
        } catch (SecurityException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }

        System.out.println("Server is started..........");
        logger.info("Server is started..........");


    }


}
