package com;


/**
* com/SuperRecordHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ClinicTasks.idl
* Wednesday, August 3, 2016 7:56:17 PM EDT
*/

abstract public class SuperRecordHelper
{
  private static String  _id = "IDL:com/SuperRecord:1.0";

  public static void insert (org.omg.CORBA.Any a, com.SuperRecord that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static com.SuperRecord extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [13];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "recordID",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[1] = new org.omg.CORBA.StructMember (
            "managerID",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[2] = new org.omg.CORBA.StructMember (
            "firstName",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[3] = new org.omg.CORBA.StructMember (
            "lastName",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[4] = new org.omg.CORBA.StructMember (
            "recType",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[5] = new org.omg.CORBA.StructMember (
            "designation",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[6] = new org.omg.CORBA.StructMember (
            "status",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[7] = new org.omg.CORBA.StructMember (
            "statusDate",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[8] = new org.omg.CORBA.StructMember (
            "category",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[9] = new org.omg.CORBA.StructMember (
            "address",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[10] = new org.omg.CORBA.StructMember (
            "phoneNumber",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[11] = new org.omg.CORBA.StructMember (
            "specialization",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[12] = new org.omg.CORBA.StructMember (
            "location",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (com.SuperRecordHelper.id (), "SuperRecord", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static com.SuperRecord read (org.omg.CORBA.portable.InputStream istream)
  {
    com.SuperRecord value = new com.SuperRecord ();
    value.recordID = istream.read_string ();
    value.managerID = istream.read_string ();
    value.firstName = istream.read_string ();
    value.lastName = istream.read_string ();
    value.recType = istream.read_string ();
    value.designation = istream.read_long ();
    value.status = istream.read_long ();
    value.statusDate = istream.read_string ();
    value.category = istream.read_long ();
    value.address = istream.read_string ();
    value.phoneNumber = istream.read_string ();
    value.specialization = istream.read_long ();
    value.location = istream.read_long ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, com.SuperRecord value)
  {
    ostream.write_string (value.recordID);
    ostream.write_string (value.managerID);
    ostream.write_string (value.firstName);
    ostream.write_string (value.lastName);
    ostream.write_string (value.recType);
    ostream.write_long (value.designation);
    ostream.write_long (value.status);
    ostream.write_string (value.statusDate);
    ostream.write_long (value.category);
    ostream.write_string (value.address);
    ostream.write_string (value.phoneNumber);
    ostream.write_long (value.specialization);
    ostream.write_long (value.location);
  }

}
