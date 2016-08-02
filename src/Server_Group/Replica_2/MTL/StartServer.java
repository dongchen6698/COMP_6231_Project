package Server_Group.Replica_2.MTL;

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
