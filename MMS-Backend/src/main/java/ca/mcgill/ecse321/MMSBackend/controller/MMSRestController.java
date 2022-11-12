package ca.mcgill.ecse321.MMSBackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.MMSBackend.dto.MuseumManagementSystemDto;
import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;

/**
 * @author Lucy Zhang (Lucy-Zh)
 *         The MmsRestController class is responsible for exposing
 *         the business logic declared in MuseumManagementSystemService using a REST
 *         API.
 */
@CrossOrigin(origins = "*")
@RestController
public class MmsRestController {
    
    @Autowired
    private MuseumManagementSystemService mmsService;

    /**
     * Create a new MMS
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @return
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/mms", "/mms/" })
    public MuseumManagementSystemDto createMms() throws IllegalArgumentException {
        
        return ToDtoHelper.convertToDto(mmsService.createMuseumManagementSystem());

    }

    /**
     * Get a mms by id
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param systemId
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms/{systemId}", "/mms/{systemId}/"})
    public MuseumManagementSystemDto getMms(@PathVariable("systemId") int systemId) throws IllegalArgumentException {
        
        return ToDtoHelper.convertToDto(mmsService.getMuseumManagementSystem(systemId));

    }

    /**
     * Delete mms by id
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param systemId
     * @return
     * @throws IllegalArgumentException
     */
    @DeleteMapping(value = { "/mms/{systemId}", "/mms/{systemId}/"})
    public void deleteMms(@PathVariable("systemId") int systemId) throws IllegalArgumentException {
        
        mmsService.deleteMuseumManagementSystem(systemId);

    }

     /**
     * Update mms name by id
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param systemId
     * @param name
     * @return
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/mms/{systemId}", "/mms/{systemId}/"})
    public MuseumManagementSystemDto updateMmsName(@PathVariable("systemId") int systemId, @RequestParam String name) throws IllegalArgumentException {
        
        mmsService.setMuseumManagementSystemName(systemId, name);

        return ToDtoHelper.convertToDto(mmsService.getMuseumManagementSystem(systemId));

    }

    /**
     * Get mms
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms", "/mms/" })
    public List<MuseumManagementSystemDto> getAllMms() throws IllegalArgumentException {
        
        List<MuseumManagementSystemDto> mmsDto = new ArrayList<MuseumManagementSystemDto>();
        
        for (MuseumManagementSystem mms : mmsService.getMuseumManagementSystem()) {
            mmsDto.add(ToDtoHelper.convertToDto(mms));
        }
        
        return mmsDto;
    }

    /**
     * Set mms ticket fee
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param systemId
     * @param ticketFee
     * @return
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/mms/{systemId}/fee", "/mms/{systemId}/fee/"})
    public MuseumManagementSystemDto updateMmsTicketFee(@PathVariable("systemId") int systemId, @RequestParam double ticketFee) throws IllegalArgumentException {
        
        mmsService.setMuseumTicketPrice(systemId, ticketFee);

        return ToDtoHelper.convertToDto(mmsService.getMuseumManagementSystem(systemId));

    }

}
