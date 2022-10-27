package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.Shift;

public interface ShiftRepository extends CrudRepository<Shift, Integer>{
    public Shift findShiftByShiftId(String id); 
}
