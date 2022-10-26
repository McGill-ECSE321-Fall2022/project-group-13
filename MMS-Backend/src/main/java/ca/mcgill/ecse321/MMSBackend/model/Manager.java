/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;
// import java.util.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

// import java.sql.Time;

// line 28 "MMS.ump"
// line 138 "MMS.ump"
@Entity
@DiscriminatorValue("Manager")
public class Manager extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Manager Associations
  private MuseumManagementSystem museumManagementSystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // public Manager(String aUsername, String aName, String aPassword, MuseumManagementSystem aMuseumManagementSystem)
  // {
  //   super(aUsername, aName, aPassword);
  //   if (aMuseumManagementSystem == null || aMuseumManagementSystem.getManager() != null)
  //   {
  //     throw new RuntimeException("Unable to create Manager due to aMuseumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
  //   }
  //   museumManagementSystem = aMuseumManagementSystem;
  // }

  // public Manager(String aUsername, String aName, String aPassword, Time aOpenTimeForMuseumManagementSystem, Time aCloseTimeForMuseumManagementSystem, int aMaxLoanNumberForMuseumManagementSystem, double aTicketFeeForMuseumManagementSystem)
  // {
  //   super(aUsername, aName, aPassword);
  //   museumManagementSystem = new MuseumManagementSystem(aOpenTimeForMuseumManagementSystem, aCloseTimeForMuseumManagementSystem, aMaxLoanNumberForMuseumManagementSystem, aTicketFeeForMuseumManagementSystem, this);
  // }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public MuseumManagementSystem getMuseumManagementSystem()
  {
    return museumManagementSystem;
  }

  public void delete()
  {
    MuseumManagementSystem existingMuseumManagementSystem = museumManagementSystem;
    museumManagementSystem = null;
    if (existingMuseumManagementSystem != null)
    {
      existingMuseumManagementSystem.delete();
    }
    super.delete();
  }

}