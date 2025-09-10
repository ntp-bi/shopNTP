package com.ntp.be.controllers;

import com.ntp.be.dto.AddressRequest;
import com.ntp.be.entities.Address;
import com.ntp.be.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/ntpshop")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<Address> createAddress(@RequestBody AddressRequest addressRequest, Principal principal) {
        Address address = addressService.createAddress(addressRequest, principal);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @DeleteMapping("/admin/address/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAddress(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/address")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Address>> getAllAddresses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(addressService.getAllAddress(pageable));
    }
}
