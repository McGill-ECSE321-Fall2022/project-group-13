package ca.mcgill.ecse321.MMSBackend.dto;

public class EmployeeDto {

    //Account Attributes
    private String username;
    private String name;
    private String password;
    
    /**
     * Constructor of a EmployeeDto
     * 
     * @param username
     * @param name
     * @param password
     * @param currentLoanNumber
     */
    public EmployeeDto(String username, String name, String password){
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
}
