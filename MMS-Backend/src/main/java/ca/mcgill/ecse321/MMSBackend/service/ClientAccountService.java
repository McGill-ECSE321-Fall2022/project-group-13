package ca.mcgill.ecse321.MMSBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.EmployeeRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Client;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

@Service
public class ClientAccountService {

    @Autowired
    ClientRepository clientRepository; 

    @Autowired
    EmployeeRepository employeeRepository; 

    /**
     * Create a Client account 
     * @param - Username: string that will be used as the unique identifier of the account 
     * @author - Nikolas Pasichnik 
     */
    @Transactional
	public Client createClient(String username, String name, String password, MuseumManagementSystem mms) {
        
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
        if (clientRepository.existsById(username) == true || employeeRepository.existsById(username) == true){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is already taken");  
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
     * get a Client account 
     * @param - Username: string that will be used to locate the account associated to that unique ID  
     * @author - Nikolas Pasichnik 
     */
    @Transactional
	public Client getClient(String username) {

        // Case where the username is empty or if it contains whitespaces
        if (username == "" || username == null || username.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is invalid");
        }

		Client client = clientRepository.findClientByUsername(username);

        if (client == null){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This client account does not exist");
        }

		return client;
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
     * delete a Client account 
     * @param - Username: string that will be used to locate the account associated to that unique ID  
     * @author - Nikolas Pasichnik 
     */
    @Transactional
    public void deleteClient(String username){

        Client client = getClient(username);

        clientRepository.deleteById(client.getUsername());   
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

        // Case where the password is empty or if it contains whitespaces
        if (password == "" || password == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        Client client = getClient(username); 

        // Case where the entered password does not match with the one in the system 
        if (client.getPassword().equals(password) == false || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is incorrect");
        }

        return client; 
    }

    /**
     * @param username - String that will be used to locate the account associated to that unique ID  
     * @param name - Sring that is the name of the user 
     * @param password - String that is used to verify that it is the correct employee signing in 
     * @author - Nikolas Pasichnik 
     */
    @Transactional
    public Client editClientAccount(String username, String newName, String newPassword){

        // Case where the name is empty or if it contains whitespaces
        if (newName == "" || newName == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This name is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (newPassword.contains(" ") || newPassword.length() < 8 || newPassword.length() > 30) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        Client client = getClient(username);

        client.setName(newName); 
        client.setPassword(newPassword); 

        return client; 
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
