package ca.mcgill.ecse321.MMSBackend.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.MMSBackend.model.Artifact;

/**
 * @author Yu An Lu
 */
public interface ArtifactRepository extends CrudRepository<Artifact, Integer>{
    public Artifact findArtifactByArtifactId(Integer id); 
}
