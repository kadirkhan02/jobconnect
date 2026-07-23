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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@Tag(name="Auth" ,description = "Authentication APIs")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${jwt.expiration}")
    private int accessExpiration;
    @Value("${jwt.refresh-expiration}")
    private int refresehExpiration;

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
    public ResponseEntity<ApiResponse<AuthResponseDTO>>login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response)
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



        setAccessTokenCookie(response,token);
        setRefreshTokenCookie(response,refreshToken.getToken());
        AuthResponseDTO responseDTO=new AuthResponseDTO(
                null,
                null,
                "Bearer",
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getUsername(),
                userDetails.getUser().getRole()
        );
        log.info("User logged in: {}", responseDTO.getEmail());
        return  ResponseEntity.ok(ApiResponse.success(responseDTO,"Log succesfull"));




    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie=new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/v1/auth/refresh");
        cookie.setMaxAge(refresehExpiration/ 1000);
        response.addCookie(cookie);
    }

    private void setAccessTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie=new Cookie("accessToken",token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(accessExpiration/ 1000);
        response.addCookie(cookie);
    }

    @Operation(summary = "Get new access token using refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>>refresh(HttpServletRequest request,
                                                               HttpServletResponse response)
    {
        String refreshTokenvalue=getCookieValue(request,"refreshToken");

        if(refreshTokenvalue ==null)
            return ResponseEntity.status(401).body(ApiResponse.error("Refresh token not found",401));

        RefreshToken refreshToken=refreshTokenService.findByToken(refreshTokenvalue);

        refreshTokenService.verifyExpiration(refreshToken);

        UserDetailsImpl userDetails= (UserDetailsImpl) userDetailsServiceImp.loadUserByUsername(refreshToken.getUser().getEmail());

        String newAccessToken=jwtUtils.generateToken(userDetails);

        setAccessTokenCookie(response,newAccessToken);

        log.info("Token refreshed for: {}",
                refreshToken.getUser().getEmail());

        return ResponseEntity.ok(
                ApiResponse.success("Success!", "Token refreshed!"));
    }

    private String getCookieValue(HttpServletRequest request, String refreshToken) {

        if(request.getCookies()==null)
            return null;
        return Arrays.stream(request.getCookies())
                .filter(c->c.getName().equals(refreshToken))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(null);
    }

    @Operation(summary = "Logout — invalidate refresh token")
    @PostMapping("/logout/{userId}")
    public ResponseEntity<ApiResponse<Void>>logout( HttpServletRequest request,
                                                    HttpServletResponse response)
    {

        String getTokenvalue=getCookieValue(request,"refreshToken");

        if(getTokenvalue!=null)
        {
            try {
                RefreshToken refreshToken = refreshTokenService.findByToken(getTokenvalue);
                refreshTokenService.deleteByUser(refreshToken.getUser().getId());
                log.info("User logged out: {}", refreshToken.getUser().getId());
            }catch (Exception e)
            {
                log.warn("Refresh token not found during logout");
            }
        }
        clearCookie(response, "accessToken");
        clearCookie(response, "refreshToken");


        return ResponseEntity.ok(
                ApiResponse.success(null, "Logged out successfully!"));
    }

    private void clearCookie(HttpServletResponse response, String name) {

        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
