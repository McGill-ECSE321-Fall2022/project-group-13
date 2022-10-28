/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

// line 35 "MMS.ump"
// line 136 "MMS.ump"
// line 193 "MMS.ump"
@Entity
public class Employee extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Employee Associations
  @ManyToOne(optional=false)
  @JoinColumn(name="museum_fk")
  private MuseumManagementSystem museumManagementSystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // public Employee(String aUsername, String aName, String aPassword, MuseumManagementSystem aMuseumManagementSystem)
  // {
  //   super(aUsername, aName, aPassword);
  //   boolean didAddMuseumManagementSystem = setMuseumManagementSystem(aMuseumManagementSystem);
  //   if (!didAddMuseumManagementSystem)
  //   {
  //     throw new RuntimeException("Unable to create employee due to museumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
  //   }
  // }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public MuseumManagementSystem getMuseumManagementSystem()
  {
    return museumManagementSystem;
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
      existingMuseumManagementSystem.removeEmployee(this);
    }
    museumManagementSystem.addEmployee(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    MuseumManagementSystem placeholderMuseumManagementSystem = museumManagementSystem;
    this.museumManagementSystem = null;
    if(placeholderMuseumManagementSystem != null)
    {
      placeholderMuseumManagementSystem.removeEmployee(this);
    }
    super.delete();
  }

}