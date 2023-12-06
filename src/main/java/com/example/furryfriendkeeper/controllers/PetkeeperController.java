package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.PetkeeperDTO;
import com.example.furryfriendkeeper.dtos.PetkeeperDetailDTO;
import com.example.furryfriendkeeper.services.PetkeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://cp23at3.sit.kmutt.ac.th")
//@CrossOrigin(origins = "*")
@RequestMapping("/api/keepers")
public class PetkeeperController {
    @Autowired
    private PetkeeperService service;

    @GetMapping("/all")
    public List<PetkeeperDTO> getAllPetkeeper(){
        return service.getPetkeeperList();
    }

    @GetMapping("/{keeperId}")
    public PetkeeperDetailDTO getPetkeeperDetail(@PathVariable Integer keeperId){
        return service.getPetkeeperDetails(keeperId);
    }
}
