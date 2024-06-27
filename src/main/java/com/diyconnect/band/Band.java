package com.diyconnect.band;

import com.diyconnect.city.City;
import com.diyconnect.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "bands")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Band {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long band_id;

    private String name;
    private String gender;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    private List<String> links;

}