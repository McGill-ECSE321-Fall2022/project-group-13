package ca.mcgill.ecse321.MMSBackend.integration;

import ca.mcgill.ecse321.MMSBackend.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MMSBackend.dto.ArtifactDto;
import ca.mcgill.ecse321.MMSBackend.dto.ClientDto;
import ca.mcgill.ecse321.MMSBackend.dto.MuseumManagementSystemDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class LoanRequestIntegrationTests {
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    public class PersonIntegrationTests {

        @Autowired
        private TestRestTemplate loanRequest;

        @Autowired
        private LoanRequestRepository loanRequestRepo;

        @BeforeEach
        @AfterEach
        public void clearDatabase() {
            loanRequestRepo.deleteAll();
        }

        @Test
        public void testCreateAndGetPerson() {
            int id = testCreatePerson();
        }

        private int testCreatePerson() {
            ResponseEntity<LoanRequestDto> response = loanRequest.postForEntity("/loanRequest", new LoanRequestDto(), LoanRequestDto.class);

            assertNotNull(response);
            assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
            assertNotNull(response.getBody(), "Response has body");
            return response.getBody().getRequestId();
        }
    }

     class LoanRequestDto {
        public enum LoanRequestStatusDto {Approved, Rejected, Pending}

        private int requestId;
        private int loanDuration;
        private double fee;
        private ClientDto client;
        private ArtifactDto artifact;
        private LoanRequestStatusDto status;
        private MuseumManagementSystemDto museumManagementSystem;

        public LoanRequestDto() {
        }

        public LoanRequestDto(int requestId, int loanDuration, double fee, ClientDto client, ArtifactDto artifact, LoanRequestStatusDto status, MuseumManagementSystemDto museumManagementSystem) {
            this.requestId = requestId;
            this.loanDuration = loanDuration;
            this.client = client;
            this.artifact = artifact;
            this.fee = fee;
            this.status = status;
            this.museumManagementSystem = museumManagementSystem;
        }

        public int getRequestId() {
            return requestId;
        }

        public int getLoanDuration() {
            return loanDuration;
        }

        public double getFee() {
            return fee;
        }

        public LoanRequestStatusDto getStatus() {
            return status;
        }

        public MuseumManagementSystemDto getMuseumManagementSystem() {
            return museumManagementSystem;
        }

        public ClientDto getClient() {
            return client;
        }

        public ArtifactDto getArtifact() {
            return artifact;
        }
    }
}
