package ca.mcgill.ecse321.MMSBackend.dto;

import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

public class ManagerDto {

    //Account Attributes
    private String username;
    private String name;
    private String password;
    private MuseumManagementSystem MMS; 
    
    // /**
    //  * Empty constructor 
    //  */
    // public ManagerDto(){

    // }

    /**
     * Constructor of a EmployeeDto
     * 
     * @param username
     * @param name
     * @param password
     * @param currentLoanNumber
     */
    public ManagerDto(String username, String name, String password, MuseumManagementSystem MMS){
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
    public MuseumManagementSystem getMuseumManagementSystem() {
        return MMS; 
    }
}
