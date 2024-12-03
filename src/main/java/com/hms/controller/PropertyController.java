package com.hms.controller;


import com.hms.entities.Property;
import com.hms.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

//    public PropertyController(PropertyService propertyService) {
//        this.propertyService = propertyService;
//    }

    // http://localhost:8080/api/v1/property/create
    @PostMapping("/create")
    public ResponseEntity<Property> createProperty(
            @RequestBody Property property
    ) {
        Property savedProperty = propertyService.createPropertyData(property);
        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
    }

    @GetMapping("/listAllProperty")
    public ResponseEntity<List<Property>> listAllProperty(){
        List<Property> allProperty = propertyService.listAllProperty();
        return new ResponseEntity<>(allProperty, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deletePropertyById(
            @PathVariable long id
    ){
       propertyService.deletePropertyById(id);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Property> updatePropertyById(
            @PathVariable long id,
            @RequestBody Property property
    ){
       Property updateDetails =  propertyService.updatePropertyById(id, property);
        return new ResponseEntity<>(updateDetails, HttpStatus.OK);
    }

    @GetMapping("/search-hotels")
    public List<Property> searchHotels(
            @RequestParam String name
    ){
        List<Property> properties = propertyService.searchHotels(name);
        return properties;
    }

}
