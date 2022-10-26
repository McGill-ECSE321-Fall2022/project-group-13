/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;
import java.util.*;

// line 103 "MMS.ump"
// line 184 "MMS.ump"
public class Room
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RoomType { Small, Large, Storage }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Room> roomsByRoomId = new HashMap<String, Room>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Room Attributes
  private RoomType type;
  private String roomId;

  //Room Associations
  private MuseumManagementSystem museumManagementSystem;
  private List<Artifact> artifacts;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room(RoomType aType, String aRoomId, MuseumManagementSystem aMuseumManagementSystem)
  {
    type = aType;
    if (!setRoomId(aRoomId))
    {
      throw new RuntimeException("Cannot create due to duplicate roomId. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    boolean didAddMuseumManagementSystem = setMuseumManagementSystem(aMuseumManagementSystem);
    if (!didAddMuseumManagementSystem)
    {
      throw new RuntimeException("Unable to create room due to museumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    artifacts = new ArrayList<Artifact>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setType(RoomType aType)
  {
    boolean wasSet = false;
    type = aType;
    wasSet = true;
    return wasSet;
  }

  public boolean setRoomId(String aRoomId)
  {
    boolean wasSet = false;
    String anOldRoomId = getRoomId();
    if (anOldRoomId != null && anOldRoomId.equals(aRoomId)) {
      return true;
    }
    if (hasWithRoomId(aRoomId)) {
      return wasSet;
    }
    roomId = aRoomId;
    wasSet = true;
    if (anOldRoomId != null) {
      roomsByRoomId.remove(anOldRoomId);
    }
    roomsByRoomId.put(aRoomId, this);
    return wasSet;
  }

  public RoomType getType()
  {
    return type;
  }

  public String getRoomId()
  {
    return roomId;
  }
  /* Code from template attribute_GetUnique */
  public static Room getWithRoomId(String aRoomId)
  {
    return roomsByRoomId.get(aRoomId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithRoomId(String aRoomId)
  {
    return getWithRoomId(aRoomId) != null;
  }
  /* Code from template association_GetOne */
  public MuseumManagementSystem getMuseumManagementSystem()
  {
    return museumManagementSystem;
  }
  /* Code from template association_GetMany */
  public Artifact getArtifact(int index)
  {
    Artifact aArtifact = artifacts.get(index);
    return aArtifact;
  }

  public List<Artifact> getArtifacts()
  {
    List<Artifact> newArtifacts = Collections.unmodifiableList(artifacts);
    return newArtifacts;
  }

  public int numberOfArtifacts()
  {
    int number = artifacts.size();
    return number;
  }

  public boolean hasArtifacts()
  {
    boolean has = artifacts.size() > 0;
    return has;
  }

  public int indexOfArtifact(Artifact aArtifact)
  {
    int index = artifacts.indexOf(aArtifact);
    return index;
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
      existingMuseumManagementSystem.removeRoom(this);
    }
    museumManagementSystem.addRoom(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfArtifacts()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  // public Artifact addArtifact(String aArtifactId, String aName, String aImage, String aDescription, Artifact.LoanStatus aLoanStatus, boolean aIsDamaged, double aLoanFee, double aWorth, MuseumManagementSystem aMuseumManagementSystem)
  // {
  //   return new Artifact(aArtifactId, aName, aImage, aDescription, aLoanStatus, aIsDamaged, aLoanFee, aWorth, this, aMuseumManagementSystem);
  // }

  public boolean addArtifact(Artifact aArtifact)
  {
    boolean wasAdded = false;
    if (artifacts.contains(aArtifact)) { return false; }
    Room existingRoom = aArtifact.getRoom();
    boolean isNewRoom = existingRoom != null && !this.equals(existingRoom);
    if (isNewRoom)
    {
      aArtifact.setRoom(this);
    }
    else
    {
      artifacts.add(aArtifact);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeArtifact(Artifact aArtifact)
  {
    boolean wasRemoved = false;
    //Unable to remove aArtifact, as it must always have a room
    if (!this.equals(aArtifact.getRoom()))
    {
      artifacts.remove(aArtifact);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addArtifactAt(Artifact aArtifact, int index)
  {  
    boolean wasAdded = false;
    if(addArtifact(aArtifact))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfArtifacts()) { index = numberOfArtifacts() - 1; }
      artifacts.remove(aArtifact);
      artifacts.add(index, aArtifact);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveArtifactAt(Artifact aArtifact, int index)
  {
    boolean wasAdded = false;
    if(artifacts.contains(aArtifact))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfArtifacts()) { index = numberOfArtifacts() - 1; }
      artifacts.remove(aArtifact);
      artifacts.add(index, aArtifact);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addArtifactAt(aArtifact, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    roomsByRoomId.remove(getRoomId());
    MuseumManagementSystem placeholderMuseumManagementSystem = museumManagementSystem;
    this.museumManagementSystem = null;
    if(placeholderMuseumManagementSystem != null)
    {
      placeholderMuseumManagementSystem.removeRoom(this);
    }
    for(int i=artifacts.size(); i > 0; i--)
    {
      Artifact aArtifact = artifacts.get(i - 1);
      aArtifact.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "roomId" + ":" + getRoomId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "type" + "=" + (getType() != null ? !getType().equals(this)  ? getType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "museumManagementSystem = "+(getMuseumManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumManagementSystem())):"null");
  }
}