package com.hms.service;


import com.hms.entities.AppUser;
import com.hms.entities.Property;
import com.hms.entities.Review;
import com.hms.repository.PropertyRepository;
import com.hms.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    public Review findPropertyById(Review review, long id, AppUser user) {
        Property property = propertyRepository.findById(id).get();

        review.setProperty(property);
        review.setAppUser(user);

        return reviewRepository.save(review);

    }

    public boolean existsPropertyAndUser(Long propertyId, AppUser user) {
        Property property = propertyRepository.findById(propertyId).get();
        return reviewRepository.existsByAppUserAndProperty(user, property);
    }

    public List<Review> findAllReviewsByUser(AppUser appUser) {
        return reviewRepository.findByAppUser(appUser);
    }
}
