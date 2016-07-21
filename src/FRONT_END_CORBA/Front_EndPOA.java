package FRONT_END_CORBA;


/**
* FRONT_END_CORBA/Front_EndPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Front_End.idl
* Thursday, July 21, 2016 5:42:26 PM EDT
*/

public abstract class Front_EndPOA extends org.omg.PortableServer.Servant
 implements FRONT_END_CORBA.Front_EndOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("createDRecord", new java.lang.Integer (0));
    _methods.put ("createNRecord", new java.lang.Integer (1));
    _methods.put ("getRecordCounts", new java.lang.Integer (2));
    _methods.put ("editRecord", new java.lang.Integer (3));
    _methods.put ("transferRecord", new java.lang.Integer (4));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // FRONT_END_CORBA/Front_End/createDRecord
       {
         String managerId = in.read_string ();
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         String address = in.read_string ();
         String phone = in.read_string ();
         String specialization = in.read_string ();
         String location = in.read_string ();
         String $result = null;
         $result = this.createDRecord (managerId, firstName, lastName, address, phone, specialization, location);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // FRONT_END_CORBA/Front_End/createNRecord
       {
         String managerId = in.read_string ();
         String firstName = in.read_string ();
         String lastName = in.read_string ();
         String designation = in.read_string ();
         String status = in.read_string ();
         String statusDate = in.read_string ();
         String $result = null;
         $result = this.createNRecord (managerId, firstName, lastName, designation, status, statusDate);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // FRONT_END_CORBA/Front_End/getRecordCounts
       {
         String managerId = in.read_string ();
         String recordType = in.read_string ();
         String $result = null;
         $result = this.getRecordCounts (managerId, recordType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // FRONT_END_CORBA/Front_End/editRecord
       {
         String managerId = in.read_string ();
         String recordID = in.read_string ();
         String fieldName = in.read_string ();
         String newValue = in.read_string ();
         String $result = null;
         $result = this.editRecord (managerId, recordID, fieldName, newValue);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // FRONT_END_CORBA/Front_End/transferRecord
       {
         String managerId = in.read_string ();
         String recordID = in.read_string ();
         String remoteClinicServerName = in.read_string ();
         String $result = null;
         $result = this.transferRecord (managerId, recordID, remoteClinicServerName);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:FRONT_END_CORBA/Front_End:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Front_End _this() 
  {
    return Front_EndHelper.narrow(
    super._this_object());
  }

  public Front_End _this(org.omg.CORBA.ORB orb) 
  {
    return Front_EndHelper.narrow(
    super._this_object(orb));
  }


} // class Front_EndPOA