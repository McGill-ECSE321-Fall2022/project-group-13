/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;
import java.util.*;
import javax.persistence.*;

// line 105 "MMS.ump"
// line 183 "MMS.ump"

@Entity
public class Room
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RoomType { Small, Large, Storage }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Room> roomsByRoomId = new HashMap<Integer, Room>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Room Attributes
  private RoomType type;
  @Id
  private String name;
  private int roomId;

  //Room Associations
  @ManyToOne(optional = false)
  @JoinColumn(name="museum_fk")
  private MuseumManagementSystem museumManagementSystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

 /* public Room(String aName, RoomType aType, int aRoomId, MuseumManagementSystem aMuseumManagementSystem)
  {
    name = aName;
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
  }*/

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setType(RoomType aType)
  {
    boolean wasSet = false;
    type = aType;
    wasSet = true;
    return wasSet;
  }

  public boolean setRoomId(int aRoomId)
  {
    boolean wasSet = false;
    Integer anOldRoomId = getRoomId();
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

  public String getName()
  {
    return name;
  }

  public RoomType getType()
  {
    return type;
  }

  public int getRoomId()
  {
    return roomId;
  }
  /* Code from template attribute_GetUnique */
  public static Room getWithRoomId(int aRoomId)
  {
    return roomsByRoomId.get(aRoomId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithRoomId(int aRoomId)
  {
    return getWithRoomId(aRoomId) != null;
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
      existingMuseumManagementSystem.removeRoom(this);
    }
    museumManagementSystem.addRoom(this);
    wasSet = true;
    return wasSet;
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
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "roomId" + ":" + getRoomId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "type" + "=" + (getType() != null ? !getType().equals(this)  ? getType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "museumManagementSystem = "+(getMuseumManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumManagementSystem())):"null");
  }
}