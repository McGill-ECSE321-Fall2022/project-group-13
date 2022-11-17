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
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.lenient;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestClientAccountService {
    @Mock
    private ClientRepository clientDao;

    @Mock
    private MuseumManagementSystemRepository mmsDao;

    @InjectMocks
    private ClientAccountService service;

    private static final String CLIENT_USERNAME = "ClientUsername";
    private static final String CLIENT_NAME = "ClientName";
    private static final String CLIENT_PASSWORD = "ClientPassword"; 
    private static final int CLIENT_LOAN_NUMBER = 0; 

    private static final String CLIENT_USERNAME_A = "ClientUsername_A";
    private static final String CLIENT_NAME_A = "ClientName_A";
    private static final String CLIENT_PASSWORD_A = "ClientPassword_A"; 

    private static final String CLIENT_USERNAME_B = "ClientUsername_B";
    private static final String CLIENT_NAME_B = "ClientName_B";
    private static final String CLIENT_PASSWORD_B = "ClientPassword_B";

    private static final String CLIENT_USERNAME_C = "ClientUsername_C";
    private static final String CLIENT_NAME_C = "ClientName_C";
    private static final String CLIENT_PASSWORD_C = "ClientPassword_C"; 

    private static final String CLIENT_USERNAME_D = "ClientUsername_D";
    private static final String CLIENT_NAME_D = "ClientName_D";
    private static final String CLIENT_PASSWORD_D = "ClientPassword_D"; 

    private static final String EXISTING_CLIENT_USERNAME = "ExistingClientUsername";

    private static final String FAKE_CLIENT_USERNAME = "FakeClientUsername";
    
    private static final int MMS_ID = 8;
    private static final String MMS_NAME = "Louvre";
    private static final Time OPEN_TIME = Time.valueOf("9:00:00");
    private static final Time CLOSE_TIME = Time.valueOf("17:00:00");
    private static final int MAX_LOAN_NUMBER = 5;
    private static final double TICKET_FEE = 10.0;

 
    @BeforeEach
    public void setMockOutput() {

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
                return client(CLIENT_USERNAME,CLIENT_NAME,CLIENT_PASSWORD,CLIENT_LOAN_NUMBER,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            }
            else if (invocation.getArgument(0).equals(CLIENT_USERNAME_B)){
                return client(CLIENT_USERNAME_B,CLIENT_NAME_B,CLIENT_PASSWORD_B,CLIENT_LOAN_NUMBER,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            }
            else if (invocation.getArgument(0).equals(CLIENT_USERNAME_C)){
                return client(CLIENT_USERNAME_C,CLIENT_NAME_C,CLIENT_PASSWORD_C,CLIENT_LOAN_NUMBER,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            }
            else if (invocation.getArgument(0).equals(CLIENT_USERNAME_D)){
                return client(CLIENT_USERNAME_D,CLIENT_NAME_D,CLIENT_PASSWORD_D,CLIENT_LOAN_NUMBER,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            }
            else if (invocation.getArgument(0).equals(FAKE_CLIENT_USERNAME)){
                return null; 
            }
            else {
                return null;
            }
    });

        lenient().when(clientDao.existsById(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(EXISTING_CLIENT_USERNAME)) {
                return true;
            }
            if(invocation.getArgument(0).equals(CLIENT_USERNAME_C)) {
                return true;
            }
            else {
                return false;
            }
    });

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };

        lenient().when(mmsDao.save(any(MuseumManagementSystem.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(clientDao.save(any(Client.class))).thenAnswer(returnParameterAsAnswer);
    }

    //-----------------------------------------------Testing createClient()--------------------------------------------------

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
    public void testCreateClientUsernameWithWhitespaces(){
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
    public void testCreateClientNameOnlyWhitespaces(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(CLIENT_USERNAME, "   ", CLIENT_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid name", error);
    }

    @Test
    public void testCreateClientPasswordWithWhitespaces(){
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

    //-----------------------------------------------Testing getClient()--------------------------------------------------

    // One of the problematic tests, when not commented out, the createClient fails. 

    @Test
    public void testGetClient(){
        Client a = null; 

        try{
            a = service.getClient(CLIENT_USERNAME_B);
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(CLIENT_USERNAME_B, a.getUsername());
        assertEquals(CLIENT_NAME_B, a.getName());
        assertEquals(CLIENT_PASSWORD_B, a.getPassword());
    }

    @Test
    public void testGetClientEmptyUsername(){
        Client a = null; 

        String error = "";

        try{
            a = service.getClient("");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This username is invalid", error);
    }

    @Test
    public void testGetClientNullUsername(){
        Client a = null; 

        String error = "";

        try{
            a = service.getClient(null);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This username is invalid", error);
    }

    @Test
    public void testGetClientUsernameWithWhitespaces(){
        Client a = null; 

        String error = "";

        try{
            a = service.getClient("   ");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This username is invalid", error);
    }

    @Test
    public void testGetClientNotFound(){
        Client a = null; 

        String error = "";

        try{
            a = service.getClient(FAKE_CLIENT_USERNAME);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This client account does not exist", error);
    }

    //-----------------------------------------------Testing getAllClients()--------------------------------------------------

    // One of the problematic tests, when not commented out, the createClient fails. 

    @Test
    public void testGetAllClients(){

        Client client_1 = service.createClient(CLIENT_USERNAME_A, CLIENT_NAME_A, CLIENT_PASSWORD_A, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));

        lenient().when(clientDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {

            List<Client> clients = new ArrayList<Client>();
            clients.add(client_1);
            return clients;

        });

        List<Client> allClients = null; 

        try{
            allClients = service.getAllClients();
        }catch (MuseumManagementSystemException e){
            fail();
        }

        assertNotNull(allClients);
        assertEquals(1, allClients.size());
        assertEquals(client_1, allClients.get(0));
    }

    //-----------------------------------------------Testing deleteClient()--------------------------------------------------


    // // IDK how to test the succesful case?? Sam has verify 

    // public void testDeleteClient(){

    //     try{
    //         service.deleteClient(EXISTING_CLIENT_USERNAME);
    //     }catch (MuseumManagementSystemException e){
    //         fail();
    //     }
    // }

    //-----------------------------------------------Testing signInClientAccount()--------------------------------------------------

    @Test
    public void testSignInClientAccount(){
        Client a = null; 

        try{
            a = service.signInClientAccount(CLIENT_USERNAME_C, CLIENT_PASSWORD_C);
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(CLIENT_USERNAME_C, a.getUsername());
        assertEquals(CLIENT_NAME_C, a.getName());
        assertEquals(CLIENT_PASSWORD_C, a.getPassword());

    } 

    @Test
    public void testSignInClientAccountEmptyPassword(){
        
        Client a = null; 
        String error = ""; 

        try{
            a = service.signInClientAccount(CLIENT_USERNAME_C, "");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    }


    @Test
    public void testSignInClientAccountNullPassword(){
        
        Client a = null; 
        String error = ""; 

        try{
            a = service.signInClientAccount(CLIENT_USERNAME_C, null);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    }

    @Test
    public void testSignInClientAccountIncorrectPassword(){
        
        Client a = null; 
        String error = ""; 

        try{
            a = service.signInClientAccount(CLIENT_USERNAME_C, CLIENT_PASSWORD_B);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is incorrect", error); 
    }

    @Test
    public void testSignInClientAccountPasswordWithWhitespaces(){
        
        Client a = null; 
        String error = ""; 

        try{
            a = service.signInClientAccount(CLIENT_USERNAME_C, "   ");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is incorrect", error); 
    }

    //-----------------------------------------------Testing editClientAccount()--------------------------------------------------

    @Test
    public void testEditClientAccount(){
        Client a = null; 

        try{
            a = service.editClientAccount(CLIENT_USERNAME_D, CLIENT_NAME_A, CLIENT_PASSWORD_A);
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(CLIENT_NAME_A, a.getName());
        assertEquals(CLIENT_PASSWORD_A, a.getPassword());
        assertNotEquals(CLIENT_NAME_D, a.getName());
        assertNotEquals(CLIENT_PASSWORD_D, a.getName());
    }

    @Test
    public void testEditClientAccountEmptyName(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.editClientAccount(CLIENT_USERNAME_D, "", CLIENT_PASSWORD_A);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This name is invalid", error); 
    
    }

    @Test
    public void testEditClientAccountNullName(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.editClientAccount(CLIENT_USERNAME_D, null, CLIENT_PASSWORD_A);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This name is invalid", error); 
    }

    @Test
    public void testEditClientAccountPasswordContainsWhitespaces(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.editClientAccount(CLIENT_USERNAME_D, CLIENT_NAME_A, "   ");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    }

    @Test
    public void testEditClientAccountPasswordLessThan8(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.editClientAccount(CLIENT_USERNAME_D, CLIENT_NAME_A, "123");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    }

    @Test
    public void testEditClientAccountPasswordGreaterThan30(){
        Client a = null; 

        String error = ""; 

        try{
            a = service.editClientAccount(CLIENT_USERNAME_D, CLIENT_NAME_A, "abcdefghijklmnopqrstuvwxyz12345678");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    }
    
    /**
     * Private Constructor of a client 
     * @param username
     * @param name
     * @param password
     * @param loanNumber
     * @param mms
     * @return
     */
    private Client client(String username, String name, String password, int loanNumber, MuseumManagementSystem mms){
        Client client = new Client(); 
        client.setUsername(username); 
        client.setName(name); 
        client.setPassword(password); 
        client.setCurrentLoanNumber(loanNumber); 
        client.setMuseumManagementSystem(mms);
        return client;
    }

}   


