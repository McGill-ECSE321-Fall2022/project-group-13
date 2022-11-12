package ca.mcgill.ecse321.MMSBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.EmployeeRepository;
import ca.mcgill.ecse321.MMSBackend.dao.ManagerRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.Manager;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

@Service
public class AccountManagementService {
    @Autowired
    ClientRepository clientRepository; 
    @Autowired
    EmployeeRepository employeeRepository; 
    @Autowired
    ManagerRepository managerRepository; 

    /**
     * Create a Manager account 
     * @param - Username: string that will be used as the unique identifier of the account 
     * @author - Nikolas Pasichnik 
     */
    @Transactional
	public Manager createManager(String username, String name, String password, MuseumManagementSystem mms) {

        // Case where the mms is null 
        if (mms == null){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "The MuseumManagementSystem is null");
        }

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the name is empty or if it contains whitespaces
        if (name.equals("") || name.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This name is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (password.equals("") || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        // Creating a manager
		Manager manager = new Manager();
		manager.setUsername(username);
        manager.setName(name); 
        manager.setPassword(password); 
        manager.setMuseumManagementSystem(mms);
		managerRepository.save(manager);
		return manager;
	}

    /**
     * Create a Client account 
     * @param - Username: string that will be used as the unique identifier of the account 
     * @author - Nikolas Pasichnik 
     */
    @Transactional
	public Client createClient(String username, String name, String password, MuseumManagementSystem mms) {
        
        // Case where the mms is null 
        if (mms == null){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "The MuseumManagementSystem is null");
        }

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the name is empty or if it contains whitespaces
        if (name.equals("") || name.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This name is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (password.equals("") || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        // Creating a client
		Client client = new Client();
		client.setUsername(username);
        client.setName(name);
        client.setPassword(password); 
        client.setMuseumManagementSystem(mms);
        client.setCurrentLoanNumber(0); 
		clientRepository.save(client);
		return client;
	}

    /**
     * Create a Emlpoyee account 
     * @param - Username: string that will be used as the unique identifier of the account 
     * @author - Nikolas Pasichnik 
     */
    @Transactional
    public Employee createEmployee(String username, String name, String password, MuseumManagementSystem mms) { 

        // Case where the mms is null 
        if (mms == null){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "The MuseumManagementSystem is null");
        }

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the name is empty or if it contains whitespaces
        if (name.equals("") || name.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This name is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (password.equals("") || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        // Creating an employee
        Employee employee = new Employee(); 
        employee.setUsername(username); 
        employee.setName(name); 
        employee.setPassword(password); 
        employee.setMuseumManagementSystem(mms);
        employeeRepository.save(employee); 
        return employee; 
    }
    
    /**
     * get a Manager account 
     * @param - Username: string that will be used to locate the account associated to that unique ID  
     * @author - Nikolas Pasichnik 
     */
    @Transactional
	public Manager getManager(String username) {

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

		Manager manager = managerRepository.findManagerByUsername(username);

        if (manager == null){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This manager account does not exist");
        }

		return manager;
	}

    /**
     * get a Client account 
     * @param - Username: string that will be used to locate the account associated to that unique ID  
     * @author - Nikolas Pasichnik 
     */
    @Transactional
	public Client getClient(String username) {

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

		Client client = clientRepository.findClientByUsername(username);

        if (client == null){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This client account does not exist");
        }

		return client;
	}

    /**
     * get an Employee account 
     * @param - Username: string that will be used to locate the account associated to that unique ID  
     * @author - Nikolas Pasichnik 
     */
    @Transactional
	public Employee getEmployee(String username) {

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

		Employee employee = employeeRepository.findEmployeeByUsername(username);

        if (employee == null){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This employee account does not exist");
        }

		return employee;
	}

    /**
     * get a list of all Client accounts 
     * @author - Nikolas Pasichnik 
     */
	@Transactional
	public List<Client> getAllClients() {
		return toList(clientRepository.findAll());
	}

    /**
     * get a list of all Employee accounts
     * @author - Nikolas Pasichnik 
     */
	@Transactional
	public List<Employee> getAllEmployees() {
		return toList(employeeRepository.findAll());
	}

    /**
     * get the manager of the system 
     * @author - Nikolas Pasichnik 
     */
	@Transactional
	public List<Manager> getManager() {
		return toList(managerRepository.findAll());
	}
    

    /**
     * delete an Employee account 
     * @param - Username: string that will be used to locate the account associated to that unique ID  
     * @author - Nikolas Pasichnik 
     */
    @Transactional
    public void deleteEmployee(String username){

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the employee doesn't exist 
        if (employeeRepository.existsById(username) == false){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "An employee with that username does not exist");
        }

        employeeRepository.deleteById(username);   
    }

    /**
     * delete a Client account 
     * @param - Username: string that will be used to locate the account associated to that unique ID  
     * @author - Nikolas Pasichnik 
     */
    @Transactional
    public void deleteClient(String username){

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the client doesn't exist 
        if (clientRepository.existsById(username) == false){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "A client with that username does not exist");
        }

        clientRepository.deleteById(username);   
    }

    /**
     * Signing into an Client account  
     * @param username - String that will be used to locate the account associated to that unique ID  
     * @param password - String that is used to verify that it is the correct client signing in 
     * @author - Nikolas Pasichnik
     * @return 
     */
    @Transactional
    public Client signInClientAccount(String username, String password){

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (password.equals("") || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        // Case where a client with the entered ID doesn't exist
        if (clientRepository.existsById(username) == false) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "A client with this username does not exist");
        }

        Client client = clientRepository.findClientByUsername(username); 

        // Case where the entered password does not match with the one in the system 
        if (client.getPassword().equals(password) == false) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is incorrect");
        }

        return client; 
    }

    /**
     * Signing into an Employee account 
     * @param username - String that will be used to locate the account associated to that unique ID  
     * @param password - String that is used to verify that it is the correct employee signing in 
     * @author - Nikolas Pasichnik 
     * @return - false if failed to sign in, true if succesfully signed in
     */
    @Transactional
    public Employee signInEmployeeAccount(String username, String password){

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (password.equals("") || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        // Case where an employee with the entered ID doesn't exist
        if (employeeRepository.existsById(username) == false) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "An employee with this username does not exist");
        }

        Employee employee = employeeRepository.findEmployeeByUsername(username); 

        // Case where the entered password does not match with the one in the system 
        if (employee.getPassword().equals(password) == false) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is incorrect");
        }

        return employee; 

    }

    /**
     * Signing into a manager account 
     * @param username - String that will be used to locate the account associated to that unique ID  
     * @param password - String that is used to verify that it is in fact the manager signing in 
     * @author - Nikolas Pasichnik 
     * @return - false if failed to sign in, true if succesfully signed in
     */
    @Transactional
    public Manager signInManagerAccount(String username, String password){

        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (password.equals("") || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        // Case where a manager with the entered ID doesn't exist
        if (managerRepository.existsById(username) == false) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "A manager with this username does not exist");
        }

        Manager manager = managerRepository.findManagerByUsername(username); 

        // Case where the entered password does not match with the one in the system 
        if (manager.getPassword().equals(password) == false) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is incorrect");
        }

        return manager; 
    }

    // Create methods to edit the information of different types of account 

    /**
     * @param username - String that will be used to locate the account associated to that unique ID  
     * @param name - Sring that is the name of the user 
     * @param password - String that is used to verify that it is the correct employee signing in 
     * @author - Nikolas Pasichnik 
     */
    public void editClientAccount(String username, String name, String password){
        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the name is empty or if it contains whitespaces
        if (name.equals("") || name.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This name is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (password.equals("") || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }
        
        // Case where a client with the entered ID doesn't exist
        if (clientRepository.existsById(username) == false) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "A client with this username does not exist");
        }

        // Getting the client 
        Client client = clientRepository.findClientByUsername(username); 

        client.setName(username); 
        client.setPassword(password); 
    }


    /**
     * @param username - String that will be used to locate the account associated to that unique ID  
     * @param name - Sring that is the name of the user 
     * @param password - String that is used to verify that it is the correct employee signing in 
     * @author - Nikolas Pasichnik 
     */
    public void editEmployeeAccount(String username, String name, String password){
        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the name is empty or if it contains whitespaces
        if (name.equals("") || name.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This name is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (password.equals("") || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }
        
        // Case where a client with the entered ID doesn't exist
        if (employeeRepository.existsById(username) == false) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "An employee with this username does not exist");
        }

        // Getting the client 
        Employee employee = employeeRepository.findEmployeeByUsername(username); 

        employee.setName(username); 
        employee.setPassword(password); 
    }

    /**
     * @param username - String that will be used to locate the account associated to that unique ID  
     * @param name - Sring that is the name of the user 
     * @param password - String that is used to verify that it is the correct employee signing in 
     * @author - Nikolas Pasichnik 
     */
    public void editManagerAccount(String username, String name, String password){
        // Case where the username is empty or if it contains whitespaces
        if (username.equals("") || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

        // Case where the name is empty or if it contains whitespaces
        if (name.equals("") || name.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This name is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (password.equals("") || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }
        
        // Case where a client with the entered ID doesn't exist
        if (managerRepository.existsById(username) == false) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "A manager with this username does not exist");
        }

        // Getting the client 
        Manager manager = managerRepository.findManagerByUsername(username); 

        manager.setName(username); 
        manager.setPassword(password); 
    }

    /**
     * Helper method to create a list of type T
     * @param - iterable: An iterable object of type T 
     * @author - EventRegistration Authors  
     */
    private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
    
}
