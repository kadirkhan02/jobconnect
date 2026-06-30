package com.jobportal.jobconnect.service;

import com.jobportal.jobconnect.dto.request.RegisterRequestDTO;
import com.jobportal.jobconnect.dto.request.UpdateUserDTO;
import com.jobportal.jobconnect.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO register(RegisterRequestDTO registerDTO);
    UserResponseDTO getById(int id);
    UserResponseDTO getByEmail(String email);
    List<UserResponseDTO> getAll();
    UserResponseDTO update(int id, UpdateUserDTO updateDTO);
    void delete(int id);

}
