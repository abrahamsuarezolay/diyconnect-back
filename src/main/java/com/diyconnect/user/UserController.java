package com.diyconnect.user;

import com.diyconnect.exception.cityException.CityException;
import com.diyconnect.exception.userException.NoUsersForCityException;
import com.diyconnect.exception.userException.UserException;
import com.diyconnect.exception.userException.UserNotFoundException;
import com.diyconnect.passwordResetTokens.PasswordResetToken;
import com.diyconnect.passwordResetTokens.PasswordResetTokenRepository;
import com.diyconnect.passwordResetTokens.payload.PasswordResetRequest;
import com.diyconnect.passwordResetTokens.payload.PasswordResetTokenDTO;
import com.diyconnect.user.payload.ModifyCityRequest;
import com.diyconnect.user.payload.SaveNewUserRequest;
import com.diyconnect.user.payload.UserDTO;
import com.diyconnect.utils.mappers.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private DTOMapper dtoMapper = new DTOMapper();

    @PostMapping("/savenewuser")
    public ResponseEntity<?> saveNewUser(@RequestBody SaveNewUserRequest user){
        try{
            User savedUser = new User(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.isAdmin()
            );

            userService.save(savedUser);

            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        }catch(UserException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findbyusername")
    public ResponseEntity<?> findByUsername(@RequestParam String username){
        try{
            User user = userService.findByUsername(username).get();

            UserDTO userDTO = dtoMapper.userToDTO(user);

            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);

        }catch(UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findbyemail")
    public ResponseEntity<?> findByEmail(@RequestParam String email) {
        try{
            User user = userService.findByEmail(email).get();

            UserDTO userDTO = dtoMapper.userToDTO(user);

            return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);

        }catch(UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findbycity")
    public ResponseEntity<?> findUsersByCityName(@RequestParam  String cityName){
        try{
            List<User> users = userService.findByCityName(cityName).get();
            List<UserDTO> usersDTO = dtoMapper.ListUsersToDTO(users);

            return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);

        }catch(NoUsersForCityException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findbystate")
    public ResponseEntity<?> findUsersByState(@RequestParam  String stateName){
        try{
            List<User> users = userService.findByCityState(stateName).get();
            List<UserDTO> usersDTO = dtoMapper.ListUsersToDTO(users);

            return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);

        }catch(NoUsersForCityException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findbycountry")
    public ResponseEntity<?> findUsersByCountry(@RequestParam String countryName){
        try{
            List<User> users = userService.findByCityCountry(countryName).get();
            List<UserDTO> usersDTO = dtoMapper.ListUsersToDTO(users);

            return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);

        }catch(NoUsersForCityException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findbycitystatecountry")
    public ResponseEntity<?> findUsersByCityStateCountry(
            @RequestParam String cityName,
            @RequestParam String stateName,
            @RequestParam String countryName)
    {
        try{
            List<User> users = userService.findByCityNameAndCityStateAndCityCountry(cityName, stateName, countryName).get();
            List<UserDTO> usersDTO = dtoMapper.ListUsersToDTO(users);

            return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);

        }catch(NoUsersForCityException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findbystatecountry")
    public ResponseEntity<?> findUsersByStateCountry(
            @RequestParam String stateName,
            @RequestParam String countryName)
    {
        try{
            List<User> users = userService.findByCityStateAndCityCountry(stateName, countryName).get();
            List<UserDTO> usersDTO = dtoMapper.ListUsersToDTO(users);

            return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);

        }catch(NoUsersForCityException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/modifyCity")
    public ResponseEntity<?> modifyCity(@RequestBody ModifyCityRequest request){
        try{
            User user = userService.modifyCity(request.getCityName(), request.getStateName(), request.getCountryName(), request.getUserEmail()).get();

            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (UserException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(CityException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findall")
    public ResponseEntity<?> findAllUsers(){
        List <User> users = userService.findAll();
        List<UserDTO> usersDto = dtoMapper.ListUsersToDTO(users);

        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }

    @GetMapping("/confirmregistration")
    public String confirmRegistration(@RequestParam("token") String token) {
        try {
            userService.activateUser(token);
            return "Account activated successfully!";
        } catch (Exception e) {
            return "Activation failed: " + e.getMessage();
        }
    }

    @PostMapping("/resetpasswordrequest")
    public ResponseEntity<?> resetPasswordRequest(@RequestParam("email") String userEmail) {
        try{
            Optional<User> user = userService.findByEmail(userEmail);
            String token = UUID.randomUUID().toString();

            //This service creates the password token and sends the email to the user.
            userService.createPasswordResetTokenForUser(user.get(), token);

            return new ResponseEntity<>("Reset password token generated", HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<?> verifyResetPasswordToken(@RequestParam("token") String token) {
        //Intermediate endpoint to verify the token exists and has not expired. If the token exists, the front end reads
        //the status 200 and redirects to the reset password form.
        try{
            Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);

            LocalDateTime date = passwordResetToken.get().getExpiryDate();

            if(date.isBefore(LocalDateTime.now().plusHours(25))){
                return new ResponseEntity<PasswordResetTokenDTO>(new PasswordResetTokenDTO(passwordResetToken.get().getToken()), HttpStatus.OK);
            }else{
                passwordResetTokenRepository.delete(passwordResetToken.get());
                return new ResponseEntity<>("Token expired", HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resetpasswordaction")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request){

        //Endpoint accessed by the reset password form.

        try{
            Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(request.getToken());
            User user = passwordResetToken.get().getUser();

            Optional<User> userMod = userService.resetPassword(
                    request.getEmail(),
                    request.getPassword(),
                    request.getConfirmPassword(),
                    passwordResetToken.get());

            if(!userMod.isEmpty()){
                return new ResponseEntity<>("Password updated successfully.", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Password not updated due to an error.", HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
