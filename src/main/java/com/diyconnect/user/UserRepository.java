package com.diyconnect.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<List<User>> findByCityName(String cityName);
    Optional<List<User>> findByCityState(String stateName);
    Optional<List<User>> findByCityCountry(String countryName);

    Optional<List<User>> findByCityNameAndCityStateAndCityCountry(String cityName, String stateName, String countryName);
    Optional<List<User>> findByCityStateAndCityCountry(String stateName, String countryName);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
