package Server_Group.Replica_3.com;


/**
* com/ClinicTasksHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ClinicTasks.idl
* Wednesday, August 3, 2016 9:09:28 PM EDT
*/

abstract public class ClinicTasksHelper
{
  private static String  _id = "IDL:com/ClinicTasks:1.0";

  public static void insert (org.omg.CORBA.Any a, Server_Group.Replica_3.com.ClinicTasks that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static Server_Group.Replica_3.com.ClinicTasks extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (Server_Group.Replica_3.com.ClinicTasksHelper.id (), "ClinicTasks");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static Server_Group.Replica_3.com.ClinicTasks read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_ClinicTasksStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, Server_Group.Replica_3.com.ClinicTasks value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static Server_Group.Replica_3.com.ClinicTasks narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof Server_Group.Replica_3.com.ClinicTasks)
      return (Server_Group.Replica_3.com.ClinicTasks)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      Server_Group.Replica_3.com._ClinicTasksStub stub = new Server_Group.Replica_3.com._ClinicTasksStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static Server_Group.Replica_3.com.ClinicTasks unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof Server_Group.Replica_3.com.ClinicTasks)
      return (Server_Group.Replica_3.com.ClinicTasks)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      Server_Group.Replica_3.com._ClinicTasksStub stub = new Server_Group.Replica_3.com._ClinicTasksStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
