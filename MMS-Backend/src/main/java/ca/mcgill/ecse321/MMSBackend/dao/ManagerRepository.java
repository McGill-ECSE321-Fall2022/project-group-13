package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MMSBackend.model.Manager;

/**
 * @author Nikolas Pasichnik (NikolasPasichnik)
 */
public interface ManagerRepository extends CrudRepository<Manager, String> {
    public Manager findManagerByUsername(String id);  
}

