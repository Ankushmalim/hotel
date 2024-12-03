package com.hms.service;

import com.hms.entities.Images;
import com.hms.repository.ImagesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImagesService {
    private ImagesRepository imagesRepository;

    public ImagesService(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    public List<Images> findAllImages() {
        List<Images> allImages = imagesRepository.findAll();
        return allImages;
    }

    public List<Images> findAllByProperty_Id(long propertyId) {
        return imagesRepository.findAllByProperty_Id(propertyId);
    }
}
