package ca.mcgill.ecse321.MMSBackend.dto;

import ca.mcgill.ecse321.MMSBackend.model.MuseumManagementSystem;

public class ClientDto {

    //Account Attributes
    private String username;
    private String name;
    private String password;
    private int currentLoanNumber;
    private MuseumManagementSystem MMS; 


    // /**
    //  * Empty Constructor 
    //  */
    // public ClientDto(){

    // }

    /**
     * Constructor of a ClientDto
     * 
     * @param username
     * @param name
     * @param password
     * @param currentLoanNumber
     */
    public ClientDto(String username, String name, String password, int currentLoanNumber, MuseumManagementSystem MMS){
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
    public MuseumManagementSystem getMuseumManagementSystem() {
        return MMS; 
    }


    // /**
    //  * Setter for the loan number 
    //  * 
    //  * @param loanNumber
    //  */
    // public void setCurrentLoanNumber(int loanNumber){
    //     this.currentLoanNumber = loanNumber; 
    // }

    // /**
    //  * Setter for the name
    //  * 
    //  * @param clientName
    //  */
    // public void setName(String clientName) {
    //     this.name = clientName; 
    // }

    // /**
    //  * Setter for the password 
    //  * 
    //  * @param clientPassword
    //  */
    // public void setPassword(String clientPassword) {
    //     this.password = clientPassword; 
    // }

}
