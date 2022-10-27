package ca.mcgill.ecse321.MMSBackend.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Integer>{
    public Ticket findTicketByTicketId(Integer id);  
}
