package ca.mcgill.ecse321.MMSBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MMSBackend.dao.ManagerRepository;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.Manager;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

@Service
public class ManagerAccountService {

    @Autowired
    ManagerRepository managerRepository; 
    
    /**
     * Create a Manager account 
     * @param - Username: string that will be used as the unique identifier of the account 
     * @author - Nikolas Pasichnik 
     */
    @Transactional
	public Manager createManager(String username, String name, String password, MuseumManagementSystem mms) {

        // Case where any of the parameters are null
        if (mms.equals(null) || username.equals(null) || name.equals(null) || password.equals(null)){
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

        if (getManager().size() == 0) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "A Manager already exists");
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
     * get the manager of the system 
     * @author - Nikolas Pasichnik 
     */
	@Transactional
	public List<Manager> getManager() {
		return toList(managerRepository.findAll());
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

    /**
     * @param username - String that will be used to locate the account associated to that unique ID  
     * @param name - Sring that is the name of the user 
     * @param password - String that is used to verify that it is the correct employee signing in 
     * @author - Nikolas Pasichnik 
     */
    public Manager editManagerAccount(String username, String name, String password){
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

        return manager; 
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
