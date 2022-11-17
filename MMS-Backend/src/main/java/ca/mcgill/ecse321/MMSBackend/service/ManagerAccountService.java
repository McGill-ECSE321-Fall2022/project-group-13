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
        if (managerRepository.existsById(username) == true){
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This username is already taken");  
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
        if (username == "" || username == null || username.contains(" ")) {
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
	public Manager getManager() {
		return toList(managerRepository.findAll()).get(0);
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

        // Case where the password is empty or if it contains whitespaces
        if (password == "" || password == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        Manager manager = getManager(); 

        // Case where the entered password does not match with the one in the system 
        if (manager.getPassword().equals(password) == false || password.contains(" ")) {
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
    public Manager editManagerAccount(String username, String newName, String newPassword){

        // Case where the name is empty or if it contains whitespaces
        if (newName == "" || newName == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This name is invalid");
        }

        // Case where the password is empty or if it contains whitespaces
        if (newPassword.contains(" ") || newPassword.length() < 8 || newPassword.length() > 30) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }

        Manager manager = getManager();

        manager.setName(newName); 
        manager.setPassword(newPassword); 

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
