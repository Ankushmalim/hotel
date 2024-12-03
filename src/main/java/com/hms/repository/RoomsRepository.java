package com.hms.repository;

import com.hms.entities.Property;
import com.hms.entities.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
   @Query("SELECT r FROM Rooms r WHERE r.type = :type AND r.property.id = :propertyId AND r.date BETWEEN :startDate AND :endDate")
   List<Rooms> findByTypeAndProperty(
           @Param("type") String type,
           @Param("propertyId") Long propertyId,
           @Param("startDate") LocalDate fromDate,
           @Param("endDate") LocalDate toDate
   );
}