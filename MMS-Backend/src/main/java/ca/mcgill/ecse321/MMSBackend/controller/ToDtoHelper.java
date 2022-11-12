package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;
import ca.mcgill.ecse321.MMSBackend.dto.*;
import ca.mcgill.ecse321.MMSBackend.dto.SpecificWeekDayDto.DayTypeDto;

public class ToDtoHelper {

    /**
     * @author Lucy Zhang (Lucy-Zh)
     * @param mms
     * @return
     */
    public static MuseumManagementSystemDto convertToDto(MuseumManagementSystem mms) {
        if (mms == null) {
            throw new IllegalArgumentException("There is no such Museum Management System!");
        }
        return new MuseumManagementSystemDto(mms.getSystemId(), mms.getName(), mms.getOpenTime(), mms.getCloseTime(), mms.getMaxLoanNumber(), mms.getTicketFee());
    }

    /**
     * @author Mona Kalaoun (m-kln)
     * @param r
     * @return
     */
    public static RoomDto convertToDto(Room r) {
        if (r == null) {
            throw new IllegalArgumentException("There is no such Room!");
        }
        MuseumManagementSystemDto systemDto = convertToDto(r.getMuseumManagementSystem());
        RoomDto room = new RoomDto(r.getRoomId(), r.getName(), r.getType(), systemDto);
        return room;
    }

    /**
     * @author Mona Kalaoun (m-kln)
     * @param a
     * @return
     */
    public static ArtifactDto convertToDto(Artifact a) {
        if (a == null) {
            throw new IllegalArgumentException("There is no such Artifact!");
        }
        RoomDto roomDto = convertToDto(a.getRoomLocation());
        MuseumManagementSystemDto mmsDto = convertToDto(a.getMuseumManagementSystem());
        //hi
        ArtifactDto artifactDto = new ArtifactDto(a.getArtifactId(), a.getName(), a.getImage(), a.getDescription(), a.getLoanStatus(),
                a.getIsDamaged(), a.getLoanFee(), a.getWorth(), roomDto, mmsDto);

        return artifactDto;
    }

    /**
     * @author Nikolas Pasichnik
     * @param client
     * @return
     */
    public static ClientDto convertToDto(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("There is no such Client!");
        }

        ClientDto clientDto = new ClientDto(client.getUsername(), client.getName(), client.getPassword(),
                client.getCurrentLoanNumber(), client.getMuseumManagementSystem());

        return clientDto;
    }

    /**
     * @author Nikolas Pasichnik
     * @param manager
     * @return
     */
    public static ManagerDto convertToDto(Manager manager) {

        if (manager == null) {
            throw new IllegalArgumentException("There is no such Manager!");
        }

        ManagerDto managerDto = new ManagerDto(manager.getUsername(), manager.getName(),
        manager.getPassword(), manager.getMuseumManagementSystem());

        return managerDto;
    }

    /**
     * @author Nikolas Pasichnik 
     * @param employee
     * @return
     */
    public static EmployeeDto convertToDto(Employee employee) {

        if (employee == null) {
            throw new IllegalArgumentException("There is no such Employee!");
        }

        EmployeeDto employeeDto = new EmployeeDto(employee.getUsername(), employee.getName(), 
        employee.getPassword(), employee.getMuseumManagementSystem());

        return employeeDto;
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     * @param dayType
     * @return
     */
    public static DayTypeDto convertToDto(DayType dayType) {
        if (dayType == null) {
            throw new IllegalArgumentException("DayType cannot be null!");
        }
        switch (dayType) {
            case Monday:
                return DayTypeDto.Monday;
            case Tuesday:
                return DayTypeDto.Tuesday;
            case Wednesday:
                return DayTypeDto.Wednesday;
            case Thursday:
                return DayTypeDto.Thursday;
            case Friday:
                return DayTypeDto.Friday;
            case Saturday:
                return DayTypeDto.Saturday;
            case Sunday:
                return DayTypeDto.Sunday;
            default:
                throw new IllegalArgumentException("Unexpected value: " + dayType);
        }
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     * @param dayTypeDto
     * @return
     */
    public static DayType convertToDomainObject(DayTypeDto dayTypeDto) {
        if (dayTypeDto == null) {
            throw new IllegalArgumentException("DayType cannot be null!");
        }
        switch (dayTypeDto) {
            case Monday:
                return DayType.Monday;
            case Tuesday:
                return DayType.Tuesday;
            case Wednesday:
                return DayType.Wednesday;
            case Thursday:
                return DayType.Thursday;
            case Friday:
                return DayType.Friday;
            case Saturday:
                return DayType.Saturday;
            case Sunday:
                return DayType.Sunday;
            default:
                throw new IllegalArgumentException("Unexpected value: " + dayTypeDto);
        }
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     * @param specificWeekDay
     * @return
     */
    public static SpecificWeekDayDto convertToDto(SpecificWeekDay specificWeekDay) {
        if (specificWeekDay == null) {
            throw new IllegalArgumentException("SpecificWeekDay cannot be null!");
        }
        return new SpecificWeekDayDto(convertToDto(specificWeekDay.getDayType()), specificWeekDay.getIsClosed(),
                convertToDto(specificWeekDay.getMuseumManagementSystem()));
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     * @param shift
     * @return
     */
    public static ShiftDto convertToDto(Shift shift) {
        if (shift == null) {
            throw new IllegalArgumentException("Shift cannot be null!");
        }
        return new ShiftDto(shift.getShiftId(), shift.getStartTime(), shift.getEndTime(),
                convertToDto(shift.getDayOfTheWeek()), convertToDto(shift.getMuseumManagementSystem()),
                convertToDto(shift.getEmployee()));

    }

    /**
     * @author Lucy Zhang (Lucy-Zh)
     * @param ticket
     * @return
     */
    public static TicketDto convertToDto(Ticket ticket)
    {
        if(ticket == null)
        {
            throw new IllegalArgumentException("There is no such Ticket!");
        }
        return new TicketDto(ticket.getTicketId(), ticket.getFee(), ticket.getIsActive(), convertToDto(ticket.getMuseumManagementSystem()), convertToDto(ticket.getClient()));
    }

    /**
     * @author Nazia Chowdhury (naziaC)
     * @param loanRequest
     * @return a LoanRequestDto
     */
    public static LoanRequestDto convertToDto(LoanRequest loanRequest) {
        if (loanRequest == null) {
            throw new IllegalArgumentException("Loan request cannot be null!");
        }
        return new LoanRequestDto(loanRequest.getRequestId(), loanRequest.getLoanDuration(), loanRequest.getFee(),
                loanRequest.getStatus(), convertToDto(loanRequest.getMuseumManagementSystem()));

    }

    /**
     * @author Yu An Lu (yu-an-lu)
     * @param donationRequest
     * @return a DonationRequestDto
     */
    public static DonationRequestDto convertToDto(DonationRequest donationRequest) {
        if (donationRequest == null) {
            throw new IllegalArgumentException("Donation request cannot be null!");
        }

        DonationRequestDto donationRequestDto = new DonationRequestDto(donationRequest.getRequestId(),
                convertToDto(donationRequest.getClient()), 
                convertToDto(donationRequest.getArtifact()),
                donationRequest.getStatus(),
                convertToDto(donationRequest.getMuseumManagementSystem()));

        return donationRequestDto;
    }

}