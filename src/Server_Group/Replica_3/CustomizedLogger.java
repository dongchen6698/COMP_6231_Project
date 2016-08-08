package Server_Group.Replica_3;

import java.io.IOException;
import java.util.logging.*;

/**
 * Created by Mahdiye on 7/31/2016.
 */
public class CustomizedLogger {

    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static public void setup(String fileName) throws IOException {

        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler(fileName + ".log");

        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);


    }

}
