package com.diyconnect.band.payload;

import com.diyconnect.city.City;
import com.diyconnect.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BandDTO {
    private long band_id;
    private String name;
    private String genre;
    private String description;
    private User user;
    private City city;
    private String instagramLink;
    private String facebookLink;
    private String spotifyLink;
    private String bandcampLink;
    private String soundcloudLink;

}
