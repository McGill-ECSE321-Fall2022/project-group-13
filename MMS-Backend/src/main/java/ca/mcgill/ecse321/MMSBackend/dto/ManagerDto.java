package ca.mcgill.ecse321.MMSBackend.dto;

/**
 * DTO Manager class
 * 
 * @author Nikolas Pasichnik (NikolasPasichnik)
 */
public class ManagerDto {

    // Account Attributes
    private String username;
    private String name;
    private String password;
    private MuseumManagementSystemDto MMS;

    /**
     * Empty constructor
     */
    public ManagerDto() {

    }

    /**
     * Constructor of a EmployeeDto
     * 
     * @param username
     * @param name
     * @param password
     * @param currentLoanNumber
     */
    public ManagerDto(String username, String name, String password, MuseumManagementSystemDto MMS) {
        this.username = username;
        this.name = name;
        this.password = password;
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
     * Setter for the name
     * 
     * @param managerName
     */
    public void setName(String managerName) {
        this.name = managerName;
    }

    /**
     * Setter for the password
     * 
     * @param managerPassword
     */
    public void setPassword(String managerPassword) {
        this.password = managerPassword;
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
