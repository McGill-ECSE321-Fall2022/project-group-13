package ca.mcgill.ecse321.MMSBackend.controller;

import ca.mcgill.ecse321.MMSBackend.model.*;
import ca.mcgill.ecse321.MMSBackend.model.DonationRequest.DonationStatus;
import ca.mcgill.ecse321.MMSBackend.model.SpecificWeekDay.DayType;
import ca.mcgill.ecse321.MMSBackend.dto.*;
import ca.mcgill.ecse321.MMSBackend.dto.DonationRequestDto.DonationStatusDto;
import ca.mcgill.ecse321.MMSBackend.dto.SpecificWeekDayDto.DayTypeDto;

public class ToDtoHelper {

    /**
     * @author Lucy Zhang (Lucy-Zh)
     * @param mms
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
     */
    public static RoomDto convertToDto(Room r) {
        if (r == null) {
            return null;
        }
        MuseumManagementSystemDto systemDto = convertToDto(r.getMuseumManagementSystem());
        RoomDto.RoomTypeDto typeDto = convertToDto(r.getType());
        RoomDto room = new RoomDto(r.getRoomId(), r.getName(), typeDto, systemDto);
        return room;
    }

    /**
     * @author Mona Kalaoun (m-kln)
     * @param type
     * @return a LoanStatusDto
     */
    public static RoomDto.RoomTypeDto convertToDto(Room.RoomType type) {
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

    /**
     * @author Yu An Lu (yu-an-lu)
     * @param roomType
     * @return a RoomTypeDto
     */
    public static Room.RoomType convertStringToRoomType(String roomType) {
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

    /**
     * @author Mona Kalaoun (m-kln)
     * @param a
     */
    public static ArtifactDto convertToDto(Artifact a) {
        if (a == null) {
            throw new IllegalArgumentException("There is no such Artifact!");
        }
        RoomDto roomDto = convertToDto(a.getRoomLocation());
        MuseumManagementSystemDto mmsDto = convertToDto(a.getMuseumManagementSystem());
        ArtifactDto.LoanStatusDto statusDto = convertToDto(a.getLoanStatus());

        ArtifactDto artifactDto = new ArtifactDto(a.getArtifactId(), a.getName(), a.getImage(), a.getDescription(),
                statusDto, a.getIsDamaged(), a.getLoanFee(), a.getWorth(), roomDto, mmsDto);

        return artifactDto;
    }

    /**
     * @author Mona Kalaoun (m-kln)
     * @param status
     * @return a LoanStatusDto
     */
    public static ArtifactDto.LoanStatusDto convertToDto(Artifact.LoanStatus status) {
        if (status == null) {
            return null;
        }

        return switch (status) {
            case Available -> ArtifactDto.LoanStatusDto.Available;
            case Unavailable -> ArtifactDto.LoanStatusDto.Unavailable;
            case Loaned -> ArtifactDto.LoanStatusDto.Loaned;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    /**
     * @author Nikolas Pasichnik (NikolasPasichnik)
     * @param client
     */
    public static ClientDto convertToDto(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("There is no such Client!");
        }
        MuseumManagementSystemDto mmsDto = convertToDto(client.getMuseumManagementSystem());

        ClientDto clientDto = new ClientDto(client.getUsername(), client.getName(), client.getPassword(),
                client.getCurrentLoanNumber(), mmsDto);

        return clientDto;
    }

    /**
     * @author Nikolas Pasichnik (NikolasPasichnik)
     * @param manager
     */
    public static ManagerDto convertToDto(Manager manager) {

        if (manager == null) {
            throw new IllegalArgumentException("There is no such Manager!");
        }
        MuseumManagementSystemDto mmsDto = convertToDto(manager.getMuseumManagementSystem());

        ManagerDto managerDto = new ManagerDto(manager.getUsername(), manager.getName(),
        manager.getPassword(), mmsDto);

        return managerDto;
    }

    /**
     * @author Nikolas Pasichnik (NikolasPasichnik)
     * @param employee
     */
    public static EmployeeDto convertToDto(Employee employee) {

        if (employee == null) {
            throw new IllegalArgumentException("There is no such Employee!");
        }
        MuseumManagementSystemDto mmsDto = convertToDto(employee.getMuseumManagementSystem());

        EmployeeDto employeeDto = new EmployeeDto(employee.getUsername(), employee.getName(),
        employee.getPassword(), mmsDto);

        return employeeDto;
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     * @param dayType
     */
    public static DayTypeDto convertToDto(DayType dayType) {
        if (dayType == null) {
            throw new IllegalArgumentException("DayType cannot be null!");
        }
        return switch (dayType) {
            case Monday -> DayTypeDto.Monday;
            case Tuesday -> DayTypeDto.Tuesday;
            case Wednesday -> DayTypeDto.Wednesday;
            case Thursday -> DayTypeDto.Thursday;
            case Friday -> DayTypeDto.Friday;
            case Saturday -> DayTypeDto.Saturday;
            case Sunday -> DayTypeDto.Sunday;
            default -> throw new IllegalArgumentException("Unexpected value: " + dayType);
        };
    }

    /**
     * @author Samantha Perez Hoffman (samperezh)
     * @param day
     */
    public static DayType convertStringToDayType(String day){
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

    /**
     * @author Samantha Perez Hoffman (samperezh)
     * @param specificWeekDay
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
        return new LoanRequestDto(loanRequest.getRequestId(),
                loanRequest.getLoanDuration(),
                loanRequest.getFee(),
                convertToDto(loanRequest.getClient()),
                convertToDto(loanRequest.getArtifact()),
                convertToDto(loanRequest.getStatus()),
                convertToDto(loanRequest.getMuseumManagementSystem()));
    }

    /**
     * @author Nazia Chowdhury (naziaC)
     * @param status
     * @return a LoanRequestStatusDto
     */
    public static LoanRequestDto.LoanRequestStatusDto convertToDto(LoanRequest.LoanStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        System.out.println(status);

        return switch (status) {
            case Approved -> LoanRequestDto.LoanRequestStatusDto.Approved;
            case Rejected -> LoanRequestDto.LoanRequestStatusDto.Rejected;
            case Pending -> LoanRequestDto.LoanRequestStatusDto.Pending;
            case Returned -> LoanRequestDto.LoanRequestStatusDto.Returned;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    /**
     * @author Nazia Chowdhury (naziaC)
     * @param status
     * @return a LoanRequest.LoanStatus
     */
    public static LoanRequest.LoanStatus convertStringToLoanStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        return switch (status) {
            case "Approved" -> LoanRequest.LoanStatus.Approved;
            case "Rejected" -> LoanRequest.LoanStatus.Rejected;
            case "Pending" -> LoanRequest.LoanStatus.Pending;
            case "Returned" -> LoanRequest.LoanStatus.Returned;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    /**
     * @author Nazia Chowdhury (naziaC), Lucy Zhang (Lucy-Zh), Mona Kalaoun (m-kln), Samantha Perez Hoffman (samperezh), Nikolas Pasichnik (NikolasPasichnik), and Yu An Lu (yu-an-lu)
     * @param status
     * @return a Artifact.LoanStatus
     */
    public static Artifact.LoanStatus convertArtifactStringToLoanStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        return switch (status) {
            case "Available" -> Artifact.LoanStatus.Available;
            case "Unavailable" -> Artifact.LoanStatus.Unavailable;
            case "Loaned" -> Artifact.LoanStatus.Loaned;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    /**
     * @author Yu An Lu (yu-an-lu) 
     * @param status
     * @return a DonationStatusDto
     */
    public static DonationStatusDto convertToDto(DonationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("DonationRequestStatus cannot be null!");
        }

        return switch (status) {
            case Approved -> DonationStatusDto.Approved;
            case Rejected -> DonationStatusDto.Rejected;
            case Pending -> DonationStatusDto.Pending;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
    }

    /**
     * @author Yu An Lu (yu-an-lu)
     * @param status
     * @return a DonationRequest.DonationStatus
     */
    public static DonationRequest.DonationStatus convertStringToDonationStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        return switch (status) {
            case "Pending" -> DonationRequest.DonationStatus.Pending;
            case "Approved" -> DonationRequest.DonationStatus.Approved;
            case "Rejected" -> DonationRequest.DonationStatus.Rejected;
            default -> throw new IllegalArgumentException("Unexpected value: " + status);
        };
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
                convertToDto(donationRequest.getStatus()),
                convertToDto(donationRequest.getMuseumManagementSystem()));

        return donationRequestDto;
    }

}
