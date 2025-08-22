package com.diyconnect.city;

import com.diyconnect.band.Band;
import com.diyconnect.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {
    private long city_id;
    private String name;
    private String state;
    private String country;
    private List<User> users;
    private List<Band> bands;
}

