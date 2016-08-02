package Server_Group.Replica_2.ClinicServer;


/**
* ClinicServer/ClinicServerIntOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ClinicServer.idl
* Sunday, July 31, 2016 1:56:33 PM EDT
*/

public interface ClinicServerIntOperations 
{
  String createDRecord (String managerId, String firstName, String lastName, String address, String phone, String specialization, String location);
  String createNRecord (String mangerId, String firstName, String lastName, String designation, String status, String statusDate);
  String getRecordCounts (String managerId, String recordType);
  String editRecord (String managerId, String recordID, String fieldName, String newValue);
  String transferRecord (String managerID, String recordID, String remoteClinicServerName);
} // interface ClinicServerIntOperations