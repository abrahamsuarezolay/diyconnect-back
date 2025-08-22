package com.diyconnect.user.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyCityRequest {
    private String cityName;
    private String stateName;
    private String countryName;
    private String userEmail;


}
