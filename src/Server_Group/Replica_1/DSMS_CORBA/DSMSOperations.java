package Server_Group.Replica_1.DSMS_CORBA;


/**
* DSMS_CORBA/DSMSOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Distributed_Staff_Management_System.idl
* Wednesday, July 27, 2016 5:40:24 PM EDT
*/

public interface DSMSOperations 
{
  String createDRecord (String managerId, String firstName, String lastName, String address, String phone, String specialization, String location);
  String createNRecord (String managerId, String firstName, String lastName, String designation, String status, String statusDate);
  String getRecordCounts (String managerId, String recordType);
  String editRecord (String managerId, String recordID, String fieldName, String newValue);
  String transferRecord (String managerId, String recordID, String remoteClinicServerName);
} // interface DSMSOperations
