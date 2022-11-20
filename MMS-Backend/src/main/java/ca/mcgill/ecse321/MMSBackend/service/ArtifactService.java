package ca.mcgill.ecse321.MMSBackend.service;

import ca.mcgill.ecse321.MMSBackend.dao.*;
import ca.mcgill.ecse321.MMSBackend.exception.MuseumManagementSystemException;
import ca.mcgill.ecse321.MMSBackend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mona Kalaoun (m-kln)
 *
 * The ArtifactService class implements the use case based on the requirement:
 *
 * "FREQ06: The museum management system shall allow the manager and the employees to add or
 * update an artifact by recording the artifact’s name, picture, description, location, loan availability,
 * and loan fee, which can be viewed by all users."
 *
 */
@Service
public class ArtifactService {

    @Autowired
    ArtifactRepository artifactRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    MuseumManagementSystemRepository mmsRepository;

    /**
     * Get a single artifact by its unique id
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @param id - an int representing the unique id of the artifact
     * @return artifact object
     */
    @Transactional
    public Artifact getArtifact(int id){
        Artifact artifact = artifactRepository.findArtifactByArtifactId(id);
        if (artifact == null){
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Artifact with id: " + id +
                    " was not found.");
        }
        return artifact;
    }

    /**
     * Get all artifacts (view all artifacts)
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @return a list of all artifact objects in the system
     */
    @Transactional
    public List<Artifact> getAllArtifacts() {
        return toList(artifactRepository.findAll());
    }

    /**
     * Get all artifacts are in the same kind of room
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @param r - room type from RoomType enum in Room class
     * @return a list of all artifact objects having the same room type
     */
    @Transactional
    public List<Artifact> getAllArtifactsByRoomType(Room.RoomType r) {
        List<Artifact> all = toList(artifactRepository.findAll());
        List<Artifact> result = new ArrayList<>();

        for(Artifact a : all){
            if(a.getRoomLocation() != null && a.getRoomLocation().getType().equals(r)){
                result.add(a);
            }
        }

        return result;
    }

    /**
     * Get all artifacts that are located in the same specific room
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @param roomId - an int representing the unique id of the room
     * @return a list of all artifact objects that are in the same room
     */
    @Transactional
    public List<Artifact> getAllArtifactsByRoomId(int roomId) {
        List<Artifact> all = toList(artifactRepository.findAll());
        List<Artifact> result = new ArrayList<>();

        for(Artifact a : all){
            if(a.getRoomLocation() != null && a.getRoomLocation().getRoomId() == roomId){
                result.add(a);
            }
        }

        return result;
    }

    /**
     * Get all artifacts that share the same loan status
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @param status - loan status from LoanStatus enum in Artifact class
     * @return a list of all artifact objects that have the same loan status
     */
    @Transactional
    public List<Artifact> getAllArtifactsByLoanStatus(Artifact.LoanStatus status) {
        List<Artifact> all = toList(artifactRepository.findAll());
        List<Artifact> result = new ArrayList<>();

        for(Artifact a : all){
            if(a.getLoanStatus().equals(status)){
                result.add(a);
            }
        }

        return result;
    }

    /**
     * Get all artifacts that are in the same state (damaged or not)
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @param state - a boolean indicating if the artifact is damaged (true) or not (false)
     * @return a list of all artifact objects that have the same state
     */
    @Transactional
    public List<Artifact> getAllArtifactsByState(boolean state) {
        List<Artifact> all = toList(artifactRepository.findAll());
        List<Artifact> result = new ArrayList<>();

        for(Artifact a : all){
            if(a.getIsDamaged() == state){
                result.add(a);
            }
        }

        return result;
    }

    /**
     * Edit an existing artifact's information
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @param artifactId - int representing unique id of the artifact to be edited
     * @param name - String containing the name of the artifact
     * @param description - String containing the description of the artifact
     * @param image - String containing the file path of the artifact's image
     * @param status - LoanStatus enum type indicating the loan status of the artifact
     * @param loanFee - int representing the fee for the loan
     * @param isDamaged - boolean indicating if the artifact is damaged or not
     * @param worth - int representing the monetary value of the artifact
     * @param roomId - int representing the unique id of the room location of the artifact
     * @param systemId - int representing the unique id of the current system
     *
     * @return artifact object once edited
     */
    @Transactional
    public Artifact editArtifact(int artifactId, String name, String description, String image, Artifact.LoanStatus
            status, double loanFee, boolean isDamaged, double worth, int roomId, int systemId){

        checkInput(name,description,image,status,loanFee,isDamaged,worth,roomId,systemId);

        if (artifactId < 0) throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "Artifact id can not " +
                "be negative.");

        Artifact artifact = artifactRepository.findArtifactByArtifactId(artifactId);

        if(artifact != null){
            artifact.setName(name);
            artifact.setDescription(description);
            artifact.setImage(image);
            artifact.setLoanStatus(status);
            artifact.setIsDamaged(isDamaged);
            artifact.setLoanFee(loanFee);
            artifact.setWorth(worth);
            artifact.setRoomLocation(roomRepository.findRoomByRoomId(roomId));
            artifact.setMuseumManagementSystem(MuseumManagementSystem.getWithSystemId(systemId));
        }else{
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Artifact with id: " + artifactId +
                    " was not found.");
        }

        return artifact;
    }

    /**
     * Create an artifact and add it to the system
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @param name - String containing the name of the artifact
     * @param description - String containing the description of the artifact
     * @param image - String containing the file path of the artifact's image
     * @param status - LoanStatus enum type indicating the loan status of the artifact
     * @param loanFee - int representing the fee for the loan
     * @param isDamaged - boolean indicating if the artifact is damaged or not
     * @param worth - int representing the monetary value of the artifact
     * @param roomId - int representing the unique id of the room location of the artifact
     * @param systemId - int representing the unique id of the current system
     *
     * @return a new artifact object
     */
    @Transactional
    public Artifact createArtifact(String name, String description, String image, Artifact.LoanStatus status,
                                   double loanFee, boolean isDamaged, double worth, int roomId, int systemId){

        checkInput(name, description, image, status, loanFee, isDamaged, worth, roomId, systemId);

        Artifact artifact = new Artifact();
        artifact.setName(name);
        artifact.setDescription(description);
        artifact.setImage(image);
        artifact.setLoanStatus(status);
        artifact.setIsDamaged(isDamaged);
        artifact.setLoanFee(loanFee);
        artifact.setWorth(worth);
        artifact.setRoomLocation(roomRepository.findRoomByRoomId(roomId));
        artifact.setMuseumManagementSystem(mmsRepository.findMuseumManagementSystemBySystemId(systemId));
        artifact = artifactRepository.save(artifact);
        return artifact;
    }

    /**
     * Delete an artifact from the system
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @param artifactId : int representing the unique id of an existing artifact that is to be deleted
     */
    @Transactional
    public boolean deleteArtifact(int artifactId){
        boolean result = false;
        //check if artifact exists before deleting
        if(artifactRepository.findArtifactByArtifactId(artifactId) != null) {
            artifactRepository.deleteById(artifactId);
            result = true;
        }else {
            throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "Artifact with id: " + artifactId +
                    " was not found.");
        }
        return result;
    }


    /**
     * Checks if a specific room is full
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @param roomId : int representing the unique id of the room to be checked
     * @param systemId : int representing the unique id of the system where the room is located
     */

    public void checkRoom(int roomId, int systemId){
        //small , 5 with 200 art
        //large, 5 with 300 art
        MuseumManagementSystem mms = mmsRepository.findMuseumManagementSystemBySystemId(systemId);
        if(mms == null) throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "The museum with id: " +
                systemId + " was not found.");

        Room r = roomRepository.findRoomByRoomId(roomId);
        if(r == null) throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND, "The room with id: " +
                roomId + " was not found.");

        int count = 0;

        if(mms != null) {
            for (Artifact a : mms.getArtifacts()) {
                if (a.getRoomLocation() != null && a.getRoomLocation().getRoomId() == roomId) {
                    count++;
                }
            }

            if (r.getType().equals(Room.RoomType.Small)) {
                if (count == 1000) {
                    throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "The selected small room with id: "
                            + roomId + " is full.");
                }
            } else if (r.getType().equals(Room.RoomType.Large)) {
                if (count == 1500) {
                    throw new MuseumManagementSystemException(HttpStatus.CONFLICT, "The selected large room with id: "
                            + roomId + " is full.");
                }
            }
        }
    }


    /**
     * Helper method to check the validity of inputs and throw according exceptions
     *
     * @author Mona Kalaoun (m-kln)
     *
     * @param name - String containing the name of the artifact
     * @param description - String containing the description of the artifact
     * @param image - String containing the file path of the artifact's image
     * @param status - LoanStatus enum type indicating the loan status of the artifact
     * @param loanFee - int representing the fee for the loan
     * @param isDamaged - boolean indicating if the artifact is damaged or not
     * @param worth - int representing the monetary value of the artifact
     * @param roomId - int representing the unique id of the room location of the artifact
     * @param systemId - int representing the unique id of the current system
     */
    private void checkInput(String name, String description, String image, Artifact.LoanStatus status,
                            double loanFee, boolean isDamaged, double worth, int roomId, int systemId){

        if( name == null|| name.trim().length() == 0 || description == null || description.trim().length() == 0
                || image == null || image.trim().length() == 0 || status == null){
            throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "No null values are accepted.");
        }

        File img = new File(image);
        String absolute = img.getAbsolutePath();
        File newImage = new File(absolute);

        if(!newImage.exists()) throw new MuseumManagementSystemException(HttpStatus.NOT_FOUND,"Image file does not exist.");

        if(loanFee<0) throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "The loan fee can not be " +
                "a negative value.");

        if(worth<0) throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "The monetary value of the " +
                "artifact can not be negative.");

        if(roomId<0) throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "The room id can not be " +
                "negative.");

        if(systemId<0) throw new MuseumManagementSystemException(HttpStatus.BAD_REQUEST, "The system id can not be " +
                "negative.");

        checkRoom(roomId,systemId);
    }

    /**
     * toList method taken from section "2.5.1. Implementing Service Methods" of GitHub tutorial repo
     */
    private <T> List<T> toList(Iterable<T> iterable){
        List<T> resultList = new ArrayList<T>();
        for (T t : iterable) {
            resultList.add(t);
        }
        return resultList;
    }



}
