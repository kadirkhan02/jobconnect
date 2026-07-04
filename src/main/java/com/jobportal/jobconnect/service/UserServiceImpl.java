package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.RegisterRequestDTO;
import com.jobportal.jobconnect.dto.request.UpdateUserDTO;
import com.jobportal.jobconnect.dto.response.UserResponseDTO;
import com.jobportal.jobconnect.exception.DuplicateEmailException;
import com.jobportal.jobconnect.exception.ResourceNotFoundException;
import com.jobportal.jobconnect.model.User;
import com.jobportal.jobconnect.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;
    //private List<User> users = new ArrayList<>();
    private int nextId = 1;

    @Override
    public UserResponseDTO register(RegisterRequestDTO requestDTO) {


        if(userRepository.existsByEmail(requestDTO.getEmail())){
            throw new DuplicateEmailException(requestDTO.getEmail());}

        User user =modelMapper.map(requestDTO,User.class);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now().toString());

        User saveduser=userRepository.save(user);
        log.info("New user registered: {}",saveduser.getEmail());

        return modelMapper.map(saveduser,UserResponseDTO.class);

    }

    @Override
    public UserResponseDTO getById(int id) {

     User user=findUserById(id);

     return modelMapper.map(user,UserResponseDTO.class);

    }

    private User findUserById(int id)
    {
        return userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User",id));
    }

    @Override
    public UserResponseDTO getByEmail(String email) {

        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found"+email));

        return modelMapper.map(user,UserResponseDTO.class);
    }

    @Override
    public List<UserResponseDTO> getAll() {

        log.info("Fetching all users");
        return userRepository.findAll().stream().map(u->modelMapper
                .map(u,UserResponseDTO.class)).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO update(int id, UpdateUserDTO updateDTO) {

        User user=findUserById(id);

        if (updateDTO.getName()   != null) user.setName(updateDTO.getName());
        if (updateDTO.getPhone()  != null) user.setPhone(updateDTO.getPhone());
        if (updateDTO.getCity()   != null) user.setCity(updateDTO.getCity());
        if (updateDTO.getBio()    != null) user.setBio(updateDTO.getBio());
        if (updateDTO.getSkills() != null) user.setSkills(updateDTO.getSkills());

        User updateuser=userRepository.save(user);
        log.info("User is updated where ID: {}",id);
        return modelMapper.map(updateuser,UserResponseDTO.class);
    }

    @Override
    public void delete(int id) {

        User user=findUserById(id);

        user.setActive(false);
        userRepository.save(user);
        log.info("User deactivated with id: {}",id);

    }
}
