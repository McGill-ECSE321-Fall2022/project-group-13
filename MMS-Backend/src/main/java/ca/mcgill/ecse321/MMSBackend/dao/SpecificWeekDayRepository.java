package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;

/**
 * @author Samantha Perez Hoffman
 */  
public interface SpecificWeekDayRepository extends CrudRepository<SpecificWeekDay, DayType>{
    public SpecificWeekDay findSpecificWeekDayByDayType(DayType dayType); 
}
