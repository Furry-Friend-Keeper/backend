package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.entities.Petowner;
import com.example.furryfriendkeeper.repositories.OwnerRepository;
import com.example.furryfriendkeeper.repositories.RoleRepository;
import com.example.furryfriendkeeper.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final UserRepository userRepository;
    @Autowired
    private final OwnerRepository ownerRepository;

    public List<Petowner> getAllOwners(){
        return ownerRepository.findAll();
    }

}
