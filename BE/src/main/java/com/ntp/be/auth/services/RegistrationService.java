package com.ntp.be.auth.services;

import com.ntp.be.auth.dto.RegistrationRequest;
import com.ntp.be.auth.dto.RegistrationResponse;
import com.ntp.be.auth.entities.User;
import com.ntp.be.auth.helper.VerificationCodeGenerator;
import com.ntp.be.auth.repositories.UserDetailRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public RegistrationResponse createUser(RegistrationRequest registrationRequest) {
        User existing = userDetailRepository.findByEmail(registrationRequest.getEmail());

        if (existing != null) {
            return RegistrationResponse.builder().code(400).message("Email already exists!").build();
        }

        try {
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setFirstName(registrationRequest.getFirstName());
            user.setLastName(registrationRequest.getLastName());
            user.setEnabled(false);
            user.setProvider("manual");
            user.setPhoneNumber(registrationRequest.getPhoneNumber());

            String code = VerificationCodeGenerator.generateCode();
            user.setVerificationCode(code);
            user.setAuthorities(authorityService.getUserAuthorities());
            userDetailRepository.save(user);

            emailService.sendMail(user);

            return RegistrationResponse.builder().code(200).message("Registration successful").build();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e.getCause());
        }
    }

    public void verifyUser(String userName) {
        User user = userDetailRepository.findByEmail(userName);
        if (user == null) {
            throw new ServiceException("User not found: " + userName);
        }
        user.setEnabled(true);
        userDetailRepository.save(user);
    }
}
