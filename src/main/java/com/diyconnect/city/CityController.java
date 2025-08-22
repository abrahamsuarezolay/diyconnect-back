package com.diyconnect.city;

import com.diyconnect.exception.cityException.CityNotFoundException;
import com.diyconnect.utils.mappers.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    private DTOMapper dtoMapper = new DTOMapper();

    @GetMapping("/bycity")
    public ResponseEntity<?> getCityByName(@RequestParam String cityName){
        try{
            List<City> citiesByName = cityService.findByName(cityName).get();
            List <CityDTO> citiesDTO = dtoMapper.citiesToDTO(citiesByName);

            return new ResponseEntity <List<CityDTO>> (citiesDTO, HttpStatus.OK);

        }catch(CityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bystate")
    public ResponseEntity<?> getCitiesByState(@RequestParam String stateName){
        try{
            List<City> citiesByState = cityService.findByState(stateName).get();
            List<CityDTO> citiesDto = dtoMapper.citiesToDTO(citiesByState);

            return new ResponseEntity<List<CityDTO>>(citiesDto, HttpStatus.OK);

        }catch(CityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/bycountry")
    public ResponseEntity<?> getCitiesByCountry(@RequestParam String countryName){
        try{
            List<City> citiesByCountry = cityService.findByCountry(countryName).get();
            List<CityDTO> citiesDto = dtoMapper.citiesToDTO(citiesByCountry);

            return new ResponseEntity<List<CityDTO>>(citiesDto, HttpStatus.OK);

        }catch(CityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
