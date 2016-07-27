package Client_Side;

import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import FRONT_END_CORBA.Front_End;
import FRONT_END_CORBA.Front_EndHelper;

/**
 * This is a Client side of DSMS.
 * @author AlexChen
 *
 */
public class ManagerClients {
	
	/**
	 * this is a constructor of the class
	 */
	public ManagerClients() {
		super();
	}
	
	/**
	 * This is a local function for check manager format use Regular expression.
	 * @param n_managerID
	 * @return
	 */
	public static Boolean checkManagerIDFormat(String n_managerID){
		String pattern = "^(MTL|LVL|DDO)(\\d{5})$";
		Pattern re = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);
		Matcher matcher = re.matcher(n_managerID);
		if(matcher.find()){
			return true;
		}else{
			System.err.println("Usage:[MTL,LVL,DDO]+[10000]\n");
			return false;
		}
	}
	
	/**
	 * This is a loop for require user to input the managerID, like Login.
	 */
	public static void checkManagerLogIn(){
		Boolean valid = false;
		while(!valid){
			Scanner keyboard = new Scanner(System.in);
			do{
				System.out.println("****Please input the manager ID****");
				Client_Config.MANAGER_ID = keyboard.next();
			}while(!checkManagerIDFormat(Client_Config.MANAGER_ID));
			valid = true;
		}
	}
	
	/**
	 * If managerID is valid, this function is for get the stub of that server.
	 * @param managerID
	 * @return
	 * @throws Exception
	 */
	public static Front_End getServerReferrence(String[] args){
		try {
			//initial the port number of 1050;
			Properties props = new Properties();
	        props.put("org.omg.CORBA.ORBInitialPort", Client_Config.ORB_INITIAL_PORT);
	        
			// create and initialize the ORB
			ORB orb = ORB.init(args, props);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			// Use NamingContextExt instead of NamingContext. This is 
			// part of the Interoperable naming Service.  
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			return Front_EndHelper.narrow(ncRef.resolve_str("front_end"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	/**
	 * Define the Menu list.
	 * @param managerID
	 */
	public static void showMenu(String managerID) {
		System.out.println("****Welcome to DSMS****");
		System.out.println("****Manager: "+managerID +"****\n");
		System.out.println("Please select an option (1-5)");
		System.out.println("1. Create Doctor Record.");
		System.out.println("2. Create Nurse Record");
		System.out.println("3. Get Record Counts");
		System.out.println("4. Edit Record");
		System.out.println("5. Transfer Record");
		System.out.println("6. Exit DSMS");
	}
	
	public static void main(String[] args) {
		checkManagerLogIn();
		
		try {
			Client_Config.FRONT_END_IMPL = getServerReferrence(args);
			int userChoice=0;
			Scanner keyboard = new Scanner(System.in);
			showMenu(Client_Config.MANAGER_ID);
			
			while(true)
			{
				Boolean valid = false;
				while(!valid)
				{
					try{
						userChoice=keyboard.nextInt();
						valid=true;
					}
					catch(Exception e)
					{
						System.out.println("Invalid Input, please enter an Integer");
						valid=false;
						keyboard.nextLine();
					}
				}
				
				switch(userChoice)
				{
				case 1:
					System.out.println("Please input the FirstName");
					String d_firstname = keyboard.next();
					System.out.println("Please input the LastName");
					String d_lastname = keyboard.next();
					System.out.println("Please input the Address");
					String d_address = keyboard.next();
					System.out.println("Please input the Phone");
					String d_phone = keyboard.next();
					System.out.println("Please input the Specialization");
					String d_specialization = keyboard.next();
					System.out.println("Please input the Location(mtl/lvl/ddo)");
					String d_location =keyboard.next();
					String d_result = Client_Config.FRONT_END_IMPL.createDRecord(Client_Config.MANAGER_ID, d_firstname, d_lastname, d_address, d_phone, d_specialization, d_location);
					
					if(!d_result.contains("is not right")){
						System.out.println("Manager Creat Doctor Record Succeed!" + "\n" + d_result);
					}
					showMenu(Client_Config.MANAGER_ID);
					break;
				case 2:
					System.out.println("Manager Choose Creat Nurse Record.");
					System.out.println("Please input the FirstName");
					String n_firstname = keyboard.next();
					System.out.println("Please input the LastName");
					String n_lastname = keyboard.next();
					System.out.println("Please input the Designation(junior/senior)");
					String n_designation = keyboard.next();
					System.out.println("Please input the Status(active/terminated)");
					String n_status = keyboard.next();
					System.out.println("Please input the Status Date(yyyy/mm/dd/)");
					String n_status_date = keyboard.next();
					String n_result = Client_Config.FRONT_END_IMPL.createNRecord(Client_Config.MANAGER_ID ,n_firstname, n_lastname, n_designation, n_status, n_status_date);
					
					if(!n_result.contains("is not right")){
						System.out.println("Manager Creat Doctor Record Succeed!" + "\n" + n_result);
					}
					showMenu(Client_Config.MANAGER_ID);
					break;
				case 3:
					System.out.println("Manager Choose Get Record Counts.");
					System.out.println("Please input search type");
					String searchtype = keyboard.next();
					String s_result = Client_Config.FRONT_END_IMPL.getRecordCounts(Client_Config.MANAGER_ID, searchtype);
					
					System.out.println("Get Record Counts: " + "\n" + s_result);
					showMenu(Client_Config.MANAGER_ID);
					break;
				case 4:
					System.out.println("Manager Choose Edit Record.");
					System.out.println("Please input the RecordID");
					String e_recordID = keyboard.next();
					System.out.println("Please input the FieldName");
					String e_fieldname = keyboard.next();
					System.out.println("Please input the New Value");
					String e_newvalue = keyboard.next();
					String e_result = Client_Config.FRONT_END_IMPL.editRecord(Client_Config.MANAGER_ID, e_recordID, e_fieldname, e_newvalue);
					
					if(!e_result.contains("is not right")){
						System.out.println("Manager Creat Doctor Record Succeed!" + "\n" + e_result);
					}
					showMenu(Client_Config.MANAGER_ID);
					break;
				case 5:
					System.out.println("Manager Choose Transfer Record.");
					System.out.println("Please input the RecordID");
					String t_recordID = keyboard.next();
					System.out.println("Please input the remote clinic server name.(mtl/lvl/ddo)");
					String t_remoteClinicServerName = keyboard.next();
					String t_result = Client_Config.FRONT_END_IMPL.transferRecord(Client_Config.MANAGER_ID, t_recordID, t_remoteClinicServerName);
					
					if(!t_result.contains("is not right")){
						System.out.println("Manager Transfer Record Succeed!" + "\n" + t_result);
					}
					showMenu(Client_Config.MANAGER_ID);
					break;
				case 6:
					System.out.println("Manager Exit the DSMS");
					System.out.println("Have a nice day!");
					keyboard.close();
					System.exit(0);
				default:
					System.out.println("Invalid Input, please try again.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}
}
