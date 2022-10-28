package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MMSBackend.model.Client;

/**
 * @author Nikolas Pasichnik
 */
public interface ClientRepository extends CrudRepository<Client, String> {
    public Client findClientByUsername(String id); 
}
