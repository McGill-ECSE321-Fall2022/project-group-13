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
     * Get a mms by id
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "/mms/getMms", "/mms/getMms/" })
    public ResponseEntity<MuseumManagementSystemDto> getMms() throws IllegalArgumentException {
        return new ResponseEntity<>(convertToDto(mmsService.getMuseumManagementSystem()), HttpStatus.OK);
    }

    /**
     * Update mms name by id
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @param name
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/mms", "/mms/" })
    public ResponseEntity<MuseumManagementSystemDto> updateMmsName(@RequestParam String name)
            throws IllegalArgumentException {
        mmsService.setMuseumManagementSystemName(name);
        return new ResponseEntity<>(convertToDto(mmsService.getMuseumManagementSystem()), HttpStatus.OK);
    }

    /**
     * Set mms ticket fee
     * 
     * @author Lucy Zhang (Lucy-Zh)
     * @param ticketFee
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "/mms/fee", "/mms/fee/" })
    public ResponseEntity<MuseumManagementSystemDto> updateMmsTicketFee(
            @RequestParam double ticketFee) throws IllegalArgumentException {

        mmsService.setMuseumTicketPrice(ticketFee);
        return new ResponseEntity<>(convertToDto(mmsService.getMuseumManagementSystem()), HttpStatus.OK);

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
     */
    @GetMapping(value = { "/mms/maxLoanNumber", "/mms/maxLoanNumber/" })
    public ResponseEntity<Integer> getMaxLoanNumberOfSystem() throws IllegalArgumentException {
        return new ResponseEntity<Integer>(mmsService.getMaxLoanNumberOfMms(), HttpStatus.OK);
    }

    /**
     * Set museum management system's max loan number for clients
     *
     * @author : Nazia Chowdhury (naziaC)
     */
    @PutMapping(value = { "/mms/setMaxLoanNumber", "/mms/setMaxLoanNumber/" })
    public ResponseEntity<MuseumManagementSystemDto> setMaxLoanNumberOfSystem(@RequestParam int maxLoanNumber)
            throws IllegalArgumentException {
        mmsService.setMaxLoanNumberOfMms(maxLoanNumber);
        return new ResponseEntity<MuseumManagementSystemDto>(convertToDto(mmsService.getMuseumManagementSystem()), HttpStatus.OK);
    }

    /**
     * Set museum management system's opening hours
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @param openTime
     * @param closeTime
     * @throws IllegalArgumentException
     */
    @PutMapping(value = { "mms/setOpeningHours", "mms/setOpeningHours/" })
    public ResponseEntity<MuseumManagementSystemDto> setOpeningHours(
            @RequestParam Time openTime, @RequestParam Time closeTime) throws IllegalArgumentException {
        mmsService.setOpeningHours(openTime, closeTime);
        return new ResponseEntity<MuseumManagementSystemDto>(convertToDto(mmsService.getMuseumManagementSystem()), HttpStatus.OK);
    }

    /**
     * Get museum management system's opening hours
     * 
     * @author : Samantha Perez Hoffman (samperezh)
     * @throws IllegalArgumentException
     */
    @GetMapping(value = { "mms/getOpeningHours", "mms/getOpeningHours/" })
    public ResponseEntity<List<Time>> getOpeningHours() throws IllegalArgumentException {
        return new ResponseEntity<>(mmsService.getOpeningHours(), HttpStatus.OK);
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
