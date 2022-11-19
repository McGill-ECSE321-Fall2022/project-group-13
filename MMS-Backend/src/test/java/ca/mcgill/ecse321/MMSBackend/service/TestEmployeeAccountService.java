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
import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
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
public class TestEmployeeAccountService {
    @Mock 
    private EmployeeRepository employeeDao; 

    @Mock 
    private ClientRepository clientDao; 
    
    @Mock 
    private MuseumManagementSystemRepository mmsDao; 

    @InjectMocks
    private EmployeeAccountService service; 

    public static final String CLIENT_USERNAME = "ClientUsername";
    
    private static final String EMPLOYEE_USERNAME = "EmployeeUsername";
    private static final String EMPLOYEE_USERNAME_A = "EmployeeUsername_A";
    private static final String EMPLOYEE_USERNAME_B = "EmployeeUsername_B";
    private static final String EMPLOYEE_USERNAME_C = "EmployeeUsername_C";
    private static final String EMPLOYEE_USERNAME_D = "EmployeeUsername_D";
    private static final String EMPLOYEE_USERNAME_E = "EmployeeUsername_E";
    private static final String EXISTING_EMPLOYEE_USERNAME = "ExistingEmployeeUsername";
    private static final String FAKE_EMPLOYEE_USERNAME = "FakeEmployeeUsername";

    private static final String EMPLOYEE_NAME = "EmployeeName";
    private static final String EMPLOYEE_NEW_NAME = "NewEmployeeName";
    private static final String EMPLOYEE_NEW_PASSWORD = "NewEmployeePasword"; 
    private static final String EMPLOYEE_PASSWORD = "EmployeePassword"; 
    private static final String EMPLOYEE_INCORRECT_PASSWORD = "IncorrectEmployeePassword"; 

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

        lenient().when(employeeDao.findEmployeeByUsername(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(EMPLOYEE_USERNAME_B)) {
                return employee(EMPLOYEE_USERNAME_B,EMPLOYEE_NAME,EMPLOYEE_PASSWORD,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            }
            else if (invocation.getArgument(0).equals(EMPLOYEE_USERNAME_C)){
                return employee(EMPLOYEE_USERNAME_C,EMPLOYEE_NAME,EMPLOYEE_PASSWORD,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            }
            else if (invocation.getArgument(0).equals(EMPLOYEE_USERNAME_D)){
                return employee(EMPLOYEE_USERNAME_D,EMPLOYEE_NAME,EMPLOYEE_PASSWORD,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            }
            else if (invocation.getArgument(0).equals(EMPLOYEE_USERNAME_E)){
                return employee(EMPLOYEE_USERNAME_E,EMPLOYEE_NAME,EMPLOYEE_PASSWORD,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            }
            else if (invocation.getArgument(0).equals(FAKE_EMPLOYEE_USERNAME)){
                return null; 
            }
            else if (invocation.getArgument(0).equals(EMPLOYEE_USERNAME_C)){
                return employee(EXISTING_EMPLOYEE_USERNAME,EMPLOYEE_NAME,EMPLOYEE_PASSWORD,mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
            }
            else {
                return null;
        }
    });

    lenient().when(clientDao.existsById(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
        if(invocation.getArgument(0).equals(CLIENT_USERNAME)) {
            return true;
        }
        else {
            return false;
        }
    });


        lenient().when(employeeDao.existsById(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(EXISTING_EMPLOYEE_USERNAME)) {
                return true;
            }
            if(invocation.getArgument(0).equals(EMPLOYEE_USERNAME_C)) {
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
    lenient().when(employeeDao.save(any(Employee.class))).thenAnswer(returnParameterAsAnswer);
    }

    //-----------------------------------------------Testing createEmployee()--------------------------------------------------

    @Test
    public void testCreateEmployee(){
        assertEquals(0, service.getAllEmployees().size());

        Employee a = null; 

        try{
            a = service.createEmployee(EMPLOYEE_USERNAME, EMPLOYEE_NAME, EMPLOYEE_PASSWORD, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(EMPLOYEE_USERNAME, a.getUsername());
        assertEquals(EMPLOYEE_NAME, a.getName());
        assertEquals(EMPLOYEE_PASSWORD, a.getPassword());
    }

    @Test
    public void testCreateEmployeeMMSNull(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(EMPLOYEE_USERNAME, EMPLOYEE_NAME,EMPLOYEE_PASSWORD, null);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateEmployeeUsernameNull(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(null, EMPLOYEE_NAME, EMPLOYEE_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateEmployeeNameNull(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(EMPLOYEE_USERNAME, null, EMPLOYEE_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateEmployeePaswordNull(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(EMPLOYEE_USERNAME, EMPLOYEE_NAME, null,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateEmployeeUsernameEmpty(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee("", EMPLOYEE_NAME, EMPLOYEE_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateEmployeeNameEmpty(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(EMPLOYEE_USERNAME, "", EMPLOYEE_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateEmployeePasswordEmpty(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(EMPLOYEE_USERNAME, EMPLOYEE_NAME, "",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Cannot have empty fields", error);
    }

    @Test
    public void testCreateEmployeeUsernameWithWhitespaces(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee("  ", EMPLOYEE_NAME, EMPLOYEE_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("The username cannot have spaces", error);
    }

    @Test
    public void testCreateEmployeeNameOnlyWhitespaces(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(EMPLOYEE_USERNAME, "  ", EMPLOYEE_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid name", error);
    }


    @Test
    public void testCreateEmployeePasswordWithWhitespaces(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(EMPLOYEE_USERNAME, EMPLOYEE_NAME, "  ",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid password", error);
    }

    @Test
    public void testCreateEmployeePasswordLessThan8(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(EMPLOYEE_USERNAME, EMPLOYEE_NAME, "123",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid password", error);
    }

    @Test
    public void testCreateEmployeePasswordMoreThan30(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(EMPLOYEE_USERNAME, EMPLOYEE_NAME, "abcdefghijklmnopqrstuvwxyz12345678",  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("Invalid password", error);
    }

    @Test
    public void testCreateEmployeeWithUsedUsername(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(EXISTING_EMPLOYEE_USERNAME, EMPLOYEE_NAME, EMPLOYEE_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This username is already taken", error);
    }

    @Test
    public void testCreateEmployeeWithUsedClientUsername(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.createEmployee(CLIENT_USERNAME, EMPLOYEE_NAME, EMPLOYEE_PASSWORD,  mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This username is already taken", error);
    }

    //-----------------------------------------------Testing getEMPLOYEE()--------------------------------------------------

    @Test
    public void testGetEmployee(){
        Employee a = null; 

        try{
            a = service.getEmployee(EMPLOYEE_USERNAME_B);
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(EMPLOYEE_USERNAME_B, a.getUsername());
        assertEquals(EMPLOYEE_NAME, a.getName());
        assertEquals(EMPLOYEE_PASSWORD, a.getPassword());
    }

    @Test
    public void testGetEmployeeEmptyUsername(){
        Employee a = null; 

        String error = "";

        try{
            a = service.getEmployee("");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This username is invalid", error);
    }

    @Test
    public void testGetEmployeeNullUsername(){
        Employee a = null; 

        String error = "";

        try{
            a = service.getEmployee(null);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This username is invalid", error);
    }

    @Test
    public void testGetEmployeeUsernameWithWhitespaces(){
        Employee a = null; 

        String error = "";

        try{
            a = service.getEmployee("   ");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This username is invalid", error);
    }

    @Test
    public void testGetEmployeeNotFound(){
        Employee a = null; 

        String error = "";

        try{
            a = service.getEmployee(FAKE_EMPLOYEE_USERNAME);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This employee account does not exist", error);
    }

    //-----------------------------------------------Testing getAllEmployee())--------------------------------------------------
    
    @Test
    public void testGetAllEmployees(){

        Employee employee_1 = service.createEmployee(EMPLOYEE_USERNAME_A, EMPLOYEE_NAME, EMPLOYEE_PASSWORD, mmsDao.findMuseumManagementSystemBySystemId(MMS_ID));

        lenient().when(employeeDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {

            List<Employee> employees = new ArrayList<Employee>();
            employees.add(employee_1);
            return employees;

        });

        List<Employee> allEmployees = null; 

        try{
            allEmployees = service.getAllEmployees();
        }catch (MuseumManagementSystemException e){
            fail();
        }

        assertNotNull(allEmployees);
        assertEquals(1, allEmployees.size());
        assertEquals(employee_1, allEmployees.get(0));
    }

    //-----------------------------------------------Testing deleteEmployee()--------------------------------------------------

    @Test
    public void testDeleteEmployee(){

        try{
            service.deleteEmployee(EXISTING_EMPLOYEE_USERNAME);
        }catch (MuseumManagementSystemException e){
            fail(); 
        }
    }

    @Test
    public void testDeleteEmployeeNotFound(){

        String error = ""; 
        try{
            service.deleteEmployee(EMPLOYEE_USERNAME);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage(); 
        }

        assertEquals("Employee not found", error);
    }

    //-----------------------------------------------Testing signInClientAccount()--------------------------------------------------

    @Test
    public void testSignInEmployeeAccount(){
        Employee a = null; 

        try{
            a = service.signInEmployeeAccount(EMPLOYEE_USERNAME_D, EMPLOYEE_PASSWORD);
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(EMPLOYEE_USERNAME_D, a.getUsername());
        assertEquals(EMPLOYEE_NAME, a.getName());
        assertEquals(EMPLOYEE_PASSWORD, a.getPassword());

    } 

    @Test
    public void testSignInEmployeeAccountEmptyPassword(){
        
        Employee a = null; 
        String error = ""; 

        try{
            a = service.signInEmployeeAccount(EMPLOYEE_USERNAME_D, "");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    }

    @Test
    public void testSignInEmployeeAccountNullPassword(){
        
        Employee a = null; 
        String error = ""; 

        try{
            a = service.signInEmployeeAccount(EMPLOYEE_USERNAME_D, null);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    }


    // This can cause the failure of testSignInEmployee() [Same problem, the username stuff]
    @Test
    public void testSignInEmployeeAccountIncorrectPassword(){
        
        Employee a = null; 
        String error = ""; 

        try{
            a = service.signInEmployeeAccount(EMPLOYEE_USERNAME_E, EMPLOYEE_INCORRECT_PASSWORD);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is incorrect", error); 
    }

    // This can cause the failure of testSignInEmployee() [Same problem, the username stuff]
    @Test
    public void testSignInEmployeeAccountPasswordWithWhitespaces(){
        
        Employee a = null; 
        String error = ""; 

        try{
            a = service.signInEmployeeAccount(EMPLOYEE_USERNAME_E, "   ");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is incorrect", error); 
    }

    //-----------------------------------------------Testing editEmployeeAccount()--------------------------------------------------

    @Test
    public void testEditEmployeeAccount(){
        Employee a = null; 

        try{
            a = service.editEmployeeAccount(EMPLOYEE_USERNAME_C, EMPLOYEE_NEW_NAME, EMPLOYEE_NEW_PASSWORD);
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(EMPLOYEE_NEW_NAME, a.getName());
        assertEquals(EMPLOYEE_NEW_PASSWORD, a.getPassword());
        assertNotEquals(EMPLOYEE_NAME, a.getName());
        assertNotEquals(EMPLOYEE_PASSWORD, a.getName());
    }

    @Test
    public void testEditEmployeeAccountEmptyName(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.editEmployeeAccount(EMPLOYEE_USERNAME_C, "", EMPLOYEE_PASSWORD);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This name is invalid", error); 
    
    }

    @Test
    public void testEditEmployeeAccountNullName(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.editEmployeeAccount(EMPLOYEE_USERNAME_C, null, EMPLOYEE_PASSWORD);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This name is invalid", error); 
    
    }

    @Test
    public void testEditEmployeeAccountPasswordContainsWhitespaces(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.editEmployeeAccount(EMPLOYEE_USERNAME_C, EMPLOYEE_NAME, "  ");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    
    }

    @Test
    public void testEditEmployeeAccountPasswordLessThan8(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.editEmployeeAccount(EMPLOYEE_USERNAME_C, EMPLOYEE_NAME, "123");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    
    }

    @Test
    public void testEditEmployeeAccountPasswordGreaterThan30(){
        Employee a = null; 

        String error = ""; 

        try{
            a = service.editEmployeeAccount(EMPLOYEE_USERNAME_C, EMPLOYEE_NAME, "abcdefghijklmnopqrstuvwxyz12345678");
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("This password is invalid", error); 
    
    }

    /**
     * Private Constructor of a EMPLOYEE 
     * @param username
     * @param name
     * @param password
     * @param loanNumber
     * @param mms
     * @return
     */
    private Employee employee(String username, String name, String password, MuseumManagementSystem mms){
        Employee employee = new Employee(); 
        employee.setUsername(username); 
        employee.setName(name); 
        employee.setPassword(password); 
        employee.setMuseumManagementSystem(mms);
        return employee;
    }
}
