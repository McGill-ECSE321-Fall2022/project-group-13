package ca.mcgill.ecse321.MMSBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.EmployeeRepository;
import ca.mcgill.ecse321.MMSBackend.dao.ManagerRepository;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.Manager;



@Service 
public class accountCreationService {

    @Autowired
    ClientRepository clientRepository; 
    @Autowired
    EmployeeRepository employeeRepository; 
    @Autowired
    ManagerRepository managerRepository; 

    // Create account methods 

    @Transactional
	public Manager createManager(String username) {
		Manager manager = new Manager();
		manager.setUsername(username);
		managerRepository.save(manager);
		return manager;
	}

    @Transactional
	public Client createClient(String username) {
		Client client = new Client();
		client.setUsername(username);
		clientRepository.save(client);
		return client;
	}

    @Transactional
    public Employee createEmployee(String username) { 
        Employee employee = new Employee(); 
        employee.setUsername(username); 
        employeeRepository.save(employee); 
        return employee; 
    }

    // Get account methods 

    @Transactional
	public Manager getManager(String username) {
		Manager manager = managerRepository.findManagerByUsername(username);
		return manager;
	}

    @Transactional
	public Client getClient(String username) {
		Client client = clientRepository.findClientByUsername(username);
		return client;
	}

    @Transactional
	public Employee getEmployee(String username) {
		Employee employee = employeeRepository.findEmployeeByUsername(username);
		return employee;
	}

    // Get account list methods 
    
	@Transactional
	public List<Client> getAllClients() {
		return toList(clientRepository.findAll());
	}

	@Transactional
	public List<Employee> getAllEmployees() {
		return toList(employeeRepository.findAll());
	}

    // Delete account

    @Transactional
    public void deleteEmployee(String username){
        employeeRepository.deleteById(username);   
    }

    @Transactional
    public void deleteClient(String username){
        clientRepository.deleteById(username);   
    }

    // Signing in accounts (NOT SURE)

    @Transactional
    public boolean signInClientAccount(String username, String password){

        // Checking if a client with that username exists 
        if (clientRepository.existsById(username)){

            // Getting client with the username 
            Client client = clientRepository.findClientByUsername(username); 

            // Case where the password is correct, client is signed in
            if (client.getPassword().equals(password)){
                return true; 
            }
            
            // Case where the password is incorrect. client is not signed in 
            else{
                return false; 
            }
        }

        // Case where a client with said username doesn't exist
        else{
            return false;
        }

    }

    @Transactional
    public boolean signInEmployeeAccount(String username, String password){

        // Checking if an employee with that username exists 
        if (employeeRepository.existsById(username)){

            // Getting employee with the username 
            Employee employee = employeeRepository.findEmployeeByUsername(username); 

            // Case where the password is correct, employee is signed in
            if (employee.getPassword().equals(password)){
                return true; 
            }
            
            // Case where the password is incorrect, employee is not signed in 
            else{
                return false; 
            }
        }

        // Case where a employee with said username doesn't exist
        else{
            return false;
        }

    }

    @Transactional
    public boolean signInManagerAccount(String username, String password){

        // Checking if an manager with that username exists 
        if (managerRepository.existsById(username)){

            // Getting manager with the username 
            Manager manager = managerRepository.findManagerByUsername(username); 

            // Case where the password is correct, manager is signed in
            if (manager.getPassword().equals(password)){
                return true; 
            }
            
            // Case where the password is incorrect, manager is not signed in 
            else{
                return false; 
            }
        }

        // Case where a manager with said username doesn't exist
        else{
            return false;
        }

    }

    // toList helper method (@author eventRegistration authors) 

    private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}

    


    

    
}
