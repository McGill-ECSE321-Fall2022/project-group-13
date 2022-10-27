package ca.mcgill.ecse321.MMSBackend.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, String>{
    public Ticket findTicektByTicketId(String id);  
}
