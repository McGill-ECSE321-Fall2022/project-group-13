package ca.mcgill.ecse321.MMSBackend.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

public interface MuseumManagementSystemRepository extends CrudRepository<MuseumManagementSystem, Integer> {
    public MuseumManagementSystem findMuseumManagementSystemBySystemId(Integer id);  
}
