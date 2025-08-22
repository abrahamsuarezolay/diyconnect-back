package com.diyconnect.exception.cityException;

public class CountryNotFoundException extends CityException {
    public CountryNotFoundException() {
        super("Country not found");
    }
}
