/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;
import java.util.*;

// line 110 "MMS.ump"
// line 189 "MMS.ump"
public class Ticket
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Ticket> ticketsByTicketId = new HashMap<String, Ticket>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Ticket Attributes
  private String ticketId;
  private double fee;
  private boolean isActive;

  //Ticket Associations
  private MuseumManagementSystem museumManagementSystem;
  private Client client;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Ticket(String aTicketId, double aFee, boolean aIsActive, MuseumManagementSystem aMuseumManagementSystem, Client aClient)
  {
    fee = aFee;
    isActive = aIsActive;
    if (!setTicketId(aTicketId))
    {
      throw new RuntimeException("Cannot create due to duplicate ticketId. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    boolean didAddMuseumManagementSystem = setMuseumManagementSystem(aMuseumManagementSystem);
    if (!didAddMuseumManagementSystem)
    {
      throw new RuntimeException("Unable to create ticket due to museumManagementSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddClient = setClient(aClient);
    if (!didAddClient)
    {
      throw new RuntimeException("Unable to create ticket due to client. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTicketId(String aTicketId)
  {
    boolean wasSet = false;
    String anOldTicketId = getTicketId();
    if (anOldTicketId != null && anOldTicketId.equals(aTicketId)) {
      return true;
    }
    if (hasWithTicketId(aTicketId)) {
      return wasSet;
    }
    ticketId = aTicketId;
    wasSet = true;
    if (anOldTicketId != null) {
      ticketsByTicketId.remove(anOldTicketId);
    }
    ticketsByTicketId.put(aTicketId, this);
    return wasSet;
  }

  public boolean setFee(double aFee)
  {
    boolean wasSet = false;
    fee = aFee;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsActive(boolean aIsActive)
  {
    boolean wasSet = false;
    isActive = aIsActive;
    wasSet = true;
    return wasSet;
  }

  public String getTicketId()
  {
    return ticketId;
  }
  /* Code from template attribute_GetUnique */
  public static Ticket getWithTicketId(String aTicketId)
  {
    return ticketsByTicketId.get(aTicketId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithTicketId(String aTicketId)
  {
    return getWithTicketId(aTicketId) != null;
  }

  public double getFee()
  {
    return fee;
  }

  public boolean getIsActive()
  {
    return isActive;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsActive()
  {
    return isActive;
  }
  /* Code from template association_GetOne */
  public MuseumManagementSystem getMuseumManagementSystem()
  {
    return museumManagementSystem;
  }
  /* Code from template association_GetOne */
  public Client getClient()
  {
    return client;
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
      existingMuseumManagementSystem.removeTicket(this);
    }
    museumManagementSystem.addTicket(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setClient(Client aClient)
  {
    boolean wasSet = false;
    if (aClient == null)
    {
      return wasSet;
    }

    Client existingClient = client;
    client = aClient;
    if (existingClient != null && !existingClient.equals(aClient))
    {
      existingClient.removeTicket(this);
    }
    client.addTicket(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    ticketsByTicketId.remove(getTicketId());
    MuseumManagementSystem placeholderMuseumManagementSystem = museumManagementSystem;
    this.museumManagementSystem = null;
    if(placeholderMuseumManagementSystem != null)
    {
      placeholderMuseumManagementSystem.removeTicket(this);
    }
    Client placeholderClient = client;
    this.client = null;
    if(placeholderClient != null)
    {
      placeholderClient.removeTicket(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "ticketId" + ":" + getTicketId()+ "," +
            "fee" + ":" + getFee()+ "," +
            "isActive" + ":" + getIsActive()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "museumManagementSystem = "+(getMuseumManagementSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumManagementSystem())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "client = "+(getClient()!=null?Integer.toHexString(System.identityHashCode(getClient())):"null");
  }
}