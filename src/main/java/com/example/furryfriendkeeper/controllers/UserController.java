package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.JwtDTO;
import com.example.furryfriendkeeper.dtos.MatchUserDTO;
import com.example.furryfriendkeeper.dtos.OwnerDTO;
import com.example.furryfriendkeeper.dtos.SignUpPetkeeperDTO;
import com.example.furryfriendkeeper.entities.Petowner;
import com.example.furryfriendkeeper.entities.Role;
import com.example.furryfriendkeeper.entities.User;
import com.example.furryfriendkeeper.services.OwnerService;
import com.example.furryfriendkeeper.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://cp23at3.sit.kmutt.ac.th")
//@CrossOrigin(origins = "*")
@RequestMapping( "/api/users")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private OwnerService ownerService;


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JwtDTO> match(@Valid @RequestBody MatchUserDTO user){

//        System.out.println(user.getEmail() + " " + user.getPassword());
        return service.match(user);
    }

    @PostMapping("/sign-up/keeper")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpPetkeeperDTO createPetkeeper(@Valid @RequestBody SignUpPetkeeperDTO newPetkeeper){
        return service.signUpPetkeeper(newPetkeeper);
    }

    @GetMapping("/AllRoles")
    public List<Role> allRole(){
        return service.AllRole();
    }

    @PostMapping("/sign-up/owner")
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerDTO createPetOwner(@Valid @RequestBody OwnerDTO newOwner){
        return service.sighUpPetOwner(newOwner);
    }

    @GetMapping("/owner/all")
    public List<Petowner> getAllOwners(){
        return ownerService.getAllOwners();
    }
}
