package ca.mcgill.ecse321.MMSBackend.dto;

public class ClientDto {

    //Account Attributes
    private String username;
    private String name;
    private String password;
    private int currentLoanNumber;

    /**
     * Constructor of a ClientDto
     * 
     * @param username
     * @param name
     * @param password
     * @param currentLoanNumber
     */
    public ClientDto(String username, String name, String password, int currentLoanNumber){
        this.username = username; 
        this.name = name; 
        this.password = password; 
        this.currentLoanNumber = currentLoanNumber; 
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

}
