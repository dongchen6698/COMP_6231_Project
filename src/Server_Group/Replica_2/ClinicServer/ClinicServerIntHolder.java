package Server_Group.Replica_2.ClinicServer;

/**
* ClinicServer/ClinicServerIntHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ClinicServer.idl
* Sunday, July 31, 2016 1:56:33 PM EDT
*/

public final class ClinicServerIntHolder implements org.omg.CORBA.portable.Streamable
{
  public Server_Group.Replica_2.ClinicServer.ClinicServerInt value = null;

  public ClinicServerIntHolder ()
  {
  }

  public ClinicServerIntHolder (Server_Group.Replica_2.ClinicServer.ClinicServerInt initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Server_Group.Replica_2.ClinicServer.ClinicServerIntHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
	  Server_Group.Replica_2.ClinicServer.ClinicServerIntHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Server_Group.Replica_2.ClinicServer.ClinicServerIntHelper.type ();
  }

}
