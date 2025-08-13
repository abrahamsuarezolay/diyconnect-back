package com.diyconnect.utils.mappers;

import com.diyconnect.band.Band;
import com.diyconnect.band.payload.BandDTO;
import com.diyconnect.city.City;
import com.diyconnect.city.CityDTO;
import com.diyconnect.user.User;
import com.diyconnect.user.payload.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class DTOMapper {

    public DTOMapper() {
    }

    public CityDTO cityToDTO(City city){
        return new CityDTO(city.getCity_id(), city.getName(), city.getUsers(), city.getBands());
    }

    public UserDTO userToDTO(User user){
        return new UserDTO(
                user.getUser_id(),
                user.getUsername(),
                user.getEmail(),
                user.getMessagesSent(),
                user.getMessagesReceived(),
                user.getCity(),
                user.getBands(),
                user.getUserRoles());
    }

    public List<UserDTO> ListUsersToDTO(List<User> users){

        List<UserDTO> usersDTO = new ArrayList<UserDTO>();

        for(User user : users){
            usersDTO.add(new UserDTO(
                    user.getUser_id(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getCity(),
                    user.getBands(),
                    user.getUserRoles()
            ));
        }

        return usersDTO;
    }


    public BandDTO bandToDTO(Band band){
        BandDTO bandDTO = new BandDTO(
                band.getBand_id(),
                band.getName(),
                band.getGenre(),
                band.getDescription(),
                band.getUser(),
                band.getCity(),
                band.getFacebookLink(),
                band.getInstagramLink(),
                band.getSpotifyLink(),
                band.getBandcampLink(),
                band.getSoundcloudLink()
        );
        return bandDTO;
    }

    public List<BandDTO> listBandToDTOs(List<Band> bands){

        List<BandDTO> bandDTOs = new ArrayList<BandDTO>();

        for(Band band : bands){
            bandDTOs.add(new BandDTO(
                    band.getBand_id(),
                    band.getName(),
                    band.getGenre(),
                    band.getDescription(),
                    band.getUser(),
                    band.getCity(),
                    band.getFacebookLink(),
                    band.getInstagramLink(),
                    band.getSpotifyLink(),
                    band.getBandcampLink(),
                    band.getSoundcloudLink()
            ));
        }

        return bandDTOs;
    }
}
