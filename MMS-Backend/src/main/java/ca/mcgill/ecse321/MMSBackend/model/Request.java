/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;
import java.util.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

// line 66 "MMS.ump"
// line 159 "MMS.ump"
@MappedSuperclass
public abstract class Request
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Request> requestsByRequestId = new HashMap<Integer, Request>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Request Attributes
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private int requestId;

  //Request Associations
  @ManyToOne(optional=false)
  private Client client;
  @ManyToOne(optional=false)
  private Artifact artifact;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  // public Request(int aRequestId, Client aClient, Artifact aArtifact)
  // {
  //   if (!setRequestId(aRequestId))
  //   {
  //     throw new RuntimeException("Cannot create due to duplicate requestId. See http://manual.umple.org?RE003ViolationofUniqueness.html");
  //   }
  //   if (!setClient(aClient))
  //   {
  //     throw new RuntimeException("Unable to create Request due to aClient. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
  //   }
  //   if (!setArtifact(aArtifact))
  //   {
  //     throw new RuntimeException("Unable to create Request due to aArtifact. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
  //   }
  // }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRequestId(int aRequestId)
  {
    boolean wasSet = false;
    Integer anOldRequestId = getRequestId();
    if (anOldRequestId != null && anOldRequestId.equals(aRequestId)) {
      return true;
    }
    if (hasWithRequestId(aRequestId)) {
      return wasSet;
    }
    requestId = aRequestId;
    wasSet = true;
    if (anOldRequestId != null) {
      requestsByRequestId.remove(anOldRequestId);
    }
    requestsByRequestId.put(aRequestId, this);
    return wasSet;
  }

  public int getRequestId()
  {
    return requestId;
  }
  /* Code from template attribute_GetUnique */
  public static Request getWithRequestId(int aRequestId)
  {
    return requestsByRequestId.get(aRequestId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithRequestId(int aRequestId)
  {
    return getWithRequestId(aRequestId) != null;
  }
  /* Code from template association_GetOne */
  public Client getClient()
  {
    return client;
  }
  /* Code from template association_GetOne */
  public Artifact getArtifact()
  {
    return artifact;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setClient(Client aNewClient)
  {
    boolean wasSet = false;
    if (aNewClient != null)
    {
      client = aNewClient;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setArtifact(Artifact aNewArtifact)
  {
    boolean wasSet = false;
    if (aNewArtifact != null)
    {
      artifact = aNewArtifact;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    requestsByRequestId.remove(getRequestId());
    client = null;
    artifact = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "requestId" + ":" + getRequestId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "client = "+(getClient()!=null?Integer.toHexString(System.identityHashCode(getClient())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "artifact = "+(getArtifact()!=null?Integer.toHexString(System.identityHashCode(getArtifact())):"null");
  }
}