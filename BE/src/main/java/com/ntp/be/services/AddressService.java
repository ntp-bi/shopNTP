package com.ntp.be.services;

import com.ntp.be.auth.entities.User;
import com.ntp.be.dto.AddressRequest;
import com.ntp.be.entities.Address;
import com.ntp.be.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    public Address createAddress(AddressRequest addressRequest, Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        Address address = Address.builder()
                .name(addressRequest.getName())
                .street(addressRequest.getStreet())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .zipCode(addressRequest.getZipCode())
                .phoneNumber(addressRequest.getPhoneNumber())
                .user(user)
                .build();

        return addressRepository.save(address);
    }

    public void deleteAddress(UUID id) {
        addressRepository.deleteById(id);
    }
}
