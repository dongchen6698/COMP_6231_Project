package Server_Group.Replica_3.com;


/**
* com/ClinicTasksOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ClinicTasks.idl
* Wednesday, August 3, 2016 9:09:28 PM EDT
*/

public interface ClinicTasksOperations 
{
  String createRecord (String managerId, Server_Group.Replica_3.com.SuperRecord superRecord);
  String getRecordCounts (String managerId, String recordType, int udpIntrSrvsSenderPrt, int udpIntrSrvsReceiverPrt);
  String editRecord (String managerId, String recordId, String fieldName, String newValue);
  String transferRecord (String managerId, String recordId, String remoteClinicServerName);
  void shutdown ();
} // interface ClinicTasksOperations