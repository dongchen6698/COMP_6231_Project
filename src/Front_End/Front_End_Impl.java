package Front_End;

import org.omg.CORBA.ORB;

import FRONT_END_CORBA.Front_EndPOA;

public class Front_End_Impl extends Front_EndPOA{

	private ORB orb;
	
	/**
	 * Set ORB function
	 * @param orb_val
	 */
	public void setORB(ORB orb_val) {
	    this.orb = orb_val;
	}
	
	@Override
	public String createDRecord(String managerId, String firstName, String lastName, String address, String phone,
			String specialization, String location) {
		System.out.println(managerId + " Create DR Record");
		System.out.println("FirstName: " + firstName);
		return "success";
	}

	@Override
	public String createNRecord(String managerId, String firstName, String lastName, String designation, String status,
			String statusDate) {
		System.out.println(managerId + " Create NR Record");
		System.out.println("FirstName: " + firstName);
		return "success";
	}

	@Override
	public String getRecordCounts(String managerId, String recordType) {
		System.out.println(managerId + " Get Record Counts");
		return "success";
	}

	@Override
	public String editRecord(String managerId, String recordID, String fieldName, String newValue) {
		System.out.println(managerId + " Edit Record");
		return "success";
	}

	@Override
	public String transferRecord(String managerId, String recordID, String remoteClinicServerName) {
		System.out.println(managerId + " Transfer Record");
		return "success";
	}
}
