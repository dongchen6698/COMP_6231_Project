package Server_Group.Replica_1.Record_Type;

/**
 * A class of record information
 *
 */
public class RecordInfo {
	private String recordID;
	private DoctorRecord doctorRecord;
	private NurseRecord nurseRecord;

	/**
	 * 
	 * @param n_recordID
	 * @param n_doctorRecord
	 * This Constructor is for create doctor record with recordID.
	 */
	public RecordInfo(String n_recordID, DoctorRecord n_doctorRecord) {
		this.recordID = n_recordID;
		this.doctorRecord = n_doctorRecord;
	}
	
	/**
	 * 	This Constructor is for create nurse record with recordID.
	 * @param n_recordID
	 * @param n_nurseRecord
	 */
	public RecordInfo(String n_recordID, NurseRecord n_nurseRecord) {
		this.recordID = n_recordID;
		this.nurseRecord = n_nurseRecord;
	}
	
	/**
	 * This is a method of get record id
	 * @return
	 */
	public String getRecordID() {
		return recordID;
	}
	
	/**
	 * This is method of set record id
	 * @param recordID
	 */
	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}
	
	/**
	 * This is method of get doctor record
	 * @return
	 */
	public DoctorRecord getDoctorRecord() {
		return doctorRecord;
	}
	
	/**
	 * This is method of set doctor record
	 * @param doctorRecord
	 */
	public void setDoctorRecord(DoctorRecord doctorRecord) {
		this.doctorRecord = doctorRecord;
	}
	
	/**
	 * This is method of get nurse record
	 * @return
	 */
	public NurseRecord getNurseRecord() {
		return nurseRecord;
	}
	
	/**
	 * This is method of set a nurse record
	 * @param nurseRecord
	 */
	public void setNurseRecord(NurseRecord nurseRecord) {
		this.nurseRecord = nurseRecord;
	}
	
	/**
	 * This is method of print information
	 */
	@Override
	public String toString() {
		String str = null;
		switch (recordID.substring(0, 2)) {
		case "DR":
			str = "RecordID: " + getRecordID() + "\n" + doctorRecord.toString();
			return str;
		case "NR":
			str = "RecordID: " + getRecordID() + "\n" + nurseRecord.toString();
			return str;
		default:
			return null;
		}
	}
}
