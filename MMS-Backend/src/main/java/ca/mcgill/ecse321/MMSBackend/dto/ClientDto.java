package ca.mcgill.ecse321.MMSBackend.dto;

/**
 * DTO Client class
 * 
 * @author Nikolas Pasichnik (NikolasPasichnik)
 */
public class ClientDto {

    // Account Attributes
    private String username;
    private String name;
    private String password;
    private int currentLoanNumber;
    private MuseumManagementSystemDto MMS;

    /**
     * Empty Constructor
     */
    public ClientDto() {

    }

    /**
     * Constructor of a ClientDto
     * 
     * @param username
     * @param name
     * @param password
     * @param currentLoanNumber
     */
    public ClientDto(String username, String name, String password, int currentLoanNumber,
            MuseumManagementSystemDto MMS) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.currentLoanNumber = currentLoanNumber;
        this.MMS = MMS;
    }

    /**
     * Username getter
     */
    public String getUsername() {
        return username;
    }

    /**
     * Name getter
     */
    public String getName() {
        return name;
    }

    /**
     * Password getter
     */
    public String getPassword() {
        return password;
    }

    /**
     * CurrentLoanNumber getter
     */
    public int getCurrentLoanNumber() {
        return currentLoanNumber;
    }

    /**
     * MuseumManagementSystem getter
     */
    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return MMS;
    }

}
