package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.Shift;

/**
 * @author Samantha Perez Hoffman
 */ 
public interface ShiftRepository extends CrudRepository<Shift, Integer>{
    public Shift findShiftByShiftId(Integer id); 
}
