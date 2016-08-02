package Server_Group.Replica_2.DDO;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import Server_Group.Replica_2.ClinicServer.ClinicServerInt;
import Server_Group.Replica_2.ClinicServer.ClinicServerIntHelper;
import Server_Group.Replica_2.DDO.ClinicServerDDO;
import Server_Group.Replica_2.LVL.ClinicServerLVL;

public class StartServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("DDO Server...");
		ClinicServerDDO S2 = new ClinicServerDDO(args);
		S2.run();
	}

}
