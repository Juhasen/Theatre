package pl.juhas.theater;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import pl.juhas.theater.model.*;
import pl.juhas.theater.repository.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class TheaterApplicationTests {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PlayRepository playRepository;

    @Autowired
    private UserRepository userRepository;

    private Performance performance;
    private Reservation reservation;
    private User user;
    private Room room;

    @BeforeEach
    void setUp() {
        // Create and save Room
        room = new Room();
        room.setName("Room 1");
        roomRepository.save(room);

        // Create and save Play
        Play play = new Play("Sample Play", "A sample description of the play.", "Joe Jackson", "Action", 120);
        playRepository.save(play);

        // Create and save Performance
        performance = new Performance();
        performance.setStartTime(LocalDateTime.now().plusDays(1));
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
        ReservationStatus status = new ReservationStatus("CONFIRMED");

        // Create and save Reservation
        reservation = new Reservation();
        reservation.setPerformance(performance);
        reservation.setUser(user);
        reservation.setStatus(status);
        reservationRepository.save(reservation);

    }

    @Test
    void contextLoads() {
        assertThat(performanceRepository).isNotNull();
        assertThat(reservationRepository).isNotNull();
        assertThat(roomRepository).isNotNull();
        assertThat(seatRepository).isNotNull();
    }

    @Test
    void testFindPerformanceSummariesByRoomId() {
        Pageable pageable = PageRequest.of(0, 10);
        var result = performanceRepository.findPerformanceSummariesByRoomId(room.getId(), pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).allMatch(dto -> dto.getRoomName().equals(room.getName()));
    }

    @Test
    void testFindPerformanceSummariesById() {
        var result = performanceRepository.findPerformanceSummariesById(performance.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(performance.getId());
    }

    @Test
    void testFindPerformanceSummariesByPlayTitle() {
        Pageable pageable = PageRequest.of(0, 10);
        var result = performanceRepository.findPerformanceSummariesByPlayTitle(performance.getPlay().getTitle(), pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).allMatch(dto -> dto.getTitle().contains(performance.getPlay().getTitle()));
    }

    @Test
    void testFindUsersByPerformanceId() {
        Pageable pageable = PageRequest.of(0, 10);
        var result = reservationRepository.findUsersByPerformanceId(performance.getId(), pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).allMatch(user -> user.getId().equals(this.user.getId()));
    }

    @Test
    void testCountOccupiedSeats() {
        LocalDateTime time = performance.getStartTime();
        Long count = seatRepository.countOccupiedSeats(room.getId(), time);

        assertThat(count).isEqualTo(0L);  // No seats should be occupied yet
    }

    @Test
    void testCountDistinctRoomsByPlayId() {
        Long count = roomRepository.countDistinctRoomsByPlayId(performance.getPlay().getId());

        assertThat(count).isEqualTo(1L);  // The play is scheduled in one room
    }

    @Test
    void testCountTicketsBoughtByUserInDateRange() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        Long count = seatRepository.countTicketsBoughtByUserInDateRange(user.getId(), startDate, endDate);

        assertThat(count).isEqualTo(0L);  // No tickets bought yet
    }
}
