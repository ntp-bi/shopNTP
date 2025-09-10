package com.ntp.be.auth.controllers;

import com.ntp.be.auth.dto.ChangePasswordRequest;
import com.ntp.be.auth.services.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/api/ntpshop")
public class ChangePasswordController {
    @Autowired
    private ChangePasswordService changePasswordService;

    @PutMapping("/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        try {
            changePasswordService.changePassword(request, principal.getName());
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
