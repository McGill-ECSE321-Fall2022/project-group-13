package ca.mcgill.ecse321.MMSBackend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ca.mcgill.ecse321.MMSBackend.dao.*;
import ca.mcgill.ecse321.MMSBackend.model.*;

@ExtendWith(MockitoExtension.class)
public class TestArtifactService {
    @Mock
    private ArtifactRepository artifactDao;

    @Mock
    private MuseumManagementSystemRepository mmsDao;

    @InjectMocks
    private ArtifactService service;

    private static final int ARTIFACT_KEY = 10;
    private static final String A_NAME = "Painting";
    private static final String A_DESCRIPTION = "This is a painting of a rose.";
    private static final String A_IMAGE= "path";
    private static final Artifact.LoanStatus LOAN_STATUS = Artifact.LoanStatus.Unavailable;
    private static final double LOAN_FEE = 100.00;
    private static final boolean IS_DAMAGED = false;
    private static final double WORTH = 500.00;
    private static final int ROOM_ID = 5;
    private static final int SYSTEM_ID = 1;

    @BeforeEach
    public void setMockOutput() {
        lenient().when(artifactDao.findArtifactByArtifactId(anyInt())).thenAnswer( (InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(ARTIFACT_KEY)) {
                Artifact artifact = new Artifact();
                artifact.setArtifactId(ARTIFACT_KEY);
                artifact.setName(A_NAME);
                artifact.setImage(A_IMAGE);
                artifact.setLoanStatus(LOAN_STATUS);
                artifact.setLoanFee(LOAN_FEE);
                artifact.setIsDamaged(IS_DAMAGED);
                artifact.setWorth(WORTH);
                //artifact.setRoomLocation();
                //artifact.setMuseumManagementSystem();
                return artifact;
            } else {
                return null;
            }
        });

        // Whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(artifactDao.save(any(Artifact.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateArtifact(){
        assertEquals(0, service.getAllArtifacts().size());

        String name = "Picasso";
        String description = "I like it. Picasso.";
        String image = "PATH";
        Artifact.LoanStatus status = Artifact.LoanStatus.Unavailable;
        int loanFee = 1000000;
        boolean isDamaged = false;
        int worth = 20000000;
        int roomId = 5;
        int systemId = 1;

        Artifact a = null;
        try{
            a = service.createArtifact(name,description,image,status,loanFee,isDamaged,worth,roomId,systemId);
        }catch (IllegalArgumentException e){
            fail();
        }

        assertNotNull(a);
        assertEquals(name, a.getName());
        assertEquals(description, a.getDescription());
        assertEquals(image, a.getImage());
        assertEquals(status, a.getLoanStatus());
        assertEquals(loanFee, a.getLoanFee());
        assertEquals(isDamaged, a.getIsDamaged());
        assertEquals(worth, a.getWorth());
        assertEquals(roomId, a.getRoomLocation().getRoomId());
        assertEquals(systemId, a.getMuseumManagementSystem().getSystemId());
    }

    @Test
    public void testCreateArtifactNull(){
        String name = null;
        String description = null;
        String image = null;
        Artifact.LoanStatus status = null;
        int loanFee = Integer.parseInt(null);
        boolean isDamaged = Boolean.parseBoolean(null);
        int worth = Integer.parseInt(null);
        int roomId = Integer.parseInt(null);
        int systemId = Integer.parseInt(null);

        String error = null;
        Artifact a = null;
        try{
            a = service.createArtifact(name,description,image,status,loanFee,isDamaged,worth,roomId,systemId);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        }

        assertNull(a);
        //check error
        assertEquals("No null values are accepted.", error);
    }


}
