package Server_Group.Replica_1.Record_Type;

/**
 * a class of the doctor record
 *
 */
public class DoctorRecord {

	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private String specialization;
	private String location;
	
	/**
	 * Default Constructor of DoctorRecord.
	 * @param n_firstName
	 * @param n_lastName
	 * @param n_address
	 * @param n_phone
	 * @param n_specialization
	 * @param n_location
	 */
	public DoctorRecord(String n_firstName, String n_lastName, String n_address, String n_phone, String n_specialization, String n_location){
		this.firstName = n_firstName;
		this.lastName = n_lastName;
		this.address = n_address;
		this.phone = n_phone;
		this.specialization = n_specialization;
		this.location = n_location;
	}
	
	/**
	 * This is a method of get first name
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * This is a method of set first name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * This is a method of get last name
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * This is a method of set last name
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * This is a method of get address
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * This is a method of set address
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * This is a method of get phone number
	 * @return
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * This is a method of set phone number
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * This is a method of get specialization
	 * @return
	 */
	public String getSpecialization() {
		return specialization;
	}
	
	/**
	 * This is a method of set specialization
	 * @param specialization
	 */
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	
	/**
	 * This is a method of get the location
	 * @return
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * This is a method of set the location
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * This is a method of print the information
	 */
	@Override
	public String toString() {
		String str = "First Name: "+ getFirstName() + "\n" 
					+ "Last Name: " + getLastName() + "\n"
					+ "Address: " + getAddress() + "\n"
					+ "Phone: " + getPhone() + "\n"
					+ "specialization: " + getSpecialization() + "\n"
					+ "location: " + getLocation() + "\n";
		return str;
	}
}
