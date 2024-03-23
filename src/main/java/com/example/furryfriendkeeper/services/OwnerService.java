package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.OwnerDetailDTO;
import com.example.furryfriendkeeper.entities.Petowner;
import com.example.furryfriendkeeper.jwt.JwtTokenUtil;
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
    private final OwnerRepository ownerRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    public List<Petowner> getAllOwners(){
        return ownerRepository.findAll();
    }

    public OwnerDetailDTO getOwnerDetail(Integer petOwnerId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer petOwner = ownerRepository.getPetownerIdByEmail(emailCheck);
        if(role.equals("Owner") && petOwner == petOwnerId) {
            return ownerRepository.getOwnerDetail(petOwnerId);
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have permission.");
    }

}
