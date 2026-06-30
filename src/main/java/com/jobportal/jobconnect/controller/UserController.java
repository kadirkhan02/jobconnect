package com.jobportal.jobconnect.controller;

import com.jobportal.jobconnect.dto.request.RegisterRequestDTO;
import com.jobportal.jobconnect.dto.request.UpdateUserDTO;
import com.jobportal.jobconnect.dto.response.UserResponseDTO;
import com.jobportal.jobconnect.model.User;
import com.jobportal.jobconnect.response.ApiResponse;
import com.jobportal.jobconnect.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jobportal.jobconnect.dto.response.UserResponseDTO;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>>register(@Valid @RequestBody
    RegisterRequestDTO requestDTO)
    {
        log.info("User registered for {}",requestDTO.getEmail());
        UserResponseDTO userResponseDTO =userService.register(requestDTO);

        return ResponseEntity.status(201).body(ApiResponse.created(userResponseDTO,"Resgistration completed"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>>getAll()
    {
        log.info("User registered  ");
        List<UserResponseDTO>userResponseDTOS=userService.getAll();

        return ResponseEntity.ok(ApiResponse.success(userResponseDTOS,userResponseDTOS.size() + "User found|"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getById(@PathVariable int id)
    {
        log.info("Fetching user with id: {}", id);
        UserResponseDTO userResponseDTO=userService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(userResponseDTO,"User found with id "+id));
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getByEmail(
            @PathVariable String email) {
        log.info("Fetching user with email: {}", email);
        return ResponseEntity.ok(
                ApiResponse.success(userService.getByEmail(email), "User found!"));
    }
    @PutMapping("/{id}")
    public  ResponseEntity<ApiResponse<UserResponseDTO>> update(@PathVariable int id, @Valid @RequestBody UpdateUserDTO updateUserDTO)
    {
        log.info("Updating user with id: {}", id);
        UserResponseDTO userResponseDTO1= userService.update(id,updateUserDTO);
        return  ResponseEntity.status(201).body(ApiResponse.success(userResponseDTO1,"Updated Successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id)
    {
        log.info("Deleting user with id: {}", id);
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null,"Deleted successfully"));
    }
}
