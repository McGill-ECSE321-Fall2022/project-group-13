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
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

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

    // private static final String EMPLOYEE_USERNAME = "EmployeeUsername";
    // private static final String EMPLOYEE_NAME = "EmployeeName";
    // private static final String EMPLOYEE_PASSWORD = "EmployeePassword"; 

    // private static final String MANAGER_USERNAME = "ManagerUsername";
    // private static final String MANAGER_NAME = "ManagerName";
    // private static final String MANAGER_PASSWORD = "ManagerPassword"; 
    
    private static final MuseumManagementSystem mms = new MuseumManagementSystem(); 

    @BeforeEach
    public void setMockOutput() {
        lenient().when(clientDao.findClientByUsername(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(CLIENT_USERNAME)) {
                Client client = new Client(); 
                client.setUsername(CLIENT_USERNAME); 
                client.setName(CLIENT_NAME); 
                client.setPassword(CLIENT_PASSWORD); 
                client.setCurrentLoanNumber(CLIENT_LOAN_NUMBER); 
                client.setMuseumManagementSystem(mms); 
                return client;
            } else {
                return null;
            }
        });

        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(clientDao.save(any(Client.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateClient(){
        String client_username = "NikPasichnik"; 
        String client_name = "NikolasPasichnik"; 
        String client_password = "Charly123"; 

        Client a = null; 

        try{
            a = service.createClient(client_username, client_name, client_password, mms);
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(client_username, a.getUsername());
        assertEquals(client_name, a.getName());
        assertEquals(client_password, a.getPassword());
    }

    @Test
    public void testCreateClientNull(){
        String client_username = null; 
        String client_name = null;
        String client_password = null; 
        MuseumManagementSystem client_mms = null; 

        Client a = null; 

        String error = ""; 

        try{
            a = service.createClient(client_username, client_name, client_password, client_mms);
        }catch (MuseumManagementSystemException e){
            error = e.getMessage();
        }

        assertNull(a);
        assertEquals("The MuseumManagementSystem is null", error);
    }
}
