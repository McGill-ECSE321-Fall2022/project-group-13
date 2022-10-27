/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;

import java.util.*;

// line 66 "MMS.ump"
// line 158 "MMS.ump"
public abstract class Request
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Request> requestsByRequestId = new HashMap<String, Request>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Request Attributes
  private String requestId;

  //Request Associations
  private Client client;
  private Artifact artifact;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Request(String aRequestId, Client aClient, Artifact aArtifact)
  {
    if (!setRequestId(aRequestId))
    {
      throw new RuntimeException("Cannot create due to duplicate requestId. See http://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    boolean didAddClient = setClient(aClient);
    if (!didAddClient)
    {
      throw new RuntimeException("Unable to create request due to client. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddArtifact = setArtifact(aArtifact);
    if (!didAddArtifact)
    {
      throw new RuntimeException("Unable to create request due to artifact. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRequestId(String aRequestId)
  {
    boolean wasSet = false;
    String anOldRequestId = getRequestId();
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

  public String getRequestId()
  {
    return requestId;
  }
  /* Code from template attribute_GetUnique */
  public static Request getWithRequestId(String aRequestId)
  {
    return requestsByRequestId.get(aRequestId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithRequestId(String aRequestId)
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
      existingClient.removeRequest(this);
    }
    client.addRequest(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setArtifact(Artifact aArtifact)
  {
    boolean wasSet = false;
    if (aArtifact == null)
    {
      return wasSet;
    }

    Artifact existingArtifact = artifact;
    artifact = aArtifact;
    if (existingArtifact != null && !existingArtifact.equals(aArtifact))
    {
      existingArtifact.removeRequest(this);
    }
    artifact.addRequest(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    requestsByRequestId.remove(getRequestId());
    Client placeholderClient = client;
    this.client = null;
    if(placeholderClient != null)
    {
      placeholderClient.removeRequest(this);
    }
    Artifact placeholderArtifact = artifact;
    this.artifact = null;
    if(placeholderArtifact != null)
    {
      placeholderArtifact.removeRequest(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "requestId" + ":" + getRequestId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "client = "+(getClient()!=null?Integer.toHexString(System.identityHashCode(getClient())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "artifact = "+(getArtifact()!=null?Integer.toHexString(System.identityHashCode(getArtifact())):"null");
  }
}