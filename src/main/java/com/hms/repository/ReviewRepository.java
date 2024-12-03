package com.hms.repository;

import com.hms.entities.AppUser;
import com.hms.entities.Property;
import com.hms.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByAppUser(AppUser appUser);

    boolean existsByAppUserAndProperty(AppUser appUser, Property property);
}