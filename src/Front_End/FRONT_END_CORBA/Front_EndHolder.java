package Front_End.FRONT_END_CORBA;

/**
* FRONT_END_CORBA/Front_EndHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Front_End.idl
* Wednesday, July 27, 2016 7:42:35 PM EDT
*/

public final class Front_EndHolder implements org.omg.CORBA.portable.Streamable
{
  public Front_End value = null;

  public Front_EndHolder ()
  {
  }

  public Front_EndHolder (Front_End initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Front_EndHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
	  Front_EndHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Front_EndHelper.type ();
  }

}