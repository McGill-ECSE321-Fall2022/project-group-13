package ca.mcgill.ecse321.MMSBackend.dto;

/**
 * DTO Client class
 * 
 * @Author: Nikolas Pasichnik (NikolasPasichnik)
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
     * 
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Name getter
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Password getter
     * 
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * CurrentLoanNumber getter
     * 
     * @return
     */
    public int getCurrentLoanNumber() {
        return currentLoanNumber;
    }

    /**
     * MuseumManagementSystem getter
     * 
     * @return
     */
    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return MMS;
    }

}
