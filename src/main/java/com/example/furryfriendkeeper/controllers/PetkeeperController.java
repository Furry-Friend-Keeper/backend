package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.CategoriesDTO;
import com.example.furryfriendkeeper.dtos.PetKeeperEditDTO;
import com.example.furryfriendkeeper.dtos.PetkeeperDTO;
import com.example.furryfriendkeeper.dtos.PetkeeperDetailDTO;
import com.example.furryfriendkeeper.entities.Petkeepers;
import com.example.furryfriendkeeper.services.PetkeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/categories/all")
    public List<CategoriesDTO> allCategories(){
       return service.AllCategories();
    }

    @PatchMapping("/{keeperId}")
    public ResponseEntity<String> updatePetkeeper(@PathVariable Integer keeperId, @RequestBody PetKeeperEditDTO petkeepers){
         service.updatePetkeeper(petkeepers,keeperId);
        return ResponseEntity.ok("Petkeeper updated successfully");
    }


}
