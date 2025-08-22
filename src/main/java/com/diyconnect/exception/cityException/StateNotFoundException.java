package com.diyconnect.exception.cityException;

public class StateNotFoundException extends CityException {
    public StateNotFoundException() {
        super("State not found");
    }
}
