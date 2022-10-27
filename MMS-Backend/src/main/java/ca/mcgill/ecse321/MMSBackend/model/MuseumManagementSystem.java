/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MMSBackend.model;
import java.util.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.sql.Time;

// line 1 "MMS.ump"
// line 199 "MMS.ump"
public class MuseumManagementSystem
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, MuseumManagementSystem> museummanagementsystemsBySystemId = new HashMap<Integer, MuseumManagementSystem>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //MuseumManagementSystem Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int systemId;
  private String name;
  private Time openTime;
  private Time closeTime;
  private int maxLoanNumber;
  private double ticketFee;

  //MuseumManagementSystem Associations
  @OneToOne(mappedBy="museumManagementSystem")
  private Manager manager;
  @OneToMany(mappedBy="museumManagementSystem")
  private List<Employee> employees;
  @OneToMany(mappedBy="museumManagementSystem")
  private List<Client> clients;
  @OneToMany(mappedBy="museumManagementSystem")
  private List<Shift> shifts;
  @OneToMany(mappedBy="museumManagementSystem")
  private List<LoanRequest> loanRequests;
  @OneToMany(mappedBy="museumManagementSystem")
  private List<DonationRequest> donationRequests;
  @OneToMany(mappedBy="museumManagementSystem")
  private List<Artifact> artifacts;
  @OneToMany(mappedBy="museumManagementSystem")
  private List<Room> rooms;
  @OneToMany(mappedBy="museumManagementSystem")
  private List<Ticket> tickets;
  @OneToMany(mappedBy="museumManagementSystem")
  private List<SpecificWeekDay> weekDays;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public MuseumManagementSystem(){
    employees = new ArrayList<Employee>();
    clients = new ArrayList<Client>();
    shifts = new ArrayList<Shift>();
    loanRequests = new ArrayList<LoanRequest>();
    donationRequests = new ArrayList<DonationRequest>();
    artifacts = new ArrayList<Artifact>();
    rooms = new ArrayList<Room>();
    tickets = new ArrayList<Ticket>();
    weekDays = new ArrayList<SpecificWeekDay>();
  }

  // public MuseumManagementSystem(int aSystemId, String aName, Time aOpenTime, Time aCloseTime, int aMaxLoanNumber, double aTicketFee, Manager aManager)
  // {
  //   name = aName;
  //   openTime = aOpenTime;
  //   closeTime = aCloseTime;
  //   maxLoanNumber = aMaxLoanNumber;
  //   ticketFee = aTicketFee;
  //   if (!setSystemId(aSystemId))
  //   {
  //     throw new RuntimeException("Cannot create due to duplicate systemId. See http://manual.umple.org?RE003ViolationofUniqueness.html");
  //   }
  //   if (aManager == null || aManager.getMuseumManagementSystem() != null)
  //   {
  //     throw new RuntimeException("Unable to create MuseumManagementSystem due to aManager. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
  //   }
  //   manager = aManager;
  //   employees = new ArrayList<Employee>();
  //   clients = new ArrayList<Client>();
  //   shifts = new ArrayList<Shift>();
  //   loanRequests = new ArrayList<LoanRequest>();
  //   donationRequests = new ArrayList<DonationRequest>();
  //   artifacts = new ArrayList<Artifact>();
  //   rooms = new ArrayList<Room>();
  //   tickets = new ArrayList<Ticket>();
  //   weekDays = new ArrayList<SpecificWeekDay>();
  // }

  // public MuseumManagementSystem(int aSystemId, String aName, Time aOpenTime, Time aCloseTime, int aMaxLoanNumber, double aTicketFee, String aUsernameForManager, String aNameForManager, String aPasswordForManager)
  // {
  //   if (!setSystemId(aSystemId))
  //   {
  //     throw new RuntimeException("Cannot create due to duplicate systemId. See http://manual.umple.org?RE003ViolationofUniqueness.html");
  //   }
  //   name = aName;
  //   openTime = aOpenTime;
  //   closeTime = aCloseTime;
  //   maxLoanNumber = aMaxLoanNumber;
  //   ticketFee = aTicketFee;
  //   manager = new Manager(aUsernameForManager, aNameForManager, aPasswordForManager, this);
  //   employees = new ArrayList<Employee>();
  //   clients = new ArrayList<Client>();
  //   shifts = new ArrayList<Shift>();
  //   loanRequests = new ArrayList<LoanRequest>();
  //   donationRequests = new ArrayList<DonationRequest>();
  //   artifacts = new ArrayList<Artifact>();
  //   rooms = new ArrayList<Room>();
  //   tickets = new ArrayList<Ticket>();
  //   weekDays = new ArrayList<SpecificWeekDay>();
  // }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSystemId(int aSystemId)
  {
    boolean wasSet = false;
    Integer anOldSystemId = getSystemId();
    if (anOldSystemId != null && anOldSystemId.equals(aSystemId)) {
      return true;
    }
    if (hasWithSystemId(aSystemId)) {
      return wasSet;
    }
    systemId = aSystemId;
    wasSet = true;
    if (anOldSystemId != null) {
      museummanagementsystemsBySystemId.remove(anOldSystemId);
    }
    museummanagementsystemsBySystemId.put(aSystemId, this);
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setOpenTime(Time aOpenTime)
  {
    boolean wasSet = false;
    openTime = aOpenTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setCloseTime(Time aCloseTime)
  {
    boolean wasSet = false;
    closeTime = aCloseTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setMaxLoanNumber(int aMaxLoanNumber)
  {
    boolean wasSet = false;
    maxLoanNumber = aMaxLoanNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setTicketFee(double aTicketFee)
  {
    boolean wasSet = false;
    ticketFee = aTicketFee;
    wasSet = true;
    return wasSet;
  }

  public int getSystemId()
  {
    return systemId;
  }
  /* Code from template attribute_GetUnique */
  public static MuseumManagementSystem getWithSystemId(int aSystemId)
  {
    return museummanagementsystemsBySystemId.get(aSystemId);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithSystemId(int aSystemId)
  {
    return getWithSystemId(aSystemId) != null;
  }

  public String getName()
  {
    return name;
  }

  public Time getOpenTime()
  {
    return openTime;
  }

  public Time getCloseTime()
  {
    return closeTime;
  }

  public int getMaxLoanNumber()
  {
    return maxLoanNumber;
  }

  public double getTicketFee()
  {
    return ticketFee;
  }
  /* Code from template association_GetOne */
  public Manager getManager()
  {
    return manager;
  }
  /* Code from template association_GetMany */
  public Employee getEmployee(int index)
  {
    Employee aEmployee = employees.get(index);
    return aEmployee;
  }

  public List<Employee> getEmployees()
  {
    List<Employee> newEmployees = Collections.unmodifiableList(employees);
    return newEmployees;
  }

  public int numberOfEmployees()
  {
    int number = employees.size();
    return number;
  }

  public boolean hasEmployees()
  {
    boolean has = employees.size() > 0;
    return has;
  }

  public int indexOfEmployee(Employee aEmployee)
  {
    int index = employees.indexOf(aEmployee);
    return index;
  }
  /* Code from template association_GetMany */
  public Client getClient(int index)
  {
    Client aClient = clients.get(index);
    return aClient;
  }

  public List<Client> getClients()
  {
    List<Client> newClients = Collections.unmodifiableList(clients);
    return newClients;
  }

  public int numberOfClients()
  {
    int number = clients.size();
    return number;
  }

  public boolean hasClients()
  {
    boolean has = clients.size() > 0;
    return has;
  }

  public int indexOfClient(Client aClient)
  {
    int index = clients.indexOf(aClient);
    return index;
  }
  /* Code from template association_GetMany */
  public Shift getShift(int index)
  {
    Shift aShift = shifts.get(index);
    return aShift;
  }

  public List<Shift> getShifts()
  {
    List<Shift> newShifts = Collections.unmodifiableList(shifts);
    return newShifts;
  }

  public int numberOfShifts()
  {
    int number = shifts.size();
    return number;
  }

  public boolean hasShifts()
  {
    boolean has = shifts.size() > 0;
    return has;
  }

  public int indexOfShift(Shift aShift)
  {
    int index = shifts.indexOf(aShift);
    return index;
  }
  /* Code from template association_GetMany */
  public LoanRequest getLoanRequest(int index)
  {
    LoanRequest aLoanRequest = loanRequests.get(index);
    return aLoanRequest;
  }

  public List<LoanRequest> getLoanRequests()
  {
    List<LoanRequest> newLoanRequests = Collections.unmodifiableList(loanRequests);
    return newLoanRequests;
  }

  public int numberOfLoanRequests()
  {
    int number = loanRequests.size();
    return number;
  }

  public boolean hasLoanRequests()
  {
    boolean has = loanRequests.size() > 0;
    return has;
  }

  public int indexOfLoanRequest(LoanRequest aLoanRequest)
  {
    int index = loanRequests.indexOf(aLoanRequest);
    return index;
  }
  /* Code from template association_GetMany */
  public DonationRequest getDonationRequest(int index)
  {
    DonationRequest aDonationRequest = donationRequests.get(index);
    return aDonationRequest;
  }

  public List<DonationRequest> getDonationRequests()
  {
    List<DonationRequest> newDonationRequests = Collections.unmodifiableList(donationRequests);
    return newDonationRequests;
  }

  public int numberOfDonationRequests()
  {
    int number = donationRequests.size();
    return number;
  }

  public boolean hasDonationRequests()
  {
    boolean has = donationRequests.size() > 0;
    return has;
  }

  public int indexOfDonationRequest(DonationRequest aDonationRequest)
  {
    int index = donationRequests.indexOf(aDonationRequest);
    return index;
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
  /* Code from template association_GetMany */
  public Room getRoom(int index)
  {
    Room aRoom = rooms.get(index);
    return aRoom;
  }

  public List<Room> getRooms()
  {
    List<Room> newRooms = Collections.unmodifiableList(rooms);
    return newRooms;
  }

  public int numberOfRooms()
  {
    int number = rooms.size();
    return number;
  }

  public boolean hasRooms()
  {
    boolean has = rooms.size() > 0;
    return has;
  }

  public int indexOfRoom(Room aRoom)
  {
    int index = rooms.indexOf(aRoom);
    return index;
  }
  /* Code from template association_GetMany */
  public Ticket getTicket(int index)
  {
    Ticket aTicket = tickets.get(index);
    return aTicket;
  }

  public List<Ticket> getTickets()
  {
    List<Ticket> newTickets = Collections.unmodifiableList(tickets);
    return newTickets;
  }

  public int numberOfTickets()
  {
    int number = tickets.size();
    return number;
  }

  public boolean hasTickets()
  {
    boolean has = tickets.size() > 0;
    return has;
  }

  public int indexOfTicket(Ticket aTicket)
  {
    int index = tickets.indexOf(aTicket);
    return index;
  }
  /* Code from template association_GetMany */
  public SpecificWeekDay getWeekDay(int index)
  {
    SpecificWeekDay aWeekDay = weekDays.get(index);
    return aWeekDay;
  }

  public List<SpecificWeekDay> getWeekDays()
  {
    List<SpecificWeekDay> newWeekDays = Collections.unmodifiableList(weekDays);
    return newWeekDays;
  }

  public int numberOfWeekDays()
  {
    int number = weekDays.size();
    return number;
  }

  public boolean hasWeekDays()
  {
    boolean has = weekDays.size() > 0;
    return has;
  }

  public int indexOfWeekDay(SpecificWeekDay aWeekDay)
  {
    int index = weekDays.indexOf(aWeekDay);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEmployees()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  // public Employee addEmployee(String aUsername, String aName, String aPassword)
  // {
  //   return new Employee(aUsername, aName, aPassword, this);
  // }

  public boolean addEmployee(Employee aEmployee)
  {
    boolean wasAdded = false;
    if (employees.contains(aEmployee)) { return false; }
    MuseumManagementSystem existingMuseumManagementSystem = aEmployee.getMuseumManagementSystem();
    boolean isNewMuseumManagementSystem = existingMuseumManagementSystem != null && !this.equals(existingMuseumManagementSystem);
    if (isNewMuseumManagementSystem)
    {
      aEmployee.setMuseumManagementSystem(this);
    }
    else
    {
      employees.add(aEmployee);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeEmployee(Employee aEmployee)
  {
    boolean wasRemoved = false;
    //Unable to remove aEmployee, as it must always have a museumManagementSystem
    if (!this.equals(aEmployee.getMuseumManagementSystem()))
    {
      employees.remove(aEmployee);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addEmployeeAt(Employee aEmployee, int index)
  {  
    boolean wasAdded = false;
    if(addEmployee(aEmployee))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployees()) { index = numberOfEmployees() - 1; }
      employees.remove(aEmployee);
      employees.add(index, aEmployee);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEmployeeAt(Employee aEmployee, int index)
  {
    boolean wasAdded = false;
    if(employees.contains(aEmployee))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfEmployees()) { index = numberOfEmployees() - 1; }
      employees.remove(aEmployee);
      employees.add(index, aEmployee);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addEmployeeAt(aEmployee, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfClients()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  // public Client addClient(String aUsername, String aName, String aPassword, int aCurrentLoanNumber)
  // {
  //   return new Client(aUsername, aName, aPassword, aCurrentLoanNumber, this);
  // }

  public boolean addClient(Client aClient)
  {
    boolean wasAdded = false;
    if (clients.contains(aClient)) { return false; }
    MuseumManagementSystem existingMuseumManagementSystem = aClient.getMuseumManagementSystem();
    boolean isNewMuseumManagementSystem = existingMuseumManagementSystem != null && !this.equals(existingMuseumManagementSystem);
    if (isNewMuseumManagementSystem)
    {
      aClient.setMuseumManagementSystem(this);
    }
    else
    {
      clients.add(aClient);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeClient(Client aClient)
  {
    boolean wasRemoved = false;
    //Unable to remove aClient, as it must always have a museumManagementSystem
    if (!this.equals(aClient.getMuseumManagementSystem()))
    {
      clients.remove(aClient);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addClientAt(Client aClient, int index)
  {  
    boolean wasAdded = false;
    if(addClient(aClient))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfClients()) { index = numberOfClients() - 1; }
      clients.remove(aClient);
      clients.add(index, aClient);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveClientAt(Client aClient, int index)
  {
    boolean wasAdded = false;
    if(clients.contains(aClient))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfClients()) { index = numberOfClients() - 1; }
      clients.remove(aClient);
      clients.add(index, aClient);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addClientAt(aClient, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfShifts()
  {
    return 0;
  }
  // /* Code from template association_AddManyToOne */
  // public Shift addShift(int aShiftId, Time aStartTime, Time aEndTime, SpecificWeekDay aDayOfTheWeek, Employee aEmployee)
  // {
  //   return new Shift(aShiftId, aStartTime, aEndTime, aDayOfTheWeek, this, aEmployee);
  // }

  public boolean addShift(Shift aShift)
  {
    boolean wasAdded = false;
    if (shifts.contains(aShift)) { return false; }
    MuseumManagementSystem existingMuseumManagementSystem = aShift.getMuseumManagementSystem();
    boolean isNewMuseumManagementSystem = existingMuseumManagementSystem != null && !this.equals(existingMuseumManagementSystem);
    if (isNewMuseumManagementSystem)
    {
      aShift.setMuseumManagementSystem(this);
    }
    else
    {
      shifts.add(aShift);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeShift(Shift aShift)
  {
    boolean wasRemoved = false;
    //Unable to remove aShift, as it must always have a museumManagementSystem
    if (!this.equals(aShift.getMuseumManagementSystem()))
    {
      shifts.remove(aShift);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addShiftAt(Shift aShift, int index)
  {  
    boolean wasAdded = false;
    if(addShift(aShift))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShifts()) { index = numberOfShifts() - 1; }
      shifts.remove(aShift);
      shifts.add(index, aShift);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveShiftAt(Shift aShift, int index)
  {
    boolean wasAdded = false;
    if(shifts.contains(aShift))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfShifts()) { index = numberOfShifts() - 1; }
      shifts.remove(aShift);
      shifts.add(index, aShift);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addShiftAt(aShift, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLoanRequests()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  // public LoanRequest addLoanRequest(int aRequestId, Client aClient, Artifact aArtifact, int aLoanDuration, double aFee, LoanRequest.LoanStatus aStatus)
  // {
  //   return new LoanRequest(aRequestId, aClient, aArtifact, aLoanDuration, aFee, aStatus, this);
  // }

  public boolean addLoanRequest(LoanRequest aLoanRequest)
  {
    boolean wasAdded = false;
    if (loanRequests.contains(aLoanRequest)) { return false; }
    MuseumManagementSystem existingMuseumManagementSystem = aLoanRequest.getMuseumManagementSystem();
    boolean isNewMuseumManagementSystem = existingMuseumManagementSystem != null && !this.equals(existingMuseumManagementSystem);
    if (isNewMuseumManagementSystem)
    {
      aLoanRequest.setMuseumManagementSystem(this);
    }
    else
    {
      loanRequests.add(aLoanRequest);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLoanRequest(LoanRequest aLoanRequest)
  {
    boolean wasRemoved = false;
    //Unable to remove aLoanRequest, as it must always have a museumManagementSystem
    if (!this.equals(aLoanRequest.getMuseumManagementSystem()))
    {
      loanRequests.remove(aLoanRequest);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLoanRequestAt(LoanRequest aLoanRequest, int index)
  {  
    boolean wasAdded = false;
    if(addLoanRequest(aLoanRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLoanRequests()) { index = numberOfLoanRequests() - 1; }
      loanRequests.remove(aLoanRequest);
      loanRequests.add(index, aLoanRequest);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLoanRequestAt(LoanRequest aLoanRequest, int index)
  {
    boolean wasAdded = false;
    if(loanRequests.contains(aLoanRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLoanRequests()) { index = numberOfLoanRequests() - 1; }
      loanRequests.remove(aLoanRequest);
      loanRequests.add(index, aLoanRequest);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLoanRequestAt(aLoanRequest, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfDonationRequests()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  // public DonationRequest addDonationRequest(int aRequestId, Client aClient, Artifact aArtifact, DonationRequest.DonationStatus aStatus)
  // {
  //   return new DonationRequest(aRequestId, aClient, aArtifact, aStatus, this);
  // }

  public boolean addDonationRequest(DonationRequest aDonationRequest)
  {
    boolean wasAdded = false;
    if (donationRequests.contains(aDonationRequest)) { return false; }
    MuseumManagementSystem existingMuseumManagementSystem = aDonationRequest.getMuseumManagementSystem();
    boolean isNewMuseumManagementSystem = existingMuseumManagementSystem != null && !this.equals(existingMuseumManagementSystem);
    if (isNewMuseumManagementSystem)
    {
      aDonationRequest.setMuseumManagementSystem(this);
    }
    else
    {
      donationRequests.add(aDonationRequest);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeDonationRequest(DonationRequest aDonationRequest)
  {
    boolean wasRemoved = false;
    //Unable to remove aDonationRequest, as it must always have a museumManagementSystem
    if (!this.equals(aDonationRequest.getMuseumManagementSystem()))
    {
      donationRequests.remove(aDonationRequest);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addDonationRequestAt(DonationRequest aDonationRequest, int index)
  {  
    boolean wasAdded = false;
    if(addDonationRequest(aDonationRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDonationRequests()) { index = numberOfDonationRequests() - 1; }
      donationRequests.remove(aDonationRequest);
      donationRequests.add(index, aDonationRequest);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveDonationRequestAt(DonationRequest aDonationRequest, int index)
  {
    boolean wasAdded = false;
    if(donationRequests.contains(aDonationRequest))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfDonationRequests()) { index = numberOfDonationRequests() - 1; }
      donationRequests.remove(aDonationRequest);
      donationRequests.add(index, aDonationRequest);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addDonationRequestAt(aDonationRequest, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfArtifacts()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  // public Artifact addArtifact(int aArtifactId, String aName, String aImage, String aDescription, Artifact.LoanStatus aLoanStatus, boolean aIsDamaged, double aLoanFee, double aWorth, Room aRoomLocation)
  // {
  //   return new Artifact(aArtifactId, aName, aImage, aDescription, aLoanStatus, aIsDamaged, aLoanFee, aWorth, aRoomLocation, this);
  // }

  public boolean addArtifact(Artifact aArtifact)
  {
    boolean wasAdded = false;
    if (artifacts.contains(aArtifact)) { return false; }
    MuseumManagementSystem existingMuseumManagementSystem = aArtifact.getMuseumManagementSystem();
    boolean isNewMuseumManagementSystem = existingMuseumManagementSystem != null && !this.equals(existingMuseumManagementSystem);
    if (isNewMuseumManagementSystem)
    {
      aArtifact.setMuseumManagementSystem(this);
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
    //Unable to remove aArtifact, as it must always have a museumManagementSystem
    if (!this.equals(aArtifact.getMuseumManagementSystem()))
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRooms()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  /*public Room addRoom(String aName, Room.RoomType aType, int aRoomId)
  {
    return new Room(aName, aType, aRoomId, this);
  }*/

  public boolean addRoom(Room aRoom)
  {
    boolean wasAdded = false;
    if (rooms.contains(aRoom)) { return false; }
    MuseumManagementSystem existingMuseumManagementSystem = aRoom.getMuseumManagementSystem();
    boolean isNewMuseumManagementSystem = existingMuseumManagementSystem != null && !this.equals(existingMuseumManagementSystem);
    if (isNewMuseumManagementSystem)
    {
      aRoom.setMuseumManagementSystem(this);
    }
    else
    {
      rooms.add(aRoom);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRoom(Room aRoom)
  {
    boolean wasRemoved = false;
    //Unable to remove aRoom, as it must always have a museumManagementSystem
    if (!this.equals(aRoom.getMuseumManagementSystem()))
    {
      rooms.remove(aRoom);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRoomAt(Room aRoom, int index)
  {  
    boolean wasAdded = false;
    if(addRoom(aRoom))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRooms()) { index = numberOfRooms() - 1; }
      rooms.remove(aRoom);
      rooms.add(index, aRoom);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRoomAt(Room aRoom, int index)
  {
    boolean wasAdded = false;
    if(rooms.contains(aRoom))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRooms()) { index = numberOfRooms() - 1; }
      rooms.remove(aRoom);
      rooms.add(index, aRoom);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRoomAt(aRoom, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTickets()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  // public Ticket addTicket(int aTicketId, double aFee, boolean aIsActive, Client aClient)
  // {
  //   return new Ticket(aTicketId, aFee, aIsActive, this, aClient);
  // }

  public boolean addTicket(Ticket aTicket)
  {
    boolean wasAdded = false;
    if (tickets.contains(aTicket)) { return false; }
    MuseumManagementSystem existingMuseumManagementSystem = aTicket.getMuseumManagementSystem();
    boolean isNewMuseumManagementSystem = existingMuseumManagementSystem != null && !this.equals(existingMuseumManagementSystem);
    if (isNewMuseumManagementSystem)
    {
      aTicket.setMuseumManagementSystem(this);
    }
    else
    {
      tickets.add(aTicket);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTicket(Ticket aTicket)
  {
    boolean wasRemoved = false;
    //Unable to remove aTicket, as it must always have a museumManagementSystem
    if (!this.equals(aTicket.getMuseumManagementSystem()))
    {
      tickets.remove(aTicket);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTicketAt(Ticket aTicket, int index)
  {  
    boolean wasAdded = false;
    if(addTicket(aTicket))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTickets()) { index = numberOfTickets() - 1; }
      tickets.remove(aTicket);
      tickets.add(index, aTicket);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTicketAt(Ticket aTicket, int index)
  {
    boolean wasAdded = false;
    if(tickets.contains(aTicket))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTickets()) { index = numberOfTickets() - 1; }
      tickets.remove(aTicket);
      tickets.add(index, aTicket);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTicketAt(aTicket, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWeekDays()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfWeekDays()
  {
    return 7;
  }
  // /* Code from template association_AddOptionalNToOne */
  // public SpecificWeekDay addWeekDay(boolean aIsClosed, SpecificWeekDay.DayType aDayType)
  // {
  //   if (numberOfWeekDays() >= maximumNumberOfWeekDays())
  //   {
  //     return null;
  //   }
  //   else
  //   {
  //     return new SpecificWeekDay(aIsClosed, aDayType, this);
  //   }
  // }

  public boolean addWeekDay(SpecificWeekDay aWeekDay)
  {
    boolean wasAdded = false;
    if (weekDays.contains(aWeekDay)) { return false; }
    if (numberOfWeekDays() >= maximumNumberOfWeekDays())
    {
      return wasAdded;
    }

    MuseumManagementSystem existingMuseumManagementSystem = aWeekDay.getMuseumManagementSystem();
    boolean isNewMuseumManagementSystem = existingMuseumManagementSystem != null && !this.equals(existingMuseumManagementSystem);
    if (isNewMuseumManagementSystem)
    {
      aWeekDay.setMuseumManagementSystem(this);
    }
    else
    {
      weekDays.add(aWeekDay);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWeekDay(SpecificWeekDay aWeekDay)
  {
    boolean wasRemoved = false;
    //Unable to remove aWeekDay, as it must always have a museumManagementSystem
    if (!this.equals(aWeekDay.getMuseumManagementSystem()))
    {
      weekDays.remove(aWeekDay);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWeekDayAt(SpecificWeekDay aWeekDay, int index)
  {  
    boolean wasAdded = false;
    if(addWeekDay(aWeekDay))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWeekDays()) { index = numberOfWeekDays() - 1; }
      weekDays.remove(aWeekDay);
      weekDays.add(index, aWeekDay);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWeekDayAt(SpecificWeekDay aWeekDay, int index)
  {
    boolean wasAdded = false;
    if(weekDays.contains(aWeekDay))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWeekDays()) { index = numberOfWeekDays() - 1; }
      weekDays.remove(aWeekDay);
      weekDays.add(index, aWeekDay);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWeekDayAt(aWeekDay, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    museummanagementsystemsBySystemId.remove(getSystemId());
    Manager existingManager = manager;
    manager = null;
    if (existingManager != null)
    {
      existingManager.delete();
    }
    while (employees.size() > 0)
    {
      Employee aEmployee = employees.get(employees.size() - 1);
      aEmployee.delete();
      employees.remove(aEmployee);
    }
    
    while (clients.size() > 0)
    {
      Client aClient = clients.get(clients.size() - 1);
      aClient.delete();
      clients.remove(aClient);
    }
    
    while (shifts.size() > 0)
    {
      Shift aShift = shifts.get(shifts.size() - 1);
      aShift.delete();
      shifts.remove(aShift);
    }
    
    while (loanRequests.size() > 0)
    {
      LoanRequest aLoanRequest = loanRequests.get(loanRequests.size() - 1);
      aLoanRequest.delete();
      loanRequests.remove(aLoanRequest);
    }
    
    while (donationRequests.size() > 0)
    {
      DonationRequest aDonationRequest = donationRequests.get(donationRequests.size() - 1);
      aDonationRequest.delete();
      donationRequests.remove(aDonationRequest);
    }
    
    while (artifacts.size() > 0)
    {
      Artifact aArtifact = artifacts.get(artifacts.size() - 1);
      aArtifact.delete();
      artifacts.remove(aArtifact);
    }
    
    while (rooms.size() > 0)
    {
      Room aRoom = rooms.get(rooms.size() - 1);
      aRoom.delete();
      rooms.remove(aRoom);
    }
    
    while (tickets.size() > 0)
    {
      Ticket aTicket = tickets.get(tickets.size() - 1);
      aTicket.delete();
      tickets.remove(aTicket);
    }
    
    while (weekDays.size() > 0)
    {
      SpecificWeekDay aWeekDay = weekDays.get(weekDays.size() - 1);
      aWeekDay.delete();
      weekDays.remove(aWeekDay);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "systemId" + ":" + getSystemId()+ "," +
            "name" + ":" + getName()+ "," +
            "maxLoanNumber" + ":" + getMaxLoanNumber()+ "," +
            "ticketFee" + ":" + getTicketFee()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "openTime" + "=" + (getOpenTime() != null ? !getOpenTime().equals(this)  ? getOpenTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "closeTime" + "=" + (getCloseTime() != null ? !getCloseTime().equals(this)  ? getCloseTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "manager = "+(getManager()!=null?Integer.toHexString(System.identityHashCode(getManager())):"null");
  }
}