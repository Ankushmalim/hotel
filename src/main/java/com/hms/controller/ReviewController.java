package com.hms.controller;


import com.hms.entities.AppUser;
import com.hms.entities.Review;
import com.hms.service.PropertyService;
import com.hms.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    private ReviewService reviewService;
    private PropertyService propertyService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    ///api/v1/review?propertyId=1
    @PostMapping
    public ResponseEntity<?> write(
            @RequestBody Review review,
            @RequestParam long propertyId,
            @AuthenticationPrincipal AppUser user
    ){
        if (reviewService.existsPropertyAndUser(propertyId, user)){
            return new ResponseEntity<>("Property and User already exist", HttpStatus.CONFLICT);
        }
        Review savedReview = reviewService.findPropertyById(review, propertyId, user);
        return new ResponseEntity<>(savedReview, HttpStatus.OK);

    }

    @GetMapping("/user/review")
    public ResponseEntity<List<Review>> listAllReviews(
            @AuthenticationPrincipal AppUser appUser
    ){
        List<Review> reviews = reviewService.findAllReviewsByUser(appUser);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
