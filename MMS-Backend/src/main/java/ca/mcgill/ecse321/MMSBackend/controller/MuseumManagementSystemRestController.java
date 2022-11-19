package ca.mcgill.ecse321.MMSBackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.MMSBackend.dto.*;
import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;
import ca.mcgill.ecse321.MMSBackend.service.MuseumManagementSystemService;

/**
 * @author Lucy Zhang (Lucy-Zh), Yu An Lu (yu-an-lu), Nazia Chowdhury (naziaC)
 *         and Samantha Perez Hoffman (samperezh)
 *         The MmsRestController class is responsible for exposing
 *         the business logic declared in MuseumManagementSystemService using a
 *         REST
 *         API.
 */
@CrossOrigin(origins = "*")
@RestController
public class MuseumManagementSystemRestController {

    @Autowired
    private MuseumManagementSystemService mmsService;

    /**
     * Create a new museum management system
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @return
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/mms", "/mms/" })
    public ResponseEntity<MuseumManagementSystemDto> createMms() throws IllegalArgumentException {
        return new ResponseEntity<>(ToDtoHelper.convertToDto(mmsService.createMuseumManagementSystem()), HttpStatus.CREATED);
    }

    /**
     * Get a mms by id
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @param systemId
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms/{systemId}", "/mms/{systemId}/" })
    public ResponseEntity<MuseumManagementSystemDto> getMms(@PathVariable("systemId") int systemId) throws IllegalArgumentException {
        return new ResponseEntity<>(ToDtoHelper.convertToDto(mmsService.getMuseumManagementSystem(systemId)), HttpStatus.OK);
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
    @PutMapping(value = { "/mms/{systemId}", "/mms/{systemId}/" })
    public ResponseEntity<MuseumManagementSystemDto> updateMmsName(@PathVariable("systemId") int systemId, @RequestParam String name)
            throws IllegalArgumentException {
        mmsService.setMuseumManagementSystemName(systemId, name);
        return new ResponseEntity<>(ToDtoHelper.convertToDto(mmsService.getMuseumManagementSystem(systemId)), HttpStatus.OK);
    }

    /**
     * Get mms
     * 
     * @Author: Lucy Zhang (Lucy-Zh)
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms", "/mms/" })
    public ResponseEntity<List<MuseumManagementSystemDto>> getAllMms() throws IllegalArgumentException {

        List<MuseumManagementSystemDto> mmsDto = new ArrayList<>();

        for (MuseumManagementSystem mms : mmsService.getAllMuseumManagementSystem()) {
            mmsDto.add(ToDtoHelper.convertToDto(mms));
        }
        return new ResponseEntity<>(mmsDto, HttpStatus.OK);
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
    @PutMapping(value = { "/mms/{systemId}/fee", "/mms/{systemId}/fee/" })
    public ResponseEntity<MuseumManagementSystemDto> updateMmsTicketFee(@PathVariable("systemId") int systemId,
            @RequestParam double ticketFee) throws IllegalArgumentException {

        mmsService.setMuseumTicketPrice(systemId, ticketFee);
        return new ResponseEntity<>(ToDtoHelper.convertToDto(mmsService.getMuseumManagementSystem(systemId)), HttpStatus.OK);

    }

    /**
     * Gets a room by its id
     * 
     * @author Yu An Lu (yu-an-lu)
     * @param roomId
     * @return the room dto
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms/{roomId}", "/mms/{roomId}/" })
    public RoomDto getRoom(@PathVariable("roomId") int roomId) throws IllegalArgumentException {

        return ToDtoHelper.convertToDto(mmsService.getRoom(roomId));
    }

    /**
     * Gets all rooms in the mms
     * 
     * @author Yu An Lu (yu-an-lu)
     * @return a list of room dtos
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms/rooms", "/mms/rooms/" })
    public List<RoomDto> getAllRooms() throws IllegalArgumentException {

        List<RoomDto> roomDtos = new ArrayList<RoomDto>();

        for (Room room : mmsService.getAllRooms()) {
            roomDtos.add(ToDtoHelper.convertToDto(room));
        }

        return roomDtos;
    }

    /**
     * get museum management system's max loan number for clients
     *
     * @author : Nazia Chowdhury (naziaC)
     * @param systemId
     */
    @GetMapping(value = { "/mms/maxLoanNumber/{systemId}", "/mms/maxLoanNumber/{systemId}/" })
    public int getMaxLoanNumberOfSystem(@PathVariable("systemId") int systemId) throws IllegalArgumentException {
        return mmsService.getMaxLoanNumberOfMms(systemId);
    }

    /**
     * Set museum management system's max loan number for clients
     *
     * @author : Nazia Chowdhury (naziaC)
     * @param systemId
     */
    @PutMapping(value = { "/mms/setMaxLoanNumber/{systemId}", "/mms/setMaxLoanNumber/{systemId}/" })
    public MuseumManagementSystemDto setMaxLoanNumberOfSystem(@PathVariable("systemId") int systemId, @RequestParam int maxLoanNumber)
            throws IllegalArgumentException {
        mmsService.setMaxLoanNumberOfMms(systemId, maxLoanNumber);
        return ToDtoHelper.convertToDto(mmsService.getMuseumManagementSystem(systemId));
    }

    /**
     * Set museum management system's opening hours
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @param systemId
     * @param openTime
     * @param closeTime
     * @return
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "mms/setOpeningHours/{systemId}", "mms/setOpeningHours/{systemId}/" })
    public ResponseEntity<MuseumManagementSystemDto> setOpeningHours(@PathVariable("systemId") int systemId,
            @RequestParam Time openTime, @RequestParam Time closeTime) throws IllegalArgumentException {
        mmsService.setOpeningHours(openTime, closeTime, systemId);
        return new ResponseEntity<MuseumManagementSystemDto>(ToDtoHelper.convertToDto(mmsService.getMuseumManagementSystem(systemId)), HttpStatus.OK);
    }

    /**
     * Get museum management system's opening hours
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @param systemId
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "mms/getOpeningHours/{systemId}", "mms/getOpeningHours/{systemId}/" })
    public ResponseEntity<List<Time>> getOpeningHours(@PathVariable("systemId") int systemId) throws IllegalArgumentException {
        return new ResponseEntity<>(mmsService.getOpeningHours(systemId), HttpStatus.OK);
    }

    /**
     * Get specificWeekDay by dayType
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @param day
     * @return
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms/DayByDayTime", "/mms/DayByDayTime/" })
    public ResponseEntity<SpecificWeekDayDto> getSpecificWeekDayByDayType(@RequestParam String day)
            throws IllegalArgumentException {
        DayType dayType = ToDtoHelper.convertStringToDayType(day);
        return new ResponseEntity<SpecificWeekDayDto>(ToDtoHelper.convertToDto(mmsService.getSpecificWeekDayByDayType(dayType)), HttpStatus.OK);
    }
}
