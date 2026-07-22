package com.jobportal.jobconnect.controller;


import com.jobportal.jobconnect.Security.JwtUtils;
import com.jobportal.jobconnect.Security.UserDetailsImpl;
import com.jobportal.jobconnect.Security.UserDetailsServiceImp;
import com.jobportal.jobconnect.dto.request.LoginRequestDTO;
import com.jobportal.jobconnect.dto.request.RefreshRequestDTO;
import com.jobportal.jobconnect.dto.request.RegisterRequestDTO;
import com.jobportal.jobconnect.dto.response.AuthResponseDTO;
import com.jobportal.jobconnect.dto.response.UserResponseDTO;
import com.jobportal.jobconnect.model.RefreshToken;
import com.jobportal.jobconnect.repository.UserRepository;
import com.jobportal.jobconnect.response.ApiResponse;
import com.jobportal.jobconnect.service.RefreshTokenService;
import com.jobportal.jobconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Tag(name="Auth" ,description = "Authentication APIs")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceImp userDetailsServiceImp;

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO requestDTO)
    {
        UserResponseDTO userResponseDTO=userService.register(requestDTO);
        log.info("Register request: {}", userResponseDTO.getEmail());
        return ResponseEntity.status(201).body(
                ApiResponse.created(userResponseDTO,"Registration completed")
        );
    }
    @Operation(summary = "Login and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>>login(@Valid @RequestBody LoginRequestDTO loginRequestDTO)
    {
        log.info("Login request: {}", loginRequestDTO.getEmail());
        Authentication auth =authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),loginRequestDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetailsImpl userDetails= (UserDetailsImpl) auth.getPrincipal();

        String token= jwtUtils.generateToken(userDetails);

        RefreshToken refreshToken=refreshTokenService.createRefreshToken(userDetails.getId());

        AuthResponseDTO responseDTO=new AuthResponseDTO(
                token,
                refreshToken.getToken(),
                "Bearer",
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getUsername(),
                userDetails.getUser().getRole()
        );
        log.info("User logged in: {}", responseDTO.getEmail());
        return  ResponseEntity.ok(ApiResponse.success(responseDTO,"Log succesfull"));




    }
    @Operation(summary = "Get new access token using refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDTO>>refresh(@Valid @RequestBody RefreshRequestDTO refreshRequestDTO)
    {
        RefreshToken refreshToken=refreshTokenService.findByToken(refreshRequestDTO.getRefreshToken());


        refreshTokenService.verifyExpiration(refreshToken);

        UserDetailsImpl userDetails= (UserDetailsImpl) userDetailsServiceImp.loadUserByUsername(refreshToken.getUser().getEmail());

        String newAccessToken=jwtUtils.generateToken(userDetails);

        AuthResponseDTO responseDTO=new AuthResponseDTO(
                newAccessToken,
                refreshRequestDTO.getRefreshToken(),
                "Bearer",
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getUsername(),
                userDetails.getUser().getRole()

        );
        log.info("Token refreshed for: {}",
                refreshToken.getUser().getEmail());
        return ResponseEntity.ok(
                ApiResponse.success(responseDTO, "Token refreshed!"));
    }
    @Operation(summary = "Logout — invalidate refresh token")
    @PostMapping("/logout/{userId}")
    public ResponseEntity<ApiResponse<Void>>logout(@PathVariable int userId)
    {
        refreshTokenService.deleteByUser(userId);
        log.info("User logged out: {}", userId);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Logged out successfully!"));
    }

}
