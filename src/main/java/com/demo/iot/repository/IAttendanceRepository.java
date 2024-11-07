package com.demo.iot.repository;

import com.demo.iot.common.Shift;
import com.demo.iot.entity.Attendance;
import com.demo.iot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAttendanceRepository extends JpaRepository<Attendance, Integer> {
    Optional<Attendance> findByUserAndDateAndShiftAndLocation(User user, LocalDate date, Shift shift, String location);

    @Query("SELECT a FROM Attendance a JOIN a.user u " +
            "WHERE (:startDate IS NULL OR a.date >= :startDate) AND (:endDate IS NULL OR a.date <= :endDate) " +
            "AND (:shift IS NULL OR a.shift = :shift) " +
            "AND (:username IS NULL OR u.username LIKE CONCAT('%', :username, '%')) " +
            "AND (:location IS NULL OR a.location LIKE CONCAT('%', :location, '%')) ")
    Page<Attendance> filterAttendance(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("shift") Shift shift,
            @Param("username") String username,
            @Param("location") String location,
            Pageable pageable);
}

