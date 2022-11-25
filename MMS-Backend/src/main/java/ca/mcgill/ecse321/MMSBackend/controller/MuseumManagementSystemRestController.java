package ca.mcgill.ecse321.MMSBackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 * and Samantha Perez Hoffman (samperezh)
 *
 * The MmsRestController class is responsible for exposing
 * the business logic declared in MuseumManagementSystemService using a
 * REST API.
 */
@CrossOrigin(origins = "*")
@RestController
public class MuseumManagementSystemRestController {

    @Autowired
    private MuseumManagementSystemService mmsService;

    /**
     * Create a new museum management system
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @throws IllegalArgumentException
     */
    @PostMapping(value = { "/mms", "/mms/" })
    public ResponseEntity<MuseumManagementSystemDto> createMms() throws IllegalArgumentException {
        return new ResponseEntity<>(convertToDto(mmsService.createMuseumManagementSystem()), HttpStatus.CREATED);
    }

    /**
     * Get a mms by id
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @param systemId
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms/getMms/{systemId}", "/mms/getMms/{systemId}/" })
    public ResponseEntity<MuseumManagementSystemDto> getMms(@PathVariable("systemId") int systemId) throws IllegalArgumentException {
        return new ResponseEntity<>(convertToDto(mmsService.getMuseumManagementSystem(systemId)), HttpStatus.OK);
    }

    /**
     * Update mms name by id
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @param systemId
     * @param name
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/mms/{systemId}", "/mms/{systemId}/" })
    public ResponseEntity<MuseumManagementSystemDto> updateMmsName(@PathVariable("systemId") int systemId, @RequestParam String name)
            throws IllegalArgumentException {
        mmsService.setMuseumManagementSystemName(systemId, name);
        return new ResponseEntity<>(convertToDto(mmsService.getMuseumManagementSystem(systemId)), HttpStatus.OK);
    }

    /**
     * Get mms
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms", "/mms/" })
    public ResponseEntity<List<MuseumManagementSystemDto>> getAllMms() throws IllegalArgumentException {

        List<MuseumManagementSystemDto> mmsDto = new ArrayList<>();

        for (MuseumManagementSystem mms : mmsService.getAllMuseumManagementSystem()) {
            mmsDto.add(convertToDto(mms));
        }
        return new ResponseEntity<>(mmsDto, HttpStatus.OK);
    }

    /**
     * Set mms ticket fee
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @param systemId
     * @param ticketFee
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/mms/{systemId}/fee", "/mms/{systemId}/fee/" })
    public ResponseEntity<MuseumManagementSystemDto> updateMmsTicketFee(@PathVariable("systemId") int systemId,
            @RequestParam double ticketFee) throws IllegalArgumentException {

        mmsService.setMuseumTicketPrice(systemId, ticketFee);
        return new ResponseEntity<>(convertToDto(mmsService.getMuseumManagementSystem(systemId)), HttpStatus.OK);

    }

    /**
     * Gets a room by its id
     * 
     * @author Yu An Lu (yu-an-lu)
     * @param roomId
     * @return the room dto
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms/getRoom/{roomId}", "/mms/getRoom/{roomId}/" })
    public RoomDto getRoom(@PathVariable("roomId") int roomId) throws IllegalArgumentException {

        return convertToDto(mmsService.getRoom(roomId));
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
            roomDtos.add(convertToDto(room));
        }

        return roomDtos;
    }

    /**
     * Gets all rooms in the mms of a certain type
     * 
     * @author Yu An Lu (yu-an-lu)
     * @param roomType
     * @return a list of room dtos of the given type
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms/rooms/type/{roomType}", "/mms/rooms/type/{roomType}/" })
    public List<RoomDto> getAllRoomsByType(@PathVariable("roomType") String roomType) throws IllegalArgumentException {

        List<RoomDto> roomDtos = new ArrayList<RoomDto>();

        for (Room room : mmsService.getAllRoomsByType(convertStringToRoomType(roomType))) {
            roomDtos.add(convertToDto(room));
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
    public ResponseEntity<Integer> getMaxLoanNumberOfSystem(@PathVariable("systemId") int systemId) throws IllegalArgumentException {
        return new ResponseEntity<Integer>(mmsService.getMaxLoanNumberOfMms(systemId), HttpStatus.OK);
    }

    /**
     * Set museum management system's max loan number for clients
     *
     * @author : Nazia Chowdhury (naziaC)
     * @param systemId
     */
    @PutMapping(value = { "/mms/setMaxLoanNumber/{systemId}", "/mms/setMaxLoanNumber/{systemId}/" })
    public ResponseEntity<MuseumManagementSystemDto> setMaxLoanNumberOfSystem(@PathVariable("systemId") int systemId, @RequestParam int maxLoanNumber)
            throws IllegalArgumentException {
        mmsService.setMaxLoanNumberOfMms(systemId, maxLoanNumber);
        return new ResponseEntity<MuseumManagementSystemDto>(convertToDto(mmsService.getMuseumManagementSystem(systemId)), HttpStatus.OK);
    }

    /**
     * Set museum management system's opening hours
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @param systemId
     * @param openTime
     * @param closeTime
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "mms/setOpeningHours/{systemId}", "mms/setOpeningHours/{systemId}/" })
    public ResponseEntity<MuseumManagementSystemDto> setOpeningHours(@PathVariable("systemId") int systemId,
            @RequestParam Time openTime, @RequestParam Time closeTime) throws IllegalArgumentException {
        mmsService.setOpeningHours(openTime, closeTime, systemId);
        return new ResponseEntity<MuseumManagementSystemDto>(convertToDto(mmsService.getMuseumManagementSystem(systemId)), HttpStatus.OK);
    }

    /**
     * Get museum management system's opening hours
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @param systemId
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
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms/DayByDayType", "/mms/DayByDayType/" })
    public ResponseEntity<SpecificWeekDayDto> getSpecificWeekDayByDayType(@RequestParam String day)
            throws IllegalArgumentException {
        DayType dayType =  convertStringToDayType(day);
        return new ResponseEntity<SpecificWeekDayDto>(convertToDto(mmsService.getSpecificWeekDayByDayType(dayType)), HttpStatus.OK);
    }

    // Helper methods to convert classes from the model into DTOs and vice-versa

    private MuseumManagementSystemDto convertToDto(MuseumManagementSystem mms) {
        if (mms == null) {
            throw new IllegalArgumentException("There is no such Museum Management System!");
        }
        return new MuseumManagementSystemDto(mms.getSystemId(), mms.getName(), mms.getOpenTime(), mms.getCloseTime(), mms.getMaxLoanNumber(), mms.getTicketFee());
    }

    private DayType convertStringToDayType(String day){
        if (day == null) {
            throw new IllegalArgumentException("DayType cannot be null!");
        }
        return switch (day) {
            case "Monday" -> DayType.Monday;
            case "Tuesday" -> DayType.Tuesday;
            case "Wednesday" -> DayType.Wednesday;
            case "Thursday" -> DayType.Thursday;
            case "Friday" -> DayType.Friday;
            case "Saturday" -> DayType.Saturday;
            case "Sunday" -> DayType.Sunday;
            default -> throw new IllegalArgumentException("Unexpected value: " + day);
        };
    }

    private SpecificWeekDayDto convertToDto(SpecificWeekDay specificWeekDay) {
        if (specificWeekDay == null) {
            throw new IllegalArgumentException("SpecificWeekDay cannot be null!");
        }
        return new SpecificWeekDayDto(convertToDto(specificWeekDay.getDayType()), specificWeekDay.getIsClosed(),
                convertToDto(specificWeekDay.getMuseumManagementSystem()));
    }

    private RoomDto convertToDto(Room r) {
        if (r == null) {
            return null;
        }
        MuseumManagementSystemDto systemDto = convertToDto(r.getMuseumManagementSystem());
        RoomDto.RoomTypeDto typeDto = convertToDto(r.getType());
        return new RoomDto(r.getRoomId(), r.getName(), typeDto, systemDto);
    }

    private RoomDto.RoomTypeDto convertToDto(Room.RoomType type) {
        if (type == null) {
            throw new IllegalArgumentException("Room type cannot be null.");
        }

        return switch (type) {
            case Small -> RoomDto.RoomTypeDto.Small;
            case Large -> RoomDto.RoomTypeDto.Large;
            case Storage -> RoomDto.RoomTypeDto.Storage;
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }

    private SpecificWeekDayDto.DayTypeDto convertToDto(DayType dayType) {
        if (dayType == null) {
            throw new IllegalArgumentException("DayType cannot be null!");
        }
        return switch (dayType) {
            case Monday -> SpecificWeekDayDto.DayTypeDto.Monday;
            case Tuesday -> SpecificWeekDayDto.DayTypeDto.Tuesday;
            case Wednesday -> SpecificWeekDayDto.DayTypeDto.Wednesday;
            case Thursday -> SpecificWeekDayDto.DayTypeDto.Thursday;
            case Friday -> SpecificWeekDayDto.DayTypeDto.Friday;
            case Saturday -> SpecificWeekDayDto.DayTypeDto.Saturday;
            case Sunday -> SpecificWeekDayDto.DayTypeDto.Sunday;
            default -> throw new IllegalArgumentException("Unexpected value: " + dayType);
        };
    }

    private Room.RoomType convertStringToRoomType(String roomType) {
        if (roomType == null) {
            throw new IllegalArgumentException("Room type cannot be null.");
        }

        return switch (roomType) {
            case "Small" -> Room.RoomType.Small;
            case "Large" -> Room.RoomType.Large;
            case "Storage" -> Room.RoomType.Storage;
            default -> throw new IllegalArgumentException("Unexpected value: " + roomType);
        };
    }
}
