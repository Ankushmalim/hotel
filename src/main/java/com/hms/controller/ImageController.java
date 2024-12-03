package com.hms.controller;

import com.hms.entities.AppUser;
import com.hms.entities.Images;
import com.hms.entities.Property;
import com.hms.repository.ImagesRepository;
import com.hms.service.BucketService;
import com.hms.service.ImagesService;
import com.hms.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private BucketService bucketService;
    private PropertyService propertyService;
    private ImagesRepository imagesRepository;
    private ImagesService imagesService;

    public ImageController(BucketService bucketService, PropertyService propertyService, ImagesRepository imagesRepository, ImagesService imagesService) {
        this.bucketService = bucketService;
        this.propertyService = propertyService;
        this.imagesRepository = imagesRepository;
        this.imagesService = imagesService;
    }

    @PostMapping(path = "/upload/file/{bucketName}/property/{propertyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPropertyPhotos(@RequestParam MultipartFile file,
                                        @PathVariable String bucketName,
                                        @PathVariable long propertyId,
                                        @AuthenticationPrincipal AppUser user
    ) {
        Property propwerties = propertyService.findById(propertyId);
        String imageUrl = bucketService.uploadFile(file, bucketName);
        Images image = new Images();
        image.setUrl(imageUrl);
        image.setProperty(propwerties);
        Images savedImage = imagesRepository.save(image);
        return new ResponseEntity<>(savedImage, HttpStatus.OK);
    }

    @GetMapping("/get-all-images")
    public ResponseEntity<List<Images>> getAllImages() {
        List<Images> allImages = imagesService.findAllImages();
        return new ResponseEntity<>(allImages, HttpStatus.OK);
    }

    @GetMapping("/get-images-by-property/{propertyId}")
    public ResponseEntity<List<Images>> getImagesByProperty(@PathVariable long propertyId) {
        List<Images> imagesByProperty = imagesService.findAllByProperty_Id(propertyId);
        return new ResponseEntity<>(imagesByProperty, HttpStatus.OK);
    }
}