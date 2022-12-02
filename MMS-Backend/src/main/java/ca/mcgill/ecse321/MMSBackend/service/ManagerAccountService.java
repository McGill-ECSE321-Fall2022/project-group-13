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

/**
 * @author Nikolas Pasichnik (NikolasPasichnik)
 *         The ClientAccountService class implements the use case based on
 *         the following requirement:
 *         “FREQ01: Upon the creation of a new account of the type Manager,
 *         Employee or Client,
 *         the museum management system shall record its name, unique username,
 *         and password.”
 */
@Service
public class ManagerAccountService {

    @Autowired
    ManagerRepository managerRepository;

    /**
     * get the manager of the system
     * 
     * @author Nikolas Pasichnik (NikolasPasichnik)
     */
    @Transactional
    public Manager getManager() {
        List<Manager> list = toList(managerRepository.findAll());
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * Signing into a manager account
     * 
     * @param username - String that will be used to locate the account associated
     *                 to that unique ID
     * @param password - String that is used to verify that it is in fact the
     *                 manager signing in
     * @author Nikolas Pasichnik (NikolasPasichnik)
     * @return - false if failed to sign in, true if succesfully signed in
     */
    @Transactional
    public Manager signInManagerAccount(String username, String password) {
        // Case where the password is empty or if it contains whitespaces
        if (password == null || password.equals("")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }
        Manager manager = getManager();
        if (getManager() == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "A manager account doesn't exist");
        }
        // Case where the entered password does not match with the one in the system
        if (manager.getPassword().equals(password) == false || password.contains(" ")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is incorrect");
        }
        return manager;
    }

    /**
     * edit the Manager of the system
     * 
     * @param name     - Sring that is the name of the user
     * @param password - String that is used to verify that it is the correct
     *                 employee signing in
     * @author Nikolas Pasichnik (NikolasPasichnik)
     */
    @Transactional
    public Manager editManagerAccount(String newName, String newPassword) {
        // Case where the name is empty or if it contains whitespaces
        if (  newName == null || newName.equals("")) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This name is invalid");
        }
        // Case where the password is empty or if it contains whitespaces
        if (newPassword.contains(" ") || newPassword.length() < 8 || newPassword.length() > 30) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "This password is invalid");
        }
        Manager manager = getManager();
        if (getManager() == null) {
            throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "A manager account doesn't exist");
        }
        manager.setName(newName);
        manager.setPassword(newPassword);
        return manager;
    }

    /**
     * Helper method to create a list of type T
     * 
     * @param - iterable: An iterable object of type T
     * @author EventRegistration Authors
     */
    private <T> List<T> toList(Iterable<T> iterable) {
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }
}
