package ca.mcgill.ecse321.MMSBackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.EmployeeRepository;
import ca.mcgill.ecse321.MMSBackend.dao.ManagerRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.Manager;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.lenient;

import java.sql.Time;

@ExtendWith(MockitoExtension.class)
public class TestAccountManagementService {
    @Mock
    private ClientRepository clientDao;

    @Mock
    private EmployeeRepository employeeDao; 

    @Mock 
    private ManagerRepository managerDao; 

    @Mock
    private MuseumManagementSystemRepository mmsDao;

    @InjectMocks
    private AccountManagementService service;

    private static final String CLIENT_USERNAME = "ClientUsername";
    private static final String CLIENT_NAME = "ClientName";
    private static final String CLIENT_PASSWORD = "ClientPassword"; 
    private static final int CLIENT_LOAN_NUMBER = 0; 

    private static final String EXISTING_CLIENT_USERNAME = "ExistingClientUsername";
    private static final String EXISTING_CLIENT_NAME = "ExistingClientName";
    private static final String EXISTING_CLIENT_PASSWORD = "ExistingClientPassword"; 

    private static final String EMPLOYEE_USERNAME = "EmployeeUsername";
    private static final String EMPLOYEE_NAME = "EmployeeName";
    private static final String EMPLOYEE_PASSWORD = "EmployeePassword"; 

    private static final String MANAGER_USERNAME = "ManagerUsername";
    private static final String MANAGER_NAME = "ManagerName";
    private static final String MANAGER_PASSWORD = "ManagerPassword"; 

    private static final int MMS_ID = 8;
    private static final String MMS_NAME = "Louvre";
    private static final Time OPEN_TIME = Time.valueOf("9:00:00");
    private static final Time CLOSE_TIME = Time.valueOf("17:00:00");
    private static final int MAX_LOAN_NUMBER = 5;
    private static final double TICKET_FEE = 10.0;

    
    @BeforeEach
    public void setMockOutput() {

        // Creating previously existing clients (not working) 
        Client ExistingClient = new Client(); 
        ExistingClient.setUsername(EXISTING_CLIENT_USERNAME);
        ExistingClient.setName(EXISTING_CLIENT_NAME); 
        ExistingClient.setPassword(EXISTING_CLIENT_PASSWORD);
        ExistingClient.setCurrentLoanNumber(CLIENT_LOAN_NUMBER); 
        ExistingClient.setMuseumManagementSystem(mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        clientDao.save(ExistingClient); 

        lenient().when(mmsDao.findMuseumManagementSystemBySystemId(anyInt()))
        .thenAnswer((InvocationOnMock invocation) -> {
        if (invocation.getArgument(0).equals(MMS_ID)) {
            MuseumManagementSystem mms = new MuseumManagementSystem();
            mms.setSystemId(MMS_ID);
            mms.setName(MMS_NAME);
            mms.setOpenTime(OPEN_TIME);
            mms.setCloseTime(CLOSE_TIME);
            mms.setMaxLoanNumber(MAX_LOAN_NUMBER);
            mms.setTicketFee(TICKET_FEE);
            return mms;
        } else {
            return null;
        }
    });

    lenient().when(clientDao.findClientByUsername(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
        if(invocation.getArgument(0).equals(CLIENT_USERNAME)) {
            Client client = new Client(); 
            client.setUsername(CLIENT_USERNAME); 
            client.setName(CLIENT_NAME); 
            client.setPassword(CLIENT_PASSWORD); 
            client.setCurrentLoanNumber(CLIENT_LOAN_NUMBER); 
            client.setMuseumManagementSystem(mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            return client;
        } else {
            return null;
        }
    });

    lenient().when(employeeDao.findEmployeeByUsername(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
        if(invocation.getArgument(0).equals(EMPLOYEE_USERNAME)) {
            Employee employee = new Employee(); 
            employee.setUsername(EMPLOYEE_USERNAME); 
            employee.setName(EMPLOYEE_NAME); 
            employee.setPassword(EMPLOYEE_PASSWORD); 
            employee.setMuseumManagementSystem(mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            return employee;
        } else {
            return null;
        }
    }); 

    lenient().when(managerDao.findManagerByUsername(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
        if(invocation.getArgument(0).equals(MANAGER_USERNAME)) {
            Manager manager = new Manager(); 
            manager.setUsername(MANAGER_USERNAME); 
            manager.setName(MANAGER_NAME); 
            manager.setPassword(MANAGER_PASSWORD); 
            manager.setMuseumManagementSystem(mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            return manager;
        } else {
            return null;
        }
    }); 

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };

        lenient().when(mmsDao.save(any(MuseumManagementSystem.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(clientDao.save(any(Client.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(employeeDao.save(any(Employee.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(managerDao.save(any(Manager.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateClient(){
        assertEquals(0, service.getAllClients().size());

        Client a = null; 

        try{
            a = service.createClient(CLIENT_USERNAME, CLIENT_NAME, CLIENT_PASSWORD, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(CLIENT_USERNAME, a.getUsername());
        assertEquals(CLIENT_NAME, a.getName());
        assertEquals(CLIENT_PASSWORD, a.getPassword());
    }

    @Test
    public void testCreateClientMMSNull(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(CLIENT_USERNAME, CLIENT_NAME, CLIENT_PASSWORD, null);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateClientUsernameNull(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(null, CLIENT_NAME, CLIENT_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateClientNameNull(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(CLIENT_USERNAME, null, CLIENT_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }
    
    @Test
    public void testCreateClientPaswordNull(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(CLIENT_USERNAME, CLIENT_NAME, null,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateClientUsernameEmpty(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient("", CLIENT_NAME, CLIENT_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateClientNameEmpty(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(CLIENT_USERNAME, "", CLIENT_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateClientPasswordEmpty(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(CLIENT_USERNAME, CLIENT_NAME, "",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateClientUsernameWithSpaces(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient("   ", CLIENT_NAME, CLIENT_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("The username cannot have spaces", error);
    }

    @Test
    public void testCreateClientNameOnlySpaces(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient("  ", CLIENT_NAME, CLIENT_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("The username cannot have spaces", error);
    }

    @Test
    public void testCreateClientPasswordWithSpaces(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(CLIENT_USERNAME, CLIENT_NAME, "  ",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid password", error);
    }

    @Test
    public void testCreateClientPasswordLessThan8(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(CLIENT_USERNAME, CLIENT_NAME, "123",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid password", error);
    }

    @Test
    public void testCreateClientPasswordMoreThan30(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(CLIENT_USERNAME, CLIENT_NAME, "abcdefghijklmnopqrstuvwxyz12345678",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid password", error);
    }


    // Im not sure how to add an "existing account"
    @Test
    public void testCreateClientWithUsedUsername(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(EXISTING_CLIENT_USERNAME, CLIENT_NAME, CLIENT_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This username is already taken", error);
    }

}
