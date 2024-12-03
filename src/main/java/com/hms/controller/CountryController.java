package com.hms.controller;

import com.hms.entities.Country;
import com.hms.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/country")
public class CountryController {
    @Autowired
    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/listAllCountry")
    public ResponseEntity<List<Country>> getAllCountry(){
        List<Country> allCountry = countryService.getAllCountry();
        return new ResponseEntity<>(allCountry, HttpStatus.OK);
    }

    @PostMapping("/createCountry")
    public ResponseEntity<Country> createCountry(
            @RequestBody Country country
    ){
        Country addCountry  = countryService.createCountry(country);
        return new ResponseEntity<>(addCountry, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCountryById(
            @RequestParam long id
    ){
        countryService.deleteCountryById(id);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Country> updateCountry(
            @PathVariable long id,
            @RequestBody Country country
    ) {
        Country updatedCountry = countryService.updateCountryById(id, country);
        return new ResponseEntity<>(updatedCountry, HttpStatus.OK);
    }


}
