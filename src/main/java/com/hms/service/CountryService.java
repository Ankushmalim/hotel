package com.hms.service;

import com.hms.entities.Country;
import com.hms.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;


    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    public List<Country> getAllCountry() {
        List<Country> allCountries = countryRepository.findAll();
        return allCountries;
    }

    public Country createCountry(Country country) {
        Country save = countryRepository.save(country);
        return save;
    }

    public void deleteCountryById(long id) {
        countryRepository.deleteById(id);
    }

    public Country updateCountryById(long id, Country country) {
        Country c = countryRepository.findById(id).get();
        c.setName(country.getName());
        Country saved = countryRepository.save(c);
        return saved;
    }


}
