package com.hms.service;

import com.hms.entities.City;
import com.hms.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCity() {
        List<City> allCity = cityRepository.findAll();
        return allCity;
    }

    public City addCity(City city) {
        City saved = cityRepository.save(city);
        return saved;
    }

    public void deleteById(long id) {
        cityRepository.deleteById(id);
    }

    public City updateCityById(long id, City city) {
        City c = cityRepository.findById(id).get();
        c.setName(city.getName());
        City saved = cityRepository.save(c);
        return saved;
    }
}
