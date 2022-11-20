package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MMSBackend.model.Employee;

/**
 * @author Nikolas Pasichnik (NikolasPasichnik)
 */
public interface EmployeeRepository extends CrudRepository<Employee, String> {
    public Employee findEmployeeByUsername(String id);   
}



