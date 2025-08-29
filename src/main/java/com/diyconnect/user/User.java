package com.diyconnect.user;

import com.diyconnect.band.Band;
import com.diyconnect.city.City;
import com.diyconnect.message.Message;
import com.diyconnect.userRole.UserRole;
import com.diyconnect.verificationTokens.VerificationToken;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private boolean enabled;

    @PrePersist
    public void prePersistEnabled() {
        enabled = false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String bio;

    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER)
    @JsonManagedReference("userMessagesSentReference")
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    @JsonManagedReference("userMessagesReceivedReference")
    private List<Message> messagesReceived;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("userBandsReference")
    private List<Band> bands;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference("userUserRolesReference")
    private List<UserRole> userRoles;

    @Setter
    @Getter
    @Transient
    private boolean admin;

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, boolean admin) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public User(String username, String email, String password, List<UserRole> userRoles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRoles = userRoles;
    }

}
