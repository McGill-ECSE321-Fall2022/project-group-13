/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;
import java.util.*;

// line 88 "MMS.ump"
// line 179 "MMS.ump"
public class Artifact
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum LoanStatus { Available, Unavailable, Loaned }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Artifact> artifactsByArtifactId = new HashMap<String, Artifact>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Artifact Attributes
  private String artifactId;
  private String name;
  private String image;
  private String description;
  private LoanStatus loanStatus;
  private boolean isDamaged;
  private double loanFee;
  private double worth;

  //Artifact Associations
  private List<Request> requests;
  private Room room;
  private MuseumManagementSystem museumManagementSystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Artifact(String aArtifactId, String aName, String aImage, String aDescription, LoanStatus aLoanStatus, boolean aIsDamaged, double aLoanFee, double aWorth, Room aRoom, MuseumManagementSystem aMuseumManagementSystem)
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
    requests = new ArrayList<Request>();
    boolean didAddRoom = setRoom(aRoom);
    if (!didAddRoom)
    {
      throw new RuntimeException("Unable to create artifact due to room. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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

  public boolean setArtifactId(String aArtifactId)
  {
    boolean wasSet = false;
    String anOldArtifactId = getArtifactId();
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

  public String getArtifactId()
  {
    return artifactId;
  }
  /* Code from template attribute_GetUnique */
  public static Artifact getWithArtifactId(String aArtifactId)
  {
    return artifactsByArtifactId.get(aArtifactId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithArtifactId(String aArtifactId)
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
  /* Code from template association_GetMany */
  public Request getRequest(int index)
  {
    Request aRequest = requests.get(index);
    return aRequest;
  }

  public List<Request> getRequests()
  {
    List<Request> newRequests = Collections.unmodifiableList(requests);
    return newRequests;
  }

  public int numberOfRequests()
  {
    int number = requests.size();
    return number;
  }

  public boolean hasRequests()
  {
    boolean has = requests.size() > 0;
    return has;
  }

  public int indexOfRequest(Request aRequest)
  {
    int index = requests.indexOf(aRequest);
    return index;
  }
  /* Code from template association_GetOne */
  public Room getRoom()
  {
    return room;
  }
  /* Code from template association_GetOne */
  public MuseumManagementSystem getMuseumManagementSystem()
  {
    return museumManagementSystem;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRequests()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */


  public boolean addRequest(Request aRequest)
  {
    boolean wasAdded = false;
    if (requests.contains(aRequest)) { return false; }
    Artifact existingArtifact = aRequest.getArtifact();
    boolean isNewArtifact = existingArtifact != null && !this.equals(existingArtifact);
    if (isNewArtifact)
    {
      aRequest.setArtifact(this);
    }
    else
    {
      requests.add(aRequest);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRequest(Request aRequest)
  {
    boolean wasRemoved = false;
    //Unable to remove aRequest, as it must always have a artifact
    if (!this.equals(aRequest.getArtifact()))
    {
      requests.remove(aRequest);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRequestAt(Request aRequest, int index)
  {  
    boolean wasAdded = false;
    if(addRequest(aRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRequests()) { index = numberOfRequests() - 1; }
      requests.remove(aRequest);
      requests.add(index, aRequest);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRequestAt(Request aRequest, int index)
  {
    boolean wasAdded = false;
    if(requests.contains(aRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRequests()) { index = numberOfRequests() - 1; }
      requests.remove(aRequest);
      requests.add(index, aRequest);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRequestAt(aRequest, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setRoom(Room aRoom)
  {
    boolean wasSet = false;
    if (aRoom == null)
    {
      return wasSet;
    }

    Room existingRoom = room;
    room = aRoom;
    if (existingRoom != null && !existingRoom.equals(aRoom))
    {
      existingRoom.removeArtifact(this);
    }
    room.addArtifact(this);
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
      existingMuseumManagementSystem.removeArtifact(this);
    }
    museumManagementSystem.addArtifact(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    artifactsByArtifactId.remove(getArtifactId());
    for(int i=requests.size(); i > 0; i--)
    {
      Request aRequest = requests.get(i - 1);
      aRequest.delete();
    }
    Room placeholderRoom = room;
    this.room = null;
    if(placeholderRoom != null)
    {
      placeholderRoom.removeArtifact(this);
    }
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
            "  " + "room = "+(getRoom()!=null?Integer.toHexString(System.identityHashCode(getRoom())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "museumManagementSystem = "+(getMuseumManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumManagementSystem())):"null");
  }
}