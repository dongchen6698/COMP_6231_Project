package Server_Group.Replica_2.MTL;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import Server_Group.Replica_2.ClinicServer.ClinicServerInt;
import Server_Group.Replica_2.ClinicServer.ClinicServerIntHelper;
import Server_Group.Replica_2.DDO.ClinicServerDDO;
import Server_Group.Replica_2.MTL.ClinicServerMTL;

public class StartServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("MTL Server...");
		ClinicServerMTL S3 = new ClinicServerMTL(args);
		S3.run();

	}

}
