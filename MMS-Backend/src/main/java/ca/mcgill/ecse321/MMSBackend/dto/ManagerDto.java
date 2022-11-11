package ca.mcgill.ecse321.MMSBackend.dto;

public class ManagerDto {

    //Account Attributes
    private String username;
    private String name;
    private String password;
    
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
    public ManagerDto(String username, String name, String password){
        this.username = username; 
        this.name = name; 
        this.password = password; 
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
}
