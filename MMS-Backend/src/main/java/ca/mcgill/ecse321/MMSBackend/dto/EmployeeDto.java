package ca.mcgill.ecse321.MMSBackend.dto;

import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

public class EmployeeDto {

    //Account Attributes
    private String username;
    private String name;
    private String password;
    private MuseumManagementSystem MMS; 
    

    // /**
    //  * Empty Constructor 
    //  */
    // public EmployeeDto(){

    // }

    /**
     * Constructor of a EmployeeDto
     * 
     * @param username
     * @param name
     * @param password
     * @param currentLoanNumber
     */
    public EmployeeDto(String username, String name, String password, MuseumManagementSystem MMS){
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
    public MuseumManagementSystem getMuseumManagementSystem() {
        return MMS; 
    }

    // /**
    //  * Setter for the name
    //  * 
    //  * @param employeeName
    //  */
    // public void setName(String employeeName) {
    //     this.name = employeeName; 
    // }

    // /**
    //  * Setter for the password 
    //  * 
    //  * @param employeePassword
    //  */
    // public void setPassword(String employeePassword) {
    //     this.password = employeePassword; 
    // }
}

