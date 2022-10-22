package ca.mcgill.ecse321.group13_backend.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/


import java.util.*;

// line 81 "MMS.ump"
// line 174 "MMS.ump"
public class DonationRequest extends Request
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum DonationStatus { Approved, Rejected, Pending, Returned }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //DonationRequest Attributes
  private DonationStatus status;

  //DonationRequest Associations
  private MuseumManagementSystem museumManagementSystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public DonationRequest(String aRequestId, Client aClient, Artifact aArtifact, DonationStatus aStatus, MuseumManagementSystem aMuseumManagementSystem)
  {
    super(aRequestId, aClient, aArtifact);
    status = aStatus;
    boolean didAddMuseumManagementSystem = setMuseumManagementSystem(aMuseumManagementSystem);
    if (!didAddMuseumManagementSystem)
    {
      throw new RuntimeException("Unable to create donationRequest due to museumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStatus(DonationStatus aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public DonationStatus getStatus()
  {
    return status;
  }
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
      existingMuseumManagementSystem.removeDonationRequest(this);
    }
    museumManagementSystem.addDonationRequest(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    MuseumManagementSystem placeholderMuseumManagementSystem = museumManagementSystem;
    this.museumManagementSystem = null;
    if(placeholderMuseumManagementSystem != null)
    {
      placeholderMuseumManagementSystem.removeDonationRequest(this);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "museumManagementSystem = "+(getMuseumManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumManagementSystem())):"null");
  }
}