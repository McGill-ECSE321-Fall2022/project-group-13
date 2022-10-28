package ca.mcgill.ecse321.MMSBackend.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.Ticket;

/**
 * @author Lucy Zhang
 */
public interface TicketRepository extends CrudRepository<Ticket, Integer>{
    public Ticket findTicketByTicketId(Integer id);  
}
