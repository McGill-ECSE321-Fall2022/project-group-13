/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;
import java.util.*;

// line 90 "MMS.ump"
// line 174 "MMS.ump"
public class Artifact
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum LoanStatus { Available, Unavailable, Loaned }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Artifact> artifactsByArtifactId = new HashMap<Integer, Artifact>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Artifact Attributes
  private int artifactId;
  private String name;
  private String image;
  private String description;
  private LoanStatus loanStatus;
  private boolean isDamaged;
  private double loanFee;
  private double worth;

  //Artifact Associations
  private Room roomLocation;
  private MuseumManagementSystem museumManagementSystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Artifact(int aArtifactId, String aName, String aImage, String aDescription, LoanStatus aLoanStatus, boolean aIsDamaged, double aLoanFee, double aWorth, Room aRoomLocation, MuseumManagementSystem aMuseumManagementSystem)
  {
    name = aName;
    image = aImage;
    description = aDescription;
    loanStatus = aLoanStatus;
    isDamaged = aIsDamaged;
    loanFee = aLoanFee;
    worth = aWorth;
    if (!setArtifactId(aArtifactId))
    {
      throw new RuntimeException("Cannot create due to duplicate artifactId. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    if (!setRoomLocation(aRoomLocation))
    {
      throw new RuntimeException("Unable to create Artifact due to aRoomLocation. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddMuseumManagementSystem = setMuseumManagementSystem(aMuseumManagementSystem);
    if (!didAddMuseumManagementSystem)
    {
      throw new RuntimeException("Unable to create artifact due to museumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setArtifactId(int aArtifactId)
  {
    boolean wasSet = false;
    Integer anOldArtifactId = getArtifactId();
    if (anOldArtifactId != null && anOldArtifactId.equals(aArtifactId)) {
      return true;
    }
    if (hasWithArtifactId(aArtifactId)) {
      return wasSet;
    }
    artifactId = aArtifactId;
    wasSet = true;
    if (anOldArtifactId != null) {
      artifactsByArtifactId.remove(anOldArtifactId);
    }
    artifactsByArtifactId.put(aArtifactId, this);
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setImage(String aImage)
  {
    boolean wasSet = false;
    image = aImage;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setLoanStatus(LoanStatus aLoanStatus)
  {
    boolean wasSet = false;
    loanStatus = aLoanStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsDamaged(boolean aIsDamaged)
  {
    boolean wasSet = false;
    isDamaged = aIsDamaged;
    wasSet = true;
    return wasSet;
  }

  public boolean setLoanFee(double aLoanFee)
  {
    boolean wasSet = false;
    loanFee = aLoanFee;
    wasSet = true;
    return wasSet;
  }

  public boolean setWorth(double aWorth)
  {
    boolean wasSet = false;
    worth = aWorth;
    wasSet = true;
    return wasSet;
  }

  public int getArtifactId()
  {
    return artifactId;
  }
  /* Code from template attribute_GetUnique */
  public static Artifact getWithArtifactId(int aArtifactId)
  {
    return artifactsByArtifactId.get(aArtifactId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithArtifactId(int aArtifactId)
  {
    return getWithArtifactId(aArtifactId) != null;
  }

  public String getName()
  {
    return name;
  }

  public String getImage()
  {
    return image;
  }

  public String getDescription()
  {
    return description;
  }

  public LoanStatus getLoanStatus()
  {
    return loanStatus;
  }

  public boolean getIsDamaged()
  {
    return isDamaged;
  }

  public double getLoanFee()
  {
    return loanFee;
  }

  public double getWorth()
  {
    return worth;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsDamaged()
  {
    return isDamaged;
  }
  /* Code from template association_GetOne */
  public Room getRoomLocation()
  {
    return roomLocation;
  }
  /* Code from template association_GetOne */
  public MuseumManagementSystem getMuseumManagementSystem()
  {
    return museumManagementSystem;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setRoomLocation(Room aNewRoomLocation)
  {
    boolean wasSet = false;
    if (aNewRoomLocation != null)
    {
      roomLocation = aNewRoomLocation;
      wasSet = true;
    }
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
      existingMuseumManagementSystem.removeArtifact(this);
    }
    museumManagementSystem.addArtifact(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    artifactsByArtifactId.remove(getArtifactId());
    roomLocation = null;
    MuseumManagementSystem placeholderMuseumManagementSystem = museumManagementSystem;
    this.museumManagementSystem = null;
    if(placeholderMuseumManagementSystem != null)
    {
      placeholderMuseumManagementSystem.removeArtifact(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "artifactId" + ":" + getArtifactId()+ "," +
            "name" + ":" + getName()+ "," +
            "image" + ":" + getImage()+ "," +
            "description" + ":" + getDescription()+ "," +
            "isDamaged" + ":" + getIsDamaged()+ "," +
            "loanFee" + ":" + getLoanFee()+ "," +
            "worth" + ":" + getWorth()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "loanStatus" + "=" + (getLoanStatus() != null ? !getLoanStatus().equals(this)  ? getLoanStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "roomLocation = "+(getRoomLocation()!=null?Integer.toHexString(System.identityHashCode(getRoomLocation())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "museumManagementSystem = "+(getMuseumManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumManagementSystem())):"null");
  }
}