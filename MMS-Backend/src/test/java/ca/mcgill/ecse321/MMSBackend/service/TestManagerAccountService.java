package ca.mcgill.ecse321.MMSBackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.MMSBackend.dao.ManagerRepository;
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Manager;
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
public class TestManagerAccountService {
    @Mock
    private ManagerRepository managerDao; 

    @Mock
    private MuseumManagementSystemRepository mmsDao; 

    @InjectMocks
    private ManagerAccountService service; 

    private static final String MANAGER_USERNAME = "ManagerUsername";
    private static final String MANAGER_USERNAME_A = "ManagerUsername_A";
    private static final String MANAGER_USERNAME_B = "ManagerUsername_B";
    private static final String MANAGER_USERNAME_C = "ManagerUsername_C";
    private static final String MANAGER_USERNAME_D = "ManagerUsername_D";
    private static final String MANAGER_USERNAME_E = "ManagerUsername_E";
    private static final String EXISTING_MANAGER_USERNAME = "ExistingManagerUsername";
    private static final String FAKE_MANAGER_USERNAME = "FakeManagerUsername";

    private static final String MANAGER_NAME = "ManagerRName";
    private static final String MANAGER_NEW_NAME = "NewManagerName";
    private static final String MANAGER_NEW_PASSWORD = "NewManagerPasword"; 
    private static final String MANAGER_PASSWORD = "ManagerPassword"; 
    private static final String MANAGER_INCORRECT_PASSWORD = "IncorrectManagerPassword"; 

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

    lenient().when(managerDao.findManagerByUsername(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
        if(invocation.getArgument(0).equals(MANAGER_USERNAME_B)) {
            return manager(MANAGER_USERNAME_B,MANAGER_NAME,MANAGER_PASSWORD,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }
        else if (invocation.getArgument(0).equals(MANAGER_USERNAME_C)){
            return manager(MANAGER_USERNAME_C,MANAGER_NAME,MANAGER_PASSWORD,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }
        else if (invocation.getArgument(0).equals(MANAGER_USERNAME_D)){
            return manager(MANAGER_USERNAME_D,MANAGER_NAME,MANAGER_PASSWORD,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }
        else if (invocation.getArgument(0).equals(MANAGER_USERNAME_E)){
            return manager(MANAGER_USERNAME_E,MANAGER_NAME,MANAGER_PASSWORD,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }
        else if (invocation.getArgument(0).equals(FAKE_MANAGER_USERNAME)){
            return null; 
        }
        else {
            return null;
        }
    });

    lenient().when(managerDao.existsById(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
        if(invocation.getArgument(0).equals(EXISTING_MANAGER_USERNAME)) {
            return true;
        }
        if(invocation.getArgument(0).equals(MANAGER_USERNAME_C)) {
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
    lenient().when(managerDao.save(any(Manager.class))).thenAnswer(returnParameterAsAnswer);
    }

    //-----------------------------------------------Testing createManager()--------------------------------------------------


    @Test
    public void testCreateManager(){
        Manager a = null; 

        try{
            a = service.createManager(MANAGER_USERNAME, MANAGER_NAME, MANAGER_PASSWORD, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(MANAGER_USERNAME, a.getUsername());
        assertEquals(MANAGER_NAME, a.getName());
        assertEquals(MANAGER_PASSWORD, a.getPassword());
    }

    @Test
    public void testCreateManagerMMSNull(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(MANAGER_USERNAME, MANAGER_NAME,MANAGER_PASSWORD, null);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateManagerUsernameNull(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(null, MANAGER_NAME, MANAGER_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateManagerNameNull(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(MANAGER_USERNAME, null, MANAGER_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateManagerPaswordNull(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(MANAGER_USERNAME, MANAGER_NAME, null,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateManagerUsernameEmpty(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager("", MANAGER_NAME, MANAGER_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateManagerNameEmpty(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(MANAGER_USERNAME, "", MANAGER_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateManagerPasswordEmpty(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(MANAGER_USERNAME, MANAGER_NAME, "",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateManagerUsernameWithWhitespaces(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager("  ", MANAGER_NAME, MANAGER_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("The username cannot have spaces", error);
    }

    @Test
    public void testCreateManagerNameOnlyWhitespaces(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(MANAGER_USERNAME, "  ", MANAGER_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid name", error);
    }


    @Test
    public void testCreateManagerPasswordWithWhitespaces(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(MANAGER_USERNAME, MANAGER_NAME, "  ",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid password", error);
    }

    @Test
    public void testCreateManagerPasswordLessThan8(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(MANAGER_USERNAME, MANAGER_NAME, "123",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid password", error);
    }

    @Test
    public void testCreateManagerPasswordMoreThan30(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(MANAGER_USERNAME, MANAGER_NAME, "abcdefghijklmnopqrstuvwxyz12345678",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid password", error);
    }

    @Test
    public void testCreateManagerAlreadyExists(){

        Manager manager_1 = manager(EXISTING_MANAGER_USERNAME, MANAGER_NAME, MANAGER_PASSWORD, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));

        lenient().when(managerDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Manager> managers = new ArrayList<Manager>();
            managers.add(manager_1);
            return managers;

        });

        Manager a = null; 

        String error = ""; 

        try{
            a = service.createManager(MANAGER_USERNAME, MANAGER_NAME, MANAGER_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("A manager already exists", error);
    }

    //-----------------------------------------------Testing getManager()--------------------------------------------------


    @Test
    public void testGetManager(){
        Manager manager_1 = manager(MANAGER_USERNAME_C, MANAGER_NAME, MANAGER_PASSWORD, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));

        lenient().when(managerDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Manager> managers = new ArrayList<Manager>();
            managers.add(manager_1);
            return managers;

        });

        Manager a = null; 

        try{
            a = service.getManager();
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(MANAGER_USERNAME_C, a.getUsername());
        assertEquals(MANAGER_NAME, a.getName());
        assertEquals(MANAGER_PASSWORD, a.getPassword());

    }

    @Test
    public void testGetManagerReturnsNull(){
        Manager manager_1 = null; 

        lenient().when(managerDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Manager> managers = new ArrayList<Manager>();
            managers.add(manager_1);
            return managers;

        });

        Manager a = null; 

        try{
            a = service.getManager();
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNull(a);
    }
    
    //-----------------------------------------------Testing signInManagerAccount()--------------------------------------------------

    @Test
    public void testSignInManagerAccount(){

        Manager manager_1 = manager(MANAGER_USERNAME_D, MANAGER_NAME, MANAGER_PASSWORD, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));

        lenient().when(managerDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Manager> managers = new ArrayList<Manager>();
            managers.add(manager_1);
            return managers;

        });


        Manager a = null; 

        try{
            a = service.signInManagerAccount(MANAGER_USERNAME_D, MANAGER_PASSWORD);
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(MANAGER_USERNAME_D, a.getUsername());
        assertEquals(MANAGER_NAME, a.getName());
        assertEquals(MANAGER_PASSWORD, a.getPassword());

    } 

    @Test
    public void testSignInManagerAccountEmptyPassword(){
        
        Manager a = null; 
        String error = ""; 

        try{
            a = service.signInManagerAccount(MANAGER_USERNAME_D, "");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    }

    @Test
    public void testSignInManagerAccountNullPassword(){
        
        Manager a = null; 
        String error = ""; 

        try{
            a = service.signInManagerAccount(MANAGER_USERNAME_D, null);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    }

    @Test
    public void testSignInManagerAccountDoesntExist(){
        Manager manager_1 = null; 

        lenient().when(managerDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Manager> managers = new ArrayList<Manager>();
            managers.add(manager_1);
            return managers;

        });

        Manager a = null; 
        String error = ""; 

        try{
            a = service.signInManagerAccount(MANAGER_USERNAME_E, MANAGER_PASSWORD);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("A manager account doesn't exist", error); 

    }

    // This can cause the failure of testSignInManager() [Same problem, the username stuff]
    @Test
    public void testSignInManagerAccountIncorrectPassword(){

        Manager manager_1 = manager(MANAGER_USERNAME_D, MANAGER_NAME, MANAGER_PASSWORD, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));

        lenient().when(managerDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Manager> managers = new ArrayList<Manager>();
            managers.add(manager_1);
            return managers;

        });
        
        Manager a = null; 
        String error = ""; 

        try{
            a = service.signInManagerAccount(MANAGER_USERNAME_E, MANAGER_INCORRECT_PASSWORD);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is incorrect", error); 
    }

    // This can cause the failure of testSignInManager() [Same problem, the username stuff]
    @Test
    public void testSignInManagerAccountPasswordWithWhitespaces(){

        Manager manager_1 = manager(MANAGER_USERNAME_D, MANAGER_NAME, MANAGER_PASSWORD, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));

        lenient().when(managerDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Manager> managers = new ArrayList<Manager>();
            managers.add(manager_1);
            return managers;

        });
        
        Manager a = null; 
        String error = ""; 

        try{
            a = service.signInManagerAccount(MANAGER_USERNAME_E, "   ");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is incorrect", error); 
    }

    //-----------------------------------------------Testing editManagerAccount()--------------------------------------------------


    @Test
    public void testEditManagerAccount(){

        Manager manager_1 = manager(MANAGER_USERNAME_D, MANAGER_NAME, MANAGER_PASSWORD, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));

        lenient().when(managerDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Manager> managers = new ArrayList<Manager>();
            managers.add(manager_1);
            return managers;

        });

        Manager a = null; 

        try{
            a = service.editManagerAccount(MANAGER_NEW_NAME, MANAGER_NEW_PASSWORD);
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(MANAGER_NEW_NAME, a.getName());
        assertEquals(MANAGER_NEW_PASSWORD, a.getPassword());
        assertNotEquals(MANAGER_NAME, a.getName());
        assertNotEquals(MANAGER_PASSWORD, a.getName());
    }

    @Test
    public void testEditManagerAccountEmptyName(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.editManagerAccount("", MANAGER_PASSWORD);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This name is invalid", error); 
    
    }

    @Test
    public void testEditManagerAccountNullName(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.editManagerAccount(null, MANAGER_PASSWORD);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This name is invalid", error); 
    
    }

    @Test
    public void testEditManagerAccountPasswordContainsWhitespaces(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.editManagerAccount(MANAGER_NEW_NAME, "  ");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    
    }

    @Test
    public void testEditManagerAccountDoesntExist(){
        Manager manager_1 = null; 

        lenient().when(managerDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Manager> managers = new ArrayList<Manager>();
            managers.add(manager_1);
            return managers;

        });

        Manager a = null; 
        String error = ""; 

        try{
            a = service.editManagerAccount(MANAGER_NEW_NAME, MANAGER_NEW_PASSWORD);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("A manager account doesn't exist", error); 

    }

    @Test
    public void testEditManagerAccountPasswordLessThan8(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.editManagerAccount(MANAGER_NEW_NAME, "123");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    
    }

    @Test
    public void testEditManagerAccountPasswordGreaterThan30(){
        Manager a = null; 

        String error = ""; 

        try{
            a = service.editManagerAccount(MANAGER_NEW_NAME, "abcdefghijklmnopqrstuvwxyz12345678");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    
    }



    /**
     * Private Constructor of a Manager 
     * @param username
     * @param name
     * @param password
     * @param loanNumber
     * @param mms
     * @return
     */
    private Manager manager(String username, String name, String password, MuseumManagementSystem mms){
        Manager manager = new Manager(); 
        manager.setUsername(username); 
        manager.setName(name); 
        manager.setPassword(password); 
        manager.setMuseumManagementSystem(mms);
        return manager;
    }
}
