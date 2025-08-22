package com.diyconnect.city;

import com.diyconnect.band.Band;
import com.diyconnect.user.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cities")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long city_id;

    private String name;
    private String state;
    private String country;

    @OneToMany(mappedBy = "city")
    @JsonIgnore
    private List<User> users;

    @OneToMany(mappedBy = "city")
    @JsonIgnore
    private List<Band> bands;

    @Override
    public String toString() {
        return "City{" +
                "city_id=" + city_id +
                ", name='" + name + '\'' +
                '}';
    }
}
