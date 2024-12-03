package com.hms.service;

import com.hms.entities.*;

import com.hms.repository.CityRepository;
import com.hms.repository.CountryRepository;
import com.hms.repository.PropertyRepository;
import com.hms.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ReviewRepository reviewRepository;


    public Property createPropertyData(Property property) {
        Long cityId = property.getCity().getId();
        Long countryId = property.getCountry().getId();
        Optional<City> opCity = cityRepository.findById(cityId);
        if(opCity.isPresent()){
            property.setCity(opCity.get());
        }else{
            System.out.println("city is not present");
        }
        Optional<Country> opCountry = countryRepository.findById(countryId);
        if(opCountry.isPresent()){
            property.setCountry(opCountry.get());
        }else{
            System.out.println("Country is not present");
        }
        return propertyRepository.save(property);
    }


    public List<Property> listAllProperty() {
        List<Property> allPropertyLsit = propertyRepository.findAll();
        return allPropertyLsit;
    }

    public void deletePropertyById(long id) {
         propertyRepository.deleteById(id);
    }

    public Property updatePropertyById(long id, Property property) {
        Property property1 = propertyRepository.findById(id).get();
        property1.setName(property.getName());
        property1.setNo_of_bedrooms(property.getNo_of_bedrooms());
        property1.setNo_of_bathrooms(property.getNo_of_bathrooms());
        property1.setNo_of_beds(property.getNo_of_beds());
        property1.setNo_of_guest(property.getNo_of_guest());
        property1.setCountry(property.getCountry());
        property1.setCity(property.getCity());
        Property saved = propertyRepository.save(property1);
        return saved;

    }

    public List<Property> searchHotels(String name){
        return propertyRepository.searchHotel(name);
    }


    public Property findById(long propertyId) {
        Property property = propertyRepository.findById(propertyId).get();
        return property;
    }
}
