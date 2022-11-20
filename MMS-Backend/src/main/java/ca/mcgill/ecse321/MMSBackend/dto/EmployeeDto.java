package ca.mcgill.ecse321.MMSBackend.dto;

/**
 * DTO Employee class
 * 
 * @Author Nikolas Pasichnik (NikolasPasichnik)
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
     * @param currentLoanNumber
     */
    public EmployeeDto(String username, String name, String password, MuseumManagementSystemDto MMS){
        this.username = username; 
        this.name = name; 
        this.password = password; 
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
     * MuseumManagementSystem getter
     * 
     * @return
     */
    public MuseumManagementSystemDto getMuseumManagementSystem() {
        return MMS; 
    }

}

