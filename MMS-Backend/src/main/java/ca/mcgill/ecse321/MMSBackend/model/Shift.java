/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;
import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.sql.Time;

// line 50 "MMS.ump"
// line 148 "MMS.ump"
@Entity
public class Shift
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum DayType { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Shift> shiftsByShiftId = new HashMap<Integer, Shift>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Shift Attributes
  @Id
  private int shiftId;
  private Time startTime;
  private Time endTime;
  private DayType dayType;
  private boolean isActive;

  //Shift Associations
  private MuseumManagementSystem museumManagementSystem;
  @ManyToOne(optional=false)
  private Employee employee;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // public Shift(int aShiftId, Time aStartTime, Time aEndTime, DayType aDayType, boolean aIsActive, MuseumManagementSystem aMuseumManagementSystem, Employee aEmployee)
  // {
  //   startTime = aStartTime;
  //   endTime = aEndTime;
  //   dayType = aDayType;
  //   isActive = aIsActive;
  //   if (!setShiftId(aShiftId))
  //   {
  //     throw new RuntimeException("Cannot create due to duplicate shiftId. See http://manual.umple.org?RE003ViolationofUniqueness.html");
  //   }
  //   boolean didAddMuseumManagementSystem = setMuseumManagementSystem(aMuseumManagementSystem);
  //   if (!didAddMuseumManagementSystem)
  //   {
  //     throw new RuntimeException("Unable to create shift due to museumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
  //   }
  //   if (!setEmployee(aEmployee))
  //   {
  //     throw new RuntimeException("Unable to create Shift due to aEmployee. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
  //   }
  // }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setShiftId(int aShiftId)
  {
    boolean wasSet = false;
    Integer anOldShiftId = getShiftId();
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

  public boolean setDayType(DayType aDayType)
  {
    boolean wasSet = false;
    dayType = aDayType;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsActive(boolean aIsActive)
  {
    boolean wasSet = false;
    isActive = aIsActive;
    wasSet = true;
    return wasSet;
  }

  public int getShiftId()
  {
    return shiftId;
  }
  /* Code from template attribute_GetUnique */
  public static Shift getWithShiftId(int aShiftId)
  {
    return shiftsByShiftId.get(aShiftId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithShiftId(int aShiftId)
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

  public DayType getDayType()
  {
    return dayType;
  }

  public boolean getIsActive()
  {
    return isActive;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsActive()
  {
    return isActive;
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
  /* Code from template association_SetUnidirectionalOne */
  public boolean setEmployee(Employee aNewEmployee)
  {
    boolean wasSet = false;
    if (aNewEmployee != null)
    {
      employee = aNewEmployee;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    shiftsByShiftId.remove(getShiftId());
    MuseumManagementSystem placeholderMuseumManagementSystem = museumManagementSystem;
    this.museumManagementSystem = null;
    if(placeholderMuseumManagementSystem != null)
    {
      placeholderMuseumManagementSystem.removeShift(this);
    }
    employee = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "shiftId" + ":" + getShiftId()+ "," +
            "isActive" + ":" + getIsActive()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "dayType" + "=" + (getDayType() != null ? !getDayType().equals(this)  ? getDayType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "museumManagementSystem = "+(getMuseumManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumManagementSystem())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "employee = "+(getEmployee()!=null?Integer.toHexString(System.identityHashCode(getEmployee())):"null");
  }
}