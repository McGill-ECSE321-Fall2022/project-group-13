/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;

import java.util.*;
import java.sql.Time;

// line 59 "MMS.ump"
// line 153 "MMS.ump"
public class SpecificWeekDay
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum DayType { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SpecificWeekDay Attributes
  private boolean isClosed;
  private DayType dayType;

  //SpecificWeekDay Associations
  private MuseumManagementSystem museumManagementSystem;
  private List<Shift> shifts;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SpecificWeekDay(boolean aIsClosed, DayType aDayType, MuseumManagementSystem aMuseumManagementSystem)
  {
    isClosed = aIsClosed;
    dayType = aDayType;
    boolean didAddMuseumManagementSystem = setMuseumManagementSystem(aMuseumManagementSystem);
    if (!didAddMuseumManagementSystem)
    {
      throw new RuntimeException("Unable to create weekDay due to museumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    shifts = new ArrayList<Shift>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIsClosed(boolean aIsClosed)
  {
    boolean wasSet = false;
    isClosed = aIsClosed;
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

  public boolean getIsClosed()
  {
    return isClosed;
  }

  public DayType getDayType()
  {
    return dayType;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsClosed()
  {
    return isClosed;
  }
  /* Code from template association_GetOne */
  public MuseumManagementSystem getMuseumManagementSystem()
  {
    return museumManagementSystem;
  }
  /* Code from template association_GetMany */
  public Shift getShift(int index)
  {
    Shift aShift = shifts.get(index);
    return aShift;
  }

  public List<Shift> getShifts()
  {
    List<Shift> newShifts = Collections.unmodifiableList(shifts);
    return newShifts;
  }

  public int numberOfShifts()
  {
    int number = shifts.size();
    return number;
  }

  public boolean hasShifts()
  {
    boolean has = shifts.size() > 0;
    return has;
  }

  public int indexOfShift(Shift aShift)
  {
    int index = shifts.indexOf(aShift);
    return index;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setMuseumManagementSystem(MuseumManagementSystem aMuseumManagementSystem)
  {
    boolean wasSet = false;
    //Must provide museumManagementSystem to weekDay
    if (aMuseumManagementSystem == null)
    {
      return wasSet;
    }

    //museumManagementSystem already at maximum (7)
    if (aMuseumManagementSystem.numberOfWeekDays() >= MuseumManagementSystem.maximumNumberOfWeekDays())
    {
      return wasSet;
    }
    
    MuseumManagementSystem existingMuseumManagementSystem = museumManagementSystem;
    museumManagementSystem = aMuseumManagementSystem;
    if (existingMuseumManagementSystem != null && !existingMuseumManagementSystem.equals(aMuseumManagementSystem))
    {
      boolean didRemove = existingMuseumManagementSystem.removeWeekDay(this);
      if (!didRemove)
      {
        museumManagementSystem = existingMuseumManagementSystem;
        return wasSet;
      }
    }
    museumManagementSystem.addWeekDay(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfShifts()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Shift addShift(String aShiftId, Time aStartTime, Time aEndTime, MuseumManagementSystem aMuseumManagementSystem, Employee aEmployee)
  {
    return new Shift(aShiftId, aStartTime, aEndTime, this, aMuseumManagementSystem, aEmployee);
  }

  public boolean addShift(Shift aShift)
  {
    boolean wasAdded = false;
    if (shifts.contains(aShift)) { return false; }
    SpecificWeekDay existingDayOfTheWeek = aShift.getDayOfTheWeek();
    boolean isNewDayOfTheWeek = existingDayOfTheWeek != null && !this.equals(existingDayOfTheWeek);
    if (isNewDayOfTheWeek)
    {
      aShift.setDayOfTheWeek(this);
    }
    else
    {
      shifts.add(aShift);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeShift(Shift aShift)
  {
    boolean wasRemoved = false;
    //Unable to remove aShift, as it must always have a dayOfTheWeek
    if (!this.equals(aShift.getDayOfTheWeek()))
    {
      shifts.remove(aShift);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addShiftAt(Shift aShift, int index)
  {  
    boolean wasAdded = false;
    if(addShift(aShift))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShifts()) { index = numberOfShifts() - 1; }
      shifts.remove(aShift);
      shifts.add(index, aShift);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveShiftAt(Shift aShift, int index)
  {
    boolean wasAdded = false;
    if(shifts.contains(aShift))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShifts()) { index = numberOfShifts() - 1; }
      shifts.remove(aShift);
      shifts.add(index, aShift);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addShiftAt(aShift, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    MuseumManagementSystem placeholderMuseumManagementSystem = museumManagementSystem;
    this.museumManagementSystem = null;
    if(placeholderMuseumManagementSystem != null)
    {
      placeholderMuseumManagementSystem.removeWeekDay(this);
    }
    for(int i=shifts.size(); i > 0; i--)
    {
      Shift aShift = shifts.get(i - 1);
      aShift.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "isClosed" + ":" + getIsClosed()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "dayType" + "=" + (getDayType() != null ? !getDayType().equals(this)  ? getDayType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "museumManagementSystem = "+(getMuseumManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumManagementSystem())):"null");
  }
}