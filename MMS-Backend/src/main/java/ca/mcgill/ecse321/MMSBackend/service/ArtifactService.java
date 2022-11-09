package ca.mcgill.ecse321.MMSBackend.service;

import ca.mcgill.ecse321.MMSBackend.dao.*;
import ca.mcgill.ecse321.MMSBackend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mona Kalaoun (m-kln)
 *
 * The ArtifactService class implements the use case based on the requirement:
 *
 * "The museum management system shall allow the manager and the employees to add or
 * update an artifact by recording the artifact’s name, picture, description, location, loan availability,
 * and loan fee, which can be viewed by all users."
 *
 */
@Service
public class ArtifactService {

    @Autowired
    ArtifactRepository artifactRepository;

    /**
     * Get a single artifact by its id
     * @param id
     * @return artifact object
     */
    @Transactional
    public Artifact getArtifact(Integer id){
        Artifact artifact = artifactRepository.findArtifactByArtifactId(id);
        return artifact;
    }

    /**
     * Get all artifacts (view all artifacts)
     * @return a list of artifact objects
     */
    @Transactional
    public List<Artifact> getAllArtifacts() {
        return toList(artifactRepository.findAll());
    }

    /**
     * Edit an existing artifact's information
     *
     * @param artifactId
     * @param name
     * @param description
     * @param status
     * @param loanFee
     * @param isDamaged
     * @param worth
     * @param newRoom
     * @return artifact object once edited
     */
    @Transactional
    public Artifact editArtifact(Integer artifactId, String name, String description, Artifact.LoanStatus status,
                                 double loanFee, boolean isDamaged, double worth, Room newRoom){

        Artifact artifact = artifactRepository.findArtifactByArtifactId(artifactId);
        //or Artifact artifact = getArtifact(artifactId);

        if(artifact != null){
            artifact.setName(name);
            artifact.setDescription(description);
            artifact.setLoanStatus(status);
            artifact.setIsDamaged(isDamaged);
            artifact.setLoanFee(loanFee);
            artifact.setWorth(worth);
            artifact.setRoomLocation(newRoom);
        }

        return artifact;
    }

    /**
     * Create an artifact and add it to the system
     * @param artifact
     * @return newly created artifact object
     */
    @Transactional
    public Artifact addArtifact(Artifact artifact){
        artifact = artifactRepository.save(artifact);
        return artifact;
    }

    //or

    /*@Transactional
    public Artifact createArtifact(String name, String description, Artifact.LoanStatus status, double loanFee,
                                   boolean isDamaged, double worth ){
        Artifact artifact = new Artifact();
        artifact.setName(name);
        artifact.setDescription(description);
        artifact.setLoanStatus(status);
        artifact.setIsDamaged(isDamaged);
        artifact.setLoanFee(loanFee);
        artifact.setWorth(worth);
        //do I add room location
        artifact = artifactRepository.save(artifact);
        return artifact;
    }*/

    /**
     * Delete an artifact from the system
     * @param artifactId
     */
    @Transactional
    public void deleteArtifact(Integer artifactId){
        //check if artifact exists before deleting
        if(artifactRepository.existsById(artifactId)) {
            artifactRepository.deleteById(artifactId);
        }
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
