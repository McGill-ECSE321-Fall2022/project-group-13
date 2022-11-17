package ca.mcgill.ecse321.MMSBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MMSBackend.dao.EmployeeRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Employee;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

@Service
public class EmployeeAccountService {
    @Autowired
    EmployeeRepository employeeRepository; 

    /**
     * Create a Emlpoyee account 
     * @param - Username: string that will be used as the unique identifier of the account 
     * @author - Nikolas Pasichnik 
     */
    @Transactional
    public Employee createEmployee(String username, String name, String password, MuseumManagementSystem mms) { 

        // Case where any of the parameters are null
        if (mms == null || username == null || name == null || password == null){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Cannot have empty fields");
        }
        
        // Case where any of the parameters are empty strings
        if (username == "" || name == "" || password == ""){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Cannot have empty fields");
        }

        // Case where the username contains whitespaces
        if (username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "The username cannot have spaces");
        }

        // Case where the name consists of only whitespaces
        if (name.trim().isEmpty() == true) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Invalid name");
        }

        // Case where the password contains whitespaces 
        if (password.contains(" ") || password.length() < 8 || password.length() > 30) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "Invalid password");
        }

        // The username is already in use 
        if (employeeRepository.existsById(username) == true){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is already taken");  
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
     * get an Employee account 
     * @param - Username: string that will be used to locate the account associated to that unique ID  
     * @author - Nikolas Pasichnik 
     */
    @Transactional
	public Employee getEmployee(String username) {

        // Case where the username is empty or if it contains whitespaces
        if (username == "" || username == null || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

		Employee employee = employeeRepository.findEmployeeByUsername(username);

        if (employee == null){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This employee account does not exist");
        }

		return employee;
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
     * delete an Employee account 
     * @param - Username: string that will be used to locate the account associated to that unique ID  
     * @author - Nikolas Pasichnik 
     */
    @Transactional
    public void deleteEmployee(String username){

        Employee employee = getEmployee(username);

        employeeRepository.deleteById(username);    
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

        // Case where the password is empty or if it contains whitespaces
        if (password == "" || password == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        Employee employee = getEmployee(username); 

        // Case where the entered password does not match with the one in the system 
        if (employee.getPassword().equals(password) == false || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is incorrect");
        }

        return employee; 
    }

    /**
     * @param username - String that will be used to locate the account associated to that unique ID  
     * @param name - Sring that is the name of the user 
     * @param password - String that is used to verify that it is the correct employee signing in 
     * @author - Nikolas Pasichnik 
     */
    @Transactional
    public Employee editEmployeeAccount(String username, String newName, String newPassword){

        // Case where the name is empty or if it contains whitespaces
        if (newName == "" || newName == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This name is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (newPassword.contains(" ") || newPassword.length() < 8 || newPassword.length() > 30) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        Employee employee = getEmployee(username);

        employee.setName(newName); 
        employee.setPassword(newPassword); 

        return employee; 
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
