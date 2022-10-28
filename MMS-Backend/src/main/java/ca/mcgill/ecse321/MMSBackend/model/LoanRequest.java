/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

// line 72 "MMS.ump"
// line 164 "MMS.ump"
@Entity
public class LoanRequest extends Request
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum LoanStatus { Approved, Rejected, Pending, Returned }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //LoanRequest Attributes
  private int loanDuration;
  private double fee;
  private LoanStatus status;

  //LoanRequest Associations
  @ManyToOne(optional = false)
  @JoinColumn(name="museum_fk")
  private MuseumManagementSystem museumManagementSystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // public LoanRequest(int aRequestId, Client aClient, Artifact aArtifact, int aLoanDuration, double aFee, LoanStatus aStatus, MuseumManagementSystem aMuseumManagementSystem)
  // {
  //   super(aRequestId, aClient, aArtifact);
  //   loanDuration = aLoanDuration;
  //   fee = aFee;
  //   status = aStatus;
  //   boolean didAddMuseumManagementSystem = setMuseumManagementSystem(aMuseumManagementSystem);
  //   if (!didAddMuseumManagementSystem)
  //   {
  //     throw new RuntimeException("Unable to create loanRequest due to museumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
  //   }
  // }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLoanDuration(int aLoanDuration)
  {
    boolean wasSet = false;
    loanDuration = aLoanDuration;
    wasSet = true;
    return wasSet;
  }

  public boolean setFee(double aFee)
  {
    boolean wasSet = false;
    fee = aFee;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(LoanStatus aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public int getLoanDuration()
  {
    return loanDuration;
  }

  public double getFee()
  {
    return fee;
  }

  public LoanStatus getStatus()
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
      existingMuseumManagementSystem.removeLoanRequest(this);
    }
    museumManagementSystem.addLoanRequest(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    MuseumManagementSystem placeholderMuseumManagementSystem = museumManagementSystem;
    this.museumManagementSystem = null;
    if(placeholderMuseumManagementSystem != null)
    {
      placeholderMuseumManagementSystem.removeLoanRequest(this);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "loanDuration" + ":" + getLoanDuration()+ "," +
            "fee" + ":" + getFee()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "museumManagementSystem = "+(getMuseumManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumManagementSystem())):"null");
  }
}