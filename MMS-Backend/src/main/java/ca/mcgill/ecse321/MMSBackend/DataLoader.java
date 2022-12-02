package ca.mcgill.ecse321.MMSBackend;

import ca.mcgill.ecse321.MMSBackend.dao.*;
import ca.mcgill.ecse321.MMSBackend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Time;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private MuseumManagementSystemRepository mmsRepository;
	@Autowired
	private ManagerRepository managerRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private ArtifactRepository artifactRepository;
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private SpecificWeekDayRepository specificWeekDayRepository;

	@Autowired
	public DataLoader(MuseumManagementSystemRepository mmsRepository, ManagerRepository managerRepository,
					  EmployeeRepository employeeRepository, ClientRepository clientRepository, ArtifactRepository artifactRepository,
					  RoomRepository roomRepository, SpecificWeekDayRepository specificWeekDayRepository) {
		this.mmsRepository = mmsRepository;
		this.managerRepository = managerRepository;
		this.employeeRepository = employeeRepository;
		this.clientRepository = clientRepository;
		this.artifactRepository = artifactRepository;
		this.roomRepository = roomRepository;
		this.specificWeekDayRepository = specificWeekDayRepository;
	}

	public void run(ApplicationArguments args) {

		MuseumManagementSystem museumManagementSystem = new MuseumManagementSystem();
		museumManagementSystem.setName("Cabinet Of Curiosities");
		museumManagementSystem.setTicketFee(15.00);
		museumManagementSystem.setOpenTime(Time.valueOf("9:00:00"));
		museumManagementSystem.setCloseTime(Time.valueOf("17:00:00"));
		museumManagementSystem.setMaxLoanNumber(5);
		mmsRepository.save(museumManagementSystem);

		createMuseumSpecificWeekDays(museumManagementSystem);
		createMuseumRooms(museumManagementSystem);

		Manager manager = new Manager();
		manager.setUsername("ManagerMarwan");
		manager.setName("Marwan");
		manager.setPassword("password");
		manager.setMuseumManagementSystem(museumManagementSystem);
		managerRepository.save(manager);

		Employee employee = new Employee();
		employee.setUsername("YuAnUi");
		employee.setName("Yu An Lu");
		employee.setPassword("password");
		employee.setMuseumManagementSystem(museumManagementSystem);
		employeeRepository.save(employee);

		Client client = new Client();
		client.setUsername("naziaC");
		client.setName("Nazia Chowdhury");
		client.setPassword("password");
		client.setMuseumManagementSystem(museumManagementSystem);
		clientRepository.save(client);

		Artifact artifact1 = new Artifact();
		artifact1.setName("Cat watching Fifa");
		artifact1.setImage("images/Cat.PNG");
		artifact1.setDescription("Cat watching Fifa while depressed (ㄒoㄒ)");
		artifact1.setLoanStatus(Artifact.LoanStatus.Available);
		artifact1.setIsDamaged(false);
		artifact1.setLoanFee(60.0);
		artifact1.setWorth(5600.0);
		artifact1.setRoomLocation(roomRepository.findRoomByName("Small Room 1").get(0));
		artifact1.setMuseumManagementSystem(museumManagementSystem);
		artifactRepository.save(artifact1);

		Artifact artifact2 = new Artifact();
		artifact2.setName("Slices Of Heaven");
		artifact2.setImage("images/orange.PNG");
		artifact2.setDescription("Oranges ~O~");
		artifact2.setLoanStatus(Artifact.LoanStatus.Available);
		artifact2.setIsDamaged(false);
		artifact2.setLoanFee(55.0);
		artifact2.setWorth(5005.0);
		artifact2.setRoomLocation(roomRepository.findRoomByName("Large Room 1").get(0));
		artifact2.setMuseumManagementSystem(museumManagementSystem);
		artifactRepository.save(artifact2);

		Artifact artifact3 = new Artifact();
		artifact3.setName("Mona be Dabbing");
		artifact3.setImage("images/monalisa.PNG");
		artifact3.setDescription("Mona Lisa is dabbing ヽ(O⌣Oヾ)");
		artifact3.setLoanStatus(Artifact.LoanStatus.Unavailable);
		artifact3.setIsDamaged(false);
		artifact3.setLoanFee(50.0);
		artifact3.setWorth(500.0);
		artifact3.setRoomLocation(roomRepository.findRoomByName("Storage Room").get(0));
		artifact3.setMuseumManagementSystem(museumManagementSystem);
		artifactRepository.save(artifact3);
	}


	/**
	 * Helper method that creates the 7 specific week days of the museum management system
	 *
	 * @author : Samantha Perez Hoffman (samperezh)
	 * @param museumManagementSystem
	 */
	private void createMuseumSpecificWeekDays(MuseumManagementSystem museumManagementSystem){
		SpecificWeekDay monday = new SpecificWeekDay();
		monday.setDayType(SpecificWeekDay.DayType.Monday);
		monday.setIsClosed(false);
		monday.setMuseumManagementSystem(museumManagementSystem);

		SpecificWeekDay tuesday = new SpecificWeekDay();
		tuesday.setDayType(SpecificWeekDay.DayType.Tuesday);
		tuesday.setIsClosed(false);
		tuesday.setMuseumManagementSystem(museumManagementSystem);

		SpecificWeekDay wednesday = new SpecificWeekDay();
		wednesday.setDayType(SpecificWeekDay.DayType.Wednesday);
		wednesday.setIsClosed(false);
		wednesday.setMuseumManagementSystem(museumManagementSystem);

		SpecificWeekDay thursday = new SpecificWeekDay();
		thursday.setDayType(SpecificWeekDay.DayType.Thursday);
		thursday.setIsClosed(false);
		thursday.setMuseumManagementSystem(museumManagementSystem);

		SpecificWeekDay friday = new SpecificWeekDay();
		friday.setDayType(SpecificWeekDay.DayType.Friday);
		friday.setIsClosed(false);
		friday.setMuseumManagementSystem(museumManagementSystem);

		SpecificWeekDay saturday = new SpecificWeekDay();
		saturday.setDayType(SpecificWeekDay.DayType.Saturday);
		saturday.setIsClosed(false);
		saturday.setMuseumManagementSystem(museumManagementSystem);

		SpecificWeekDay sunday = new SpecificWeekDay();
		sunday.setDayType(SpecificWeekDay.DayType.Sunday);
		sunday.setIsClosed(false);
		sunday.setMuseumManagementSystem(museumManagementSystem);

		specificWeekDayRepository.save(monday);
		specificWeekDayRepository.save(tuesday);
		specificWeekDayRepository.save(wednesday);
		specificWeekDayRepository.save(thursday);
		specificWeekDayRepository.save(friday);
		specificWeekDayRepository.save(saturday);
		specificWeekDayRepository.save(sunday);
	}

	/**
	 * helper method that creates the rooms of the museum management system
	 *
	 * @author : Lucy Zhang (Lucy-Zh)
	 * @param museumManagementSystem
	 *
	 */
	public void createMuseumRooms(MuseumManagementSystem museumManagementSystem) {
		// create 5 small rooms
		for (int i = 1; i <= 5; i++) {

			Room currentRoom = new Room();
			currentRoom.setName("Small Room " + i);
			currentRoom.setType(Room.RoomType.Small);
			currentRoom.setMuseumManagementSystem(museumManagementSystem);
			roomRepository.save(currentRoom);
		}

		// create 5 large rooms
		for (int i = 1; i <= 5; i++) {

			Room currentRoom = new Room();
			currentRoom.setName("Large Room " + i);
			currentRoom.setType(Room.RoomType.Large);
			currentRoom.setMuseumManagementSystem(museumManagementSystem);
			roomRepository.save(currentRoom);
		}

		// create storage room
		Room storageRoom = new Room();
		storageRoom.setName("Storage Room");
		storageRoom.setType(Room.RoomType.Storage);
		storageRoom.setMuseumManagementSystem(museumManagementSystem);
		roomRepository.save(storageRoom);
	}
}
