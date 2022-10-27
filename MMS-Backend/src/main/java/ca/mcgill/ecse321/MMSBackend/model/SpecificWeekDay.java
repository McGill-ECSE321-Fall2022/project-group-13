/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


package ca.mcgill.ecse321.MMSBackend.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

// line 59 "MMS.ump"
// line 154 "MMS.ump"
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
  @ManyToOne(optional=false)
  @JoinColumn(name="museum_fk")
  private MuseumManagementSystem museumManagementSystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // public SpecificWeekDay(boolean aIsClosed, DayType aDayType, MuseumManagementSystem aMuseumManagementSystem)
  // {
  //   isClosed = aIsClosed;
  //   dayType = aDayType;
  //   boolean didAddMuseumManagementSystem = setMuseumManagementSystem(aMuseumManagementSystem);
  //   if (!didAddMuseumManagementSystem)
  //   {
  //     throw new RuntimeException("Unable to create weekDay due to museumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
  //   }
  // }

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

  public void delete()
  {
    MuseumManagementSystem placeholderMuseumManagementSystem = museumManagementSystem;
    this.museumManagementSystem = null;
    if(placeholderMuseumManagementSystem != null)
    {
      placeholderMuseumManagementSystem.removeWeekDay(this);
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