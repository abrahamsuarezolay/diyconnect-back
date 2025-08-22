package com.diyconnect.user;

import com.diyconnect.city.City;
import com.diyconnect.city.CityRepository;
import com.diyconnect.exception.userException.NoUsersForCityException;
import com.diyconnect.exception.userException.UserAlreadyExistsException;
import com.diyconnect.exception.userException.UserNotFoundException;
import com.diyconnect.passwordResetTokens.PasswordResetToken;
import com.diyconnect.passwordResetTokens.PasswordResetTokenRepository;
import com.diyconnect.role.Role;
import com.diyconnect.role.RoleRepository;
import com.diyconnect.userRole.UserRole;
import com.diyconnect.userRole.UserRoleRepository;
import com.diyconnect.verificationTokens.VerificationToken;
import com.diyconnect.verificationTokens.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public <S extends User> S save(S entity) {

        try {

            List<UserRole> roles = new ArrayList<UserRole>();

            //We add the role user to every new user
            Optional<Role> roleUser = roleRepository.findByName("ROLE_USER");
            roleUser.ifPresent(role -> roles.add(new UserRole(entity, roleUser.get())));

            //We add the role admin in case the flag is true
            if(entity.isAdmin()){
                Optional<Role> roleAdmin = roleRepository.findByName("ROLE_ADMIN");
                roleAdmin.ifPresent(role -> roles.add(new UserRole(entity, roleAdmin.get())));
            }

            //We encrypt the user password
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));

            //We save user Table
            entity.setUserRoles(roles);
            userRepository.save(entity);

            //We iterate over the list of userroles and save the userRole table
            roles.forEach(userRole -> {
                userRoleRepository.save(userRole);
            });

            // We save the verificationToken instance
            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = new VerificationToken(token, entity);
            tokenRepository.save(verificationToken);

            // We send the verification email
            sendVerificationEmail(entity.getEmail(), token);

            return entity;
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException();
        }
    }

    private void sendVerificationEmail(String email, String token) {
        String subject = "Complete Registration";
        String confirmationUrl = "http://localhost:8080/users/confirmregistration?token=" + token;
        String message = "Please click on the below link to activate your account:";

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject(subject);
        emailMessage.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(emailMessage);
    }

    private void sendResetPasswordEmail(String email, String token){
        String subject = "Reset your password";
        //Modify to actual url when app is going.
        String confirmationUrl = "http://localhost:5173/resetpassword/" + token;
        String message = "Please click on the below link to access the form to reset your password.";

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject(subject);
        emailMessage.setText(message + "\r\n" + confirmationUrl);
        mailSender.send(emailMessage);
    }

    public void activateUser(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);

        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token is invalid or has expired");
        }

        //Update of user to enabled
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        //Token delete after user activation
        tokenRepository.delete(verificationToken);
    }



    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken tokenObj = new PasswordResetToken(token, user, LocalDateTime.now().plusHours(24));
        passwordTokenRepository.save(tokenObj);

        sendResetPasswordEmail(user.getEmail(), tokenObj.getToken());
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return userRepository.saveAll(entities);
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return userRepository.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return userRepository.existsById(aLong);
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public Iterable<User> findAllById(Iterable<Long> longs) {
        return userRepository.findAllById(longs);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void deleteById(Long aLong) {
        userRepository.deleteById(aLong);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        userRepository.deleteAllById(longs);
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {
        userRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public Optional<List<User>> findByCityName(String cityName) {

        Optional<List<User>> usersQuery = userRepository.findByCityName(cityName);

        if (usersQuery.get().isEmpty()) {
            throw new NoUsersForCityException();
        } else {

            //We filter only the enabled users
            Optional<List<User>> users = Optional.of(usersQuery.get().stream().filter(user -> user.isEnabled()).toList());

            return users;
        }
    }

    @Override
    public Optional<List<User>> findByCityState(String stateName) {

        Optional<List<User>> usersQuery = userRepository.findByCityState(stateName);

        if (usersQuery.get().isEmpty()) {
            throw new NoUsersForCityException();
        } else {

            //We filter only the enabled users
            Optional<List<User>> users = Optional.of(usersQuery.get().stream().filter(user -> user.isEnabled()).toList());

            return users;
        }
    }

    @Override
    public Optional<List<User>> findByCityCountry(String countryName) {

        Optional<List<User>> usersQuery = userRepository.findByCityCountry(countryName);

        if (usersQuery.get().isEmpty()) {
            throw new NoUsersForCityException();
        } else {

            //We filter only the enabled users
            Optional<List<User>> users = Optional.of(usersQuery.get().stream().filter(user -> user.isEnabled()).toList());

            return users;
        }
    }

    @Override
    public Optional<List<User>> findByCityNameAndCityStateAndCityCountry(String cityName, String stateName, String countryName) {

        Optional<List<User>> usersQuery = userRepository.findByCityNameAndCityStateAndCityCountry(cityName, stateName, countryName);

        if (usersQuery.get().isEmpty()) {
            throw new NoUsersForCityException();
        } else {

            //We filter only the enabled users
            Optional<List<User>> users = Optional.of(usersQuery.get().stream().filter(user -> user.isEnabled()).toList());

            return users;
        }
    }

    @Override
    public Optional<List<User>> findByCityStateAndCityCountry(String stateName, String countryName) {

        Optional<List<User>> usersQuery = userRepository.findByCityStateAndCityCountry(stateName, countryName);

        if (usersQuery.get().isEmpty()) {
            throw new NoUsersForCityException();
        } else {

            //We filter only the enabled users
            Optional<List<User>> users = Optional.of(usersQuery.get().stream().filter(user -> user.isEnabled()).toList());

            return users;
        }
    }


    @Override
    public Optional<User> findByEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            return userRepository.findByEmail(email);
        }
    }

    public Optional<User> modifyCity(String cityName, String stateName, String countryName, String userEmail) {

        City cityToAdd = cityRepository.findFirstByNameAndStateAndCountry(cityName, stateName, countryName).get();
        User userToModify = userRepository.findByEmail(userEmail).get();

        userToModify.setCity(cityToAdd);

        userRepository.save(userToModify);
        return Optional.of(userToModify);
    }

    public Optional<User> resetPassword(String email, String newPassword, String confirmPassword, PasswordResetToken token){

        if(newPassword.equals(confirmPassword)) {
            User userToModify = userRepository.findByEmail(email).get();

            //We encode the password at saving.
            userToModify.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userToModify);

            //We delete the token
            passwordTokenRepository.delete(token);

            return Optional.of(userToModify);
        }
        else{
            return Optional.empty();
        }

    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
