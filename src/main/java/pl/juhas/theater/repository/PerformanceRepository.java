package pl.juhas.theater.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.juhas.theater.dto.PerformanceSummaryDTO;
import pl.juhas.theater.model.Performance;

import java.util.List;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {


    @Query("SELECT new pl.juhas.theater.dto.PerformanceSummaryDTO(" +
            "p.play.title, p.startTime, p.room.name) " +
            "FROM Performance p " +
            "WHERE p.room.id = :roomId")
    Page<PerformanceSummaryDTO> findPerformanceSummariesByRoomId(@Param("roomId") Long roomId, Pageable pageable);


    @Query("SELECT new pl.juhas.theater.dto.PerformanceSummaryDTO(" +
            "p.play.title, p.startTime, p.room.name) " +
            "FROM Performance p " +
            "WHERE p.id = :performanceId")
    List<PerformanceSummaryDTO> findPerformanceSummariesById(@Param("performanceId") Long performanceId);


    @Query("SELECT new pl.juhas.theater.dto.PerformanceSummaryDTO(" +
            "p.play.title, p.startTime, p.room.name) " +
            "FROM Performance p " +
            "WHERE LOWER(p.play.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<PerformanceSummaryDTO> findPerformanceSummariesByPlayTitle(@Param("title") String title, Pageable pageable);


    @Query("SELECT COUNT(p.room.id) " +
            "FROM Performance p " +
            "WHERE p.play.id = :playId")
    Long countRoomsByPlay_Id(Long playId);
}