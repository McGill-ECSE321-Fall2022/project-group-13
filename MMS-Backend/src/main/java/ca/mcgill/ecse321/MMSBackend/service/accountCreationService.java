package ca.mcgill.ecse321.MMSBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.MMSBackend.dao.ClientRepository;
import ca.mcgill.ecse321.MMSBackend.dao.EmployeeRepository;
import ca.mcgill.ecse321.MMSBackend.dao.ManagerRepository;



@Service 
public class accountCreationService {

    // EventRepository eventRepository;
    @Autowired
    ClientRepository clientRepository; 
    @Autowired
    EmployeeRepository employeeRepository; 
    @Autowired
    ManagerRepository managerRepository; 

    
}
