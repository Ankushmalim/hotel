package com.hms.controller;

import com.hms.entities.City;
import com.hms.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController("/api/v1/city")
public class CityController {

    @Autowired
    private CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/addCity")
    public ResponseEntity<City> addCity(
            @RequestBody City city
    ){
        City addCity = cityService.addCity(city);
        return new ResponseEntity<>(addCity, HttpStatus.CREATED);
    }

    @GetMapping("/listCity")
    public ResponseEntity<List<City>> getAllCity(){
        List<City> allCity = cityService.getAllCity();
        return new ResponseEntity<>(allCity, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCity(
          @PathVariable long id
    ){
        cityService.deleteById(id);
        return new ResponseEntity<>("Deleted City", HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<City> updateCity(
            @PathVariable long id,
            @RequestBody City city
    ){
        City updateCity = cityService.updateCityById(id, city);
        return new ResponseEntity<>(updateCity, HttpStatus.OK);
    }

}
