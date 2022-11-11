package ca.mcgill.ecse321.MMSBackend.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import ca.mcgill.ecse321.MMSBackend.dao.ShiftRepository;
import ca.mcgill.ecse321.MMSBackend.dao.SpecificWeekDayRepository;

@ExtendWith(MockitoExtension.class)
public class TestEmployeeScheduleService {

    @Mock 
    private ShiftRepository shiftRepository;
    @Mock
    private SpecificWeekDayRepository specificWeekDayRepository;
    
    @InjectMocks
    private EmployeeScheduleService service;
    
    @BeforeEach
    public void setMockOutput(){

    }
    
}
