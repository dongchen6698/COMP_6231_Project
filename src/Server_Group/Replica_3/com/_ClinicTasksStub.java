package Server_Group.Replica_3.com;


/**
* com/_ClinicTasksStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ClinicTasks.idl
* Wednesday, August 3, 2016 9:09:28 PM EDT
*/

public class _ClinicTasksStub extends org.omg.CORBA.portable.ObjectImpl implements Server_Group.Replica_3.com.ClinicTasks
{

  public String createRecord (String managerId, Server_Group.Replica_3.com.SuperRecord superRecord)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("createRecord", true);
                $out.write_string (managerId);
                Server_Group.Replica_3.com.SuperRecordHelper.write ($out, superRecord);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return createRecord (managerId, superRecord        );
            } finally {
                _releaseReply ($in);
            }
  } // createRecord

  public String getRecordCounts (String managerId, String recordType, int udpIntrSrvsSenderPrt, int udpIntrSrvsReceiverPrt)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getRecordCounts", true);
                $out.write_string (managerId);
                $out.write_string (recordType);
                $out.write_long (udpIntrSrvsSenderPrt);
                $out.write_long (udpIntrSrvsReceiverPrt);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getRecordCounts (managerId, recordType, udpIntrSrvsSenderPrt, udpIntrSrvsReceiverPrt        );
            } finally {
                _releaseReply ($in);
            }
  } // getRecordCounts

  public String editRecord (String managerId, String recordId, String fieldName, String newValue)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("editRecord", true);
                $out.write_string (managerId);
                $out.write_string (recordId);
                $out.write_string (fieldName);
                $out.write_string (newValue);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return editRecord (managerId, recordId, fieldName, newValue        );
            } finally {
                _releaseReply ($in);
            }
  } // editRecord

  public String transferRecord (String managerId, String recordId, String remoteClinicServerName)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("transferRecord", true);
                $out.write_string (managerId);
                $out.write_string (recordId);
                $out.write_string (remoteClinicServerName);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return transferRecord (managerId, recordId, remoteClinicServerName        );
            } finally {
                _releaseReply ($in);
            }
  } // transferRecord

  public void shutdown ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("shutdown", false);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                shutdown (        );
            } finally {
                _releaseReply ($in);
            }
  } // shutdown

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:com/ClinicTasks:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _ClinicTasksStub
