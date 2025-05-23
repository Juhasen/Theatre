package pl.juhas.theater;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import pl.juhas.theater.model.*;
import pl.juhas.theater.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
@Rollback
class TheaterApplicationTests {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationSeatRepository reservationSeatRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PlayRepository playRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationStatusRepository reservationStatusRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    private Performance performance;
    private Room room;
    private User user;

    private Play play;

    @BeforeEach
    void setUp() {
        performanceRepository.deleteAll();
        reservationRepository.deleteAll();
        roomRepository.deleteAll();
        reservationSeatRepository.deleteAll();
        seatRepository.deleteAll();
        playRepository.deleteAll();
        userRepository.deleteAll();
        reservationStatusRepository.deleteAll();

        //Create and save Seats
        for (int i = 0; i < 10; i++) {
            Seat seat = new Seat();
            seat.setRow(i);
            seat.setColumn(0);
            seat.setIsAvailable(true);
            seatRepository.save(seat);
        }

        // Create and save Room
        room = new Room();
        room.setName("Room 1");
        room.setSeats(seatRepository.findAll());
        roomRepository.save(room);

        // Create and save Play
        play = new Play("Sample Play", "A sample description of the play.", "Joe Jackson", "Action", 120);
        playRepository.save(play);

        // Create and save Performance
        performance = new Performance();
        performance.setStartTime(LocalDateTime.of(LocalDate.now().plusDays( 1), LocalTime.of(18, 30)));
        performance.setRoom(room);
        performance.setPlay(play);
        performanceRepository.save(performance);

        // Create and save User
        user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        userRepository.save(user);


        //Create Status
        ReservationStatus statusConfirmed = new ReservationStatus("confirmed");
        ReservationStatus statusDeclined = new ReservationStatus("declined");
        reservationStatusRepository.saveAll(List.of(statusDeclined, statusConfirmed));

        // Create TicketType
        TicketType ticketType = new TicketType();
        ticketType.setType("standard");
        ticketType.setPrice(100.0);
        ticketTypeRepository.save(ticketType);
        TicketType ticketTypeVIP = new TicketType();
        ticketTypeVIP.setType("vip");
        ticketTypeVIP.setPrice(200.0);
        ticketTypeRepository.save(ticketTypeVIP);

        // Create and save Reservation
        Reservation reservation = new Reservation();
        reservation.setPerformance(performance);
        reservation.setUser(user);
        reservation.setStatus(statusConfirmed);
        reservation.setTicketType(ticketType);
        reservationRepository.save(reservation);
    }

    @Test
    void contextLoads() {
        assertThat(performanceRepository).isNotNull();
        assertThat(reservationRepository).isNotNull();
        assertThat(roomRepository).isNotNull();
        assertThat(reservationSeatRepository).isNotNull();
        assertThat(seatRepository).isNotNull();
        assertThat(playRepository).isNotNull();
        assertThat(userRepository).isNotNull();
        assertThat(reservationStatusRepository).isNotNull();
    }

    //1. Listę przedstawień granych w sali o danym id.
    @Test
    void testFindPerformanceSummariesByRoomId() {
        log.info("------------------------------testFindPerformanceSummariesByRoomId-------------------------------");
        //Create 5 more performances
        for (int i = 0; i < 5; i++){
            Performance performance = new Performance();
            performance.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(i + 10), LocalTime.of(i, i+2)));
            performance.setRoom(room);
            Play play = new Play("Sample Play " + i, "A sample description of the play " + i, "Director" + i, "Genre" + i, i*30);
            playRepository.save(play);
            performance.setPlay(play);
            performanceRepository.save(performance);
        }

        Pageable pageable = PageRequest.of(0, 2);
        var result = performanceRepository.findPerformanceSummariesByRoomId(room.getId(), pageable);

        log.info("Performances found by room ID:");
        result.getContent().forEach(dto -> log.info(dto.toString()));

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);
        assertThat(result.getContent()).allMatch(dto -> dto.getRoomName().contains("Room 1"));

        // Test with a different page size
        pageable = PageRequest.of(0, 5);
        result = performanceRepository.findPerformanceSummariesByRoomId(room.getId(), pageable);
        assertThat(result).hasSize(5);
    }

    //2. Listę przedstawień o danym id.
    @Test
    void testFindPerformanceSummariesById() {
        log.info("------------------------------testFindPerformanceSummariesById-------------------------------");
        var result = performanceRepository.findPerformanceSummariesById(performance.getId());

        log.info("Performance summary found by ID {}: {}", performance.getId(), result);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo(performance.getPlay().getTitle());
    }

    //3. Listę przedstawień o danym tytule.
    @Test
    void testFindPerformanceSummariesByPlayTitle() {
        log.info("------------------------------testFindPerformanceSummariesByPlayTitle-------------------------------");
        // Create 5 more performances with the same play title
        for(int i = 0; i < 5; i++){
            Performance performance = new Performance();
            performance.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(i + 10), LocalTime.of(i, i+2)));
            performance.setRoom(room);
            Play play = new Play(this.performance.getPlay().getTitle(), "A sample description of the play " + i, "Director" + i, "Genre" + i, i*30);
            playRepository.save(play);
            performance.setPlay(play);
            performanceRepository.save(performance);
        }

        Pageable pageable = PageRequest.of(0, 10);
        var result = performanceRepository.findPerformanceSummariesByPlayTitle(performance.getPlay().getTitle(), pageable);
        log.info("Performances found by play title '{}'", performance.getPlay().getTitle());
        result.getContent().forEach(dto -> log.info(dto.toString()));

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).allMatch(dto -> dto.getTitle().contains(performance.getPlay().getTitle()));
    }

    //4. Listę uczestników na przedstawieniu o danym id.
    @Test
    void testFindUsersByPerformanceId() {
        log.info("------------------------------testFindUsersByPerformanceId-------------------------------");
        //Create 5 more users for the same performance
        for (int i = 0; i < 5; i++){
            User user = new User();
            user.setEmail("user" + i + "@example.com");
            user.setFirstName("User" + i);
            user.setLastName("Doe" + i);
            userRepository.save(user);

            Reservation reservation = new Reservation();
            reservation.setPerformance(performance);
            reservation.setUser(user);
            reservation.setStatus(reservationStatusRepository.findByName("confirmed"));
            reservation.setTicketType(ticketTypeRepository.findByType("standard"));
            if(i % 2 == 0) {
                reservation.setTicketType(ticketTypeRepository.findByType("vip"));
            }
            reservationRepository.save(reservation);
        }


        Pageable pageable = PageRequest.of(0, 10);
        var result = reservationRepository.findUsersByPerformanceId(performance.getId(), pageable);

        result.getContent().forEach(u ->
                log.info("User found: id={}, email={}, name={} {}", u.getId(), u.getEmail(), u.getFirstName(), u.getLastName())
        );

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(6);
    }


    //5. Listę przedstawień uczestnika o danym id.
    @Test
    void testFindPerformancesByUserId() {
        log.info("------------------------------testFindPerformancesByUserId-------------------------------");
        //Create 5 more performances for the same user
        for (int i = 0; i < 5; i++){
            Performance performance = new Performance();
            performance.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(i + 10), LocalTime.of(i, i+2)));
            performance.setRoom(room);
            Play play = new Play("Sample Play " + i, "A sample description of the play " + i, "Director" + i, "Genre" + i, i*30);
            playRepository.save(play);
            performance.setPlay(play);
            performanceRepository.save(performance);

            Reservation reservation = new Reservation();
            reservation.setPerformance(performance);
            reservation.setUser(user);
            reservation.setStatus(reservationStatusRepository.findByName("confirmed"));
            reservation.setTicketType(ticketTypeRepository.findByType("standard"));
            reservationRepository.save(reservation);
        }

        Pageable pageable = PageRequest.of(0, 4);
        var result = reservationRepository.findPerformancesByUserId(user.getId(), pageable);

        result.getContent().forEach(p ->
                log.info("Performance found: title={}, startTime={}", p.getTitle(), p.getStartTime())
        );

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(4);
    }


    //6. Listę przedstawień uczestnika o danym loginie.
    @Test
    void testFindPerformancesByUserLogin() {
        log.info("------------------------------testFindPerformancesByUserLogin-------------------------------");
        //Create 5 more performances for the same user
        for (int i = 0; i < 5; i++){
            Performance performance = new Performance();
            performance.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(i + 10), LocalTime.of(i, i+2)));
            performance.setRoom(room);
            Play play = new Play("Sample Play " + i, "A sample description of the play " + i, "Director" + i, "Genre" + i, i*30);
            playRepository.save(play);
            performance.setPlay(play);
            performanceRepository.save(performance);

            Reservation reservation = new Reservation();
            reservation.setPerformance(performance);
            reservation.setUser(user);
            reservation.setStatus(reservationStatusRepository.findByName("confirmed"));
            reservation.setTicketType(ticketTypeRepository.findByType("standard"));
            reservationRepository.save(reservation);
        }

        Pageable pageable = PageRequest.of(0, 5);
        var result = reservationRepository.findPerformanceByUserEmail(user.getEmail(), pageable);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        result.getContent().forEach(p ->
                log.info("Performance found: title={}, startTime={}", p.getTitle(), p.getStartTime().format(formatter))
        );

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(5);
    }

    //7. Liczbę miejsc zajętych w danej sali o danej godzinie.
    @Test
    void testCountOccupiedSeats() {
        log.info("------------------------------testCountOccupiedSeats-------------------------------");
        // Create 5 more reservations for the same performance
        for (int i = 0; i < 8; i++){
            Reservation reservation = new Reservation();
            reservation.setPerformance(performance);
            reservation.setUser(user);
            reservation.setStatus(reservationStatusRepository.findByName("confirmed"));
            reservation.setTicketType(ticketTypeRepository.findByType("standard"));
            ReservationSeat reservationSeat = new ReservationSeat();
            Seat seat = new Seat();
            seat.setRow(i);
            seat.setColumn(i);
            seat.setIsAvailable(false);
            seatRepository.save(seat);
            reservationSeat.setSeat(seat);
            reservationSeat.setReservation(reservation);
            reservation.setReservationSeats(List.of(reservationSeat));
            reservationRepository.save(reservation);
        }

        Long count = reservationSeatRepository.countOccupiedSeats(room.getId(), performance.getStartTime());
        log.info("Number of occupied seats in room {} at time {}: {}", room.getId(), performance.getStartTime(), count);

        assertThat(count).isEqualTo(8);
    }

    //8. Liczbę sal, w których wystawiano przedstawienie o danym id.
    @Test
    void testCountRoomsByPlayId() {
        log.info("------------------------------testCountRoomsByPlayId-------------------------------");
        // Create 5 more performances in different rooms
        for (int i = 0; i < 5; i++){
            Room room = new Room();
            room.setName("Room " + (i + 2));
            room.setSeats(seatRepository.findAll());
            roomRepository.save(room);

            Performance performance = new Performance();
            performance.setStartTime(LocalDateTime.of(LocalDate.now().plusDays(i + 10), LocalTime.of(i, i+2)));
            performance.setRoom(room);
            performance.setPlay(play);
            performanceRepository.save(performance);
        }

        Long count = performanceRepository.countRoomsByPlay_Id(play.getId());
        log.info("Number of rooms where play with ID {} was held: {}", play.getId(), count);

        assertThat(count).isEqualTo(6);
    }

    //9. Liczbę miejsc, jaką kupił w teatrze dany użytkownik w danym przedziale dat.
    @Test
    void testCountTicketsBoughtByUserInDateRange() {
        log.info("------------------------------testCountTicketsBoughtByUserInDateRange-------------------------------");
        // Create 5 more reservations for the same user in different date ranges
        for (int i = 0; i < 5; i++){
            Reservation reservation = new Reservation();
            reservation.setPerformance(performance);
            reservation.setUser(user);
            reservation.setStatus(reservationStatusRepository.findByName("confirmed"));
            reservation.setTicketType(ticketTypeRepository.findByType("standard"));
            reservation.setCreatedAt(LocalDateTime.now().minusDays(i));
            ReservationSeat reservationSeat = new ReservationSeat();
            Seat seat = new Seat();
            seat.setRow(i);
            seat.setColumn(i);
            seat.setIsAvailable(false);
            seatRepository.save(seat);
            reservationSeat.setSeat(seat);
            reservationSeat.setReservation(reservation);
            reservation.setReservationSeats(List.of(reservationSeat));
            reservationRepository.save(reservation);
        }

        LocalDateTime startDate = LocalDateTime.now().minusDays(3);
        LocalDateTime endDate = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        Long count = reservationSeatRepository.countTicketsBoughtByUserInDateRange(user.getId(), startDate, endDate);
        log.info("Number of tickets bought by user {} in date range {} to {}: {}", user.getId(), startDate.format(formatter), endDate.format(formatter), count);

        assertThat(count).isEqualTo(4);
    }
}
