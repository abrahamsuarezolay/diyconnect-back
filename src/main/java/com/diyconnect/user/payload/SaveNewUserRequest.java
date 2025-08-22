package com.diyconnect.user.payload;

import com.diyconnect.city.City;
import com.diyconnect.city.CityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveNewUserRequest {
    private String username;
    private String email;
    private String password;
    private CityDTO city;
    private boolean admin;
}
