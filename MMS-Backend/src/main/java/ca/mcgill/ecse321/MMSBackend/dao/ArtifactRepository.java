package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;

public interface ArtifactRepository extends CrudRepository<Artifact, String>{
    public Artifact findArtifactByArtifactId(String id); 
}
