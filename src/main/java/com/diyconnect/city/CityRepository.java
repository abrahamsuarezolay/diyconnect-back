package com.diyconnect.city;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {

    Optional <List<City>> findByName(String name);
    Optional <List<City>> findByState(String state);
    Optional <List<City>> findByCountry(String country);

    Optional<City> findFirstByNameAndStateAndCountry(String name, String state, String country);

}
