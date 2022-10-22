package ca.mcgill.ecse321.group13_backend.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import java.util.*;
import java.sql.Time;

// line 48 "MMS.ump"
// line 154 "MMS.ump"
public class Shift
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Shift> shiftsByShiftId = new HashMap<String, Shift>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Shift Attributes
  private String shiftId;
  private Time startTime;
  private Time endTime;

  //Shift Associations
  private SpecificWeekDay dayOfTheWeek;
  private MuseumManagementSystem museumManagementSystem;
  private Employee employee;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Shift(String aShiftId, Time aStartTime, Time aEndTime, SpecificWeekDay aDayOfTheWeek, MuseumManagementSystem aMuseumManagementSystem, Employee aEmployee)
  {
    startTime = aStartTime;
    endTime = aEndTime;
    if (!setShiftId(aShiftId))
    {
      throw new RuntimeException("Cannot create due to duplicate shiftId. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    boolean didAddDayOfTheWeek = setDayOfTheWeek(aDayOfTheWeek);
    if (!didAddDayOfTheWeek)
    {
      throw new RuntimeException("Unable to create shift due to dayOfTheWeek. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddMuseumManagementSystem = setMuseumManagementSystem(aMuseumManagementSystem);
    if (!didAddMuseumManagementSystem)
    {
      throw new RuntimeException("Unable to create shift due to museumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddEmployee = setEmployee(aEmployee);
    if (!didAddEmployee)
    {
      throw new RuntimeException("Unable to create shift due to employee. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setShiftId(String aShiftId)
  {
    boolean wasSet = false;
    String anOldShiftId = getShiftId();
    if (anOldShiftId != null && anOldShiftId.equals(aShiftId)) {
      return true;
    }
    if (hasWithShiftId(aShiftId)) {
      return wasSet;
    }
    shiftId = aShiftId;
    wasSet = true;
    if (anOldShiftId != null) {
      shiftsByShiftId.remove(anOldShiftId);
    }
    shiftsByShiftId.put(aShiftId, this);
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime)
  {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public String getShiftId()
  {
    return shiftId;
  }
  /* Code from template attribute_GetUnique */
  public static Shift getWithShiftId(String aShiftId)
  {
    return shiftsByShiftId.get(aShiftId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithShiftId(String aShiftId)
  {
    return getWithShiftId(aShiftId) != null;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }
  /* Code from template association_GetOne */
  public SpecificWeekDay getDayOfTheWeek()
  {
    return dayOfTheWeek;
  }
  /* Code from template association_GetOne */
  public MuseumManagementSystem getMuseumManagementSystem()
  {
    return museumManagementSystem;
  }
  /* Code from template association_GetOne */
  public Employee getEmployee()
  {
    return employee;
  }
  /* Code from template association_SetOneToMany */
  public boolean setDayOfTheWeek(SpecificWeekDay aDayOfTheWeek)
  {
    boolean wasSet = false;
    if (aDayOfTheWeek == null)
    {
      return wasSet;
    }

    SpecificWeekDay existingDayOfTheWeek = dayOfTheWeek;
    dayOfTheWeek = aDayOfTheWeek;
    if (existingDayOfTheWeek != null && !existingDayOfTheWeek.equals(aDayOfTheWeek))
    {
      existingDayOfTheWeek.removeShift(this);
    }
    dayOfTheWeek.addShift(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setMuseumManagementSystem(MuseumManagementSystem aMuseumManagementSystem)
  {
    boolean wasSet = false;
    if (aMuseumManagementSystem == null)
    {
      return wasSet;
    }

    MuseumManagementSystem existingMuseumManagementSystem = museumManagementSystem;
    museumManagementSystem = aMuseumManagementSystem;
    if (existingMuseumManagementSystem != null && !existingMuseumManagementSystem.equals(aMuseumManagementSystem))
    {
      existingMuseumManagementSystem.removeShift(this);
    }
    museumManagementSystem.addShift(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setEmployee(Employee aEmployee)
  {
    boolean wasSet = false;
    if (aEmployee == null)
    {
      return wasSet;
    }

    Employee existingEmployee = employee;
    employee = aEmployee;
    if (existingEmployee != null && !existingEmployee.equals(aEmployee))
    {
      existingEmployee.removeShift(this);
    }
    employee.addShift(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    shiftsByShiftId.remove(getShiftId());
    SpecificWeekDay placeholderDayOfTheWeek = dayOfTheWeek;
    this.dayOfTheWeek = null;
    if(placeholderDayOfTheWeek != null)
    {
      placeholderDayOfTheWeek.removeShift(this);
    }
    MuseumManagementSystem placeholderMuseumManagementSystem = museumManagementSystem;
    this.museumManagementSystem = null;
    if(placeholderMuseumManagementSystem != null)
    {
      placeholderMuseumManagementSystem.removeShift(this);
    }
    Employee placeholderEmployee = employee;
    this.employee = null;
    if(placeholderEmployee != null)
    {
      placeholderEmployee.removeShift(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "shiftId" + ":" + getShiftId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "dayOfTheWeek = "+(getDayOfTheWeek()!=null?Integer.toHexString(System.identityHashCode(getDayOfTheWeek())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "museumManagementSystem = "+(getMuseumManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumManagementSystem())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "employee = "+(getEmployee()!=null?Integer.toHexString(System.identityHashCode(getEmployee())):"null");
  }
}