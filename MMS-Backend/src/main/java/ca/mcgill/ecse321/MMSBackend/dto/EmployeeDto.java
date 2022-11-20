package ca.mcgill.ecse321.MMSBackend.dto;

/**
 * DTO Employee class
 * 
 * @author Nikolas Pasichnik (NikolasPasichnik)
 */
public class EmployeeDto {

    //Account Attributes
    private String username;
    private String name;
    private String password;
    private MuseumManagementSystemDto MMS; 
    
    /**
     * Empty Constructor 
     */
    public EmployeeDto(){

    }

    /**
     * Constructor of a EmployeeDto
     * 
     * @param username
     * @param name
     * @param password
     * @param MMS
     */
    public EmployeeDto(String username, String name, String password, MuseumManagementSystemDto MMS){
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
     * MuseumManagementSystem getter
     */
    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return MMS; 
    }

}

