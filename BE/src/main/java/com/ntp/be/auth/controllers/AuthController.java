package com.ntp.be.auth.controllers;

import com.ntp.be.auth.config.JWTTokenHelper;
import com.ntp.be.auth.dto.LoginRequest;
import com.ntp.be.auth.dto.RegistrationRequest;
import com.ntp.be.auth.dto.RegistrationResponse;
import com.ntp.be.auth.dto.UserToken;
import com.ntp.be.auth.entities.User;
import com.ntp.be.auth.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request) {
        RegistrationResponse registrationResponse = registrationService.createUser(request);
        return new ResponseEntity<>(
                registrationResponse,
                registrationResponse.getCode() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> map) {
        String userName = map.get("userName");
        String code = map.get("code");

        User user = (User) userDetailsService.loadUserByUsername(userName);
        if (user != null && user.getVerificationCode().equals(code)) {
            registrationService.verifyUser(userName);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<UserToken> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Tạo đối tượng Authentication
            Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
            );
            // Xác thực
            Authentication authenticationResponse = this.authenticationManager.authenticate(authentication);

            if (authenticationResponse.isAuthenticated()) {
                User user = (User) authenticationResponse.getPrincipal();
                if (!user.isEnabled()) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                // Tạo JWT token
                String token = jwtTokenHelper.generateToken(user.getEmail());
                UserToken userToken = UserToken.builder().token(token).build();
                return new ResponseEntity<>(userToken, HttpStatus.OK);
            }

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
