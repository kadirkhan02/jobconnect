package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.RegisterRequestDTO;
import com.jobportal.jobconnect.dto.request.UpdateUserDTO;
import com.jobportal.jobconnect.dto.response.UserResponseDTO;
import com.jobportal.jobconnect.exception.DuplicateEmailException;
import com.jobportal.jobconnect.exception.ResourceNotFoundException;
import com.jobportal.jobconnect.model.User;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.lang.reflect.UndeclaredThrowableException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private ModelMapper modelMapper;
    private List<User> users = new ArrayList<>();
    private int nextId = 1;

    @Override
    public UserResponseDTO register(RegisterRequestDTO requestDTO) {

        boolean emailExists=users.stream()
                .anyMatch(u ->u.getEmail().equals(requestDTO.getEmail()));

        if(emailExists){
            throw new DuplicateEmailException(requestDTO.getEmail());}

        User user =modelMapper.map(requestDTO,User.class);
        user.setID(nextId++);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now().toString());

        users.add(user);
        log.info("New user registered: {}",user.getEmail());

        return modelMapper.map(user,UserResponseDTO.class);

    }

    @Override
    public UserResponseDTO getById(int id) {
     boolean ID= users.stream()
             .anyMatch(u -> u.getID() == id);

     if(!ID){
         throw new ResourceNotFoundException("ID not exists");
     }
     User user=findUserById(id);

     return modelMapper.map(user,UserResponseDTO.class);

    }

    private User findUserById(int id)
    {
        return users.stream()
                .filter(u -> u.getID()==id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User is not present with this id"));
    }

    @Override
    public UserResponseDTO getByEmail(String email) {

        User user = users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new ResourceAccessException("Not found user with this email"));


        return modelMapper.map(user,UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAll() {


        return users.stream()
                .map(u -> modelMapper.map(u,UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO update(int id, UpdateUserDTO updateDTO) {

        User user=findUserById(id);

        if (updateDTO.getName()   != null) user.setName(updateDTO.getName());
        if (updateDTO.getPhone()  != null) user.setPhone(updateDTO.getPhone());
        if (updateDTO.getCity()   != null) user.setCity(updateDTO.getCity());
        if (updateDTO.getBio()    != null) user.setBio(updateDTO.getBio());
        if (updateDTO.getSkills() != null) user.setSkills(updateDTO.getSkills());

        log.info("User is updated where ID: {}",id);
        return modelMapper.map(user,UserResponseDTO.class);
    }

    @Override
    public void delete(int id) {

        User user=findUserById(id);

        user.setActive(false);
        log.info("User deactivated with id: {}",id);

    }
}
