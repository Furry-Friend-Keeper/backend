package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.OwnerDTO;
import com.example.furryfriendkeeper.entities.Pet;
import com.example.furryfriendkeeper.entities.Petowner;
import com.example.furryfriendkeeper.entities.Role;
import com.example.furryfriendkeeper.entities.User;
import com.example.furryfriendkeeper.repositories.OwnerRepository;
import com.example.furryfriendkeeper.repositories.RoleRepository;
import com.example.furryfriendkeeper.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    public List<Petowner> getAllOwners(){
        return ownerRepository.findAll();
    }

}
