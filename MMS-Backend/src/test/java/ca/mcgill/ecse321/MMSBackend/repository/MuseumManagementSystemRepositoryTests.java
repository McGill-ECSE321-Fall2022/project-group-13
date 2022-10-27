package ca.mcgill.ecse321.MMSBackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.MMSBackend.dao.MuseumManagementSystemRepository;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MuseumManagementSystemRepositoryTests {

    @Autowired
    private MuseumManagementSystemRepository mmsRepository;

    @AfterEach
    public void clearDatabase(){
        
        // Delete the ticketRepository first to avoid violating not-null constraint
        mmsRepository.deleteAll();

    }

    @Test
    public void testPersistAndLoadDonationRequest() {

        // Create a museum management system
        MuseumManagementSystem mms = new MuseumManagementSystem();
        String name = "The Museum";
        Time openTime = Time.valueOf("9:00:00");
        Time closeTime = Time.valueOf("17:00:00");
        int maxLoanNumber = 10;
        double ticketFee = 66.66;

        // Set the attributes of the museum management system
        mms.setName(name);
        mms.setOpenTime(openTime);
        mms.setCloseTime(closeTime);
        mms.setMaxLoanNumber(maxLoanNumber);
        mms.setTicketFee(ticketFee);

        // Save the museum management system
        mmsRepository.save(mms);

        //Set id of mms
        Integer mmsId = mms.getSystemId();

        mms = null;

        mms = mmsRepository.findMuseumManagementSystemBySystemId(mmsId);

        // Check if the mms is not null
        assertNotNull(mms);

        // Check if the mms has the correct attributes
        assertEquals(mmsId, mms.getSystemId());
        assertEquals(openTime, mms.getOpenTime());
        assertEquals(closeTime, mms.getCloseTime());
        assertEquals(maxLoanNumber, mms.getMaxLoanNumber());
        assertEquals(ticketFee, mms.getTicketFee());
    }
}
