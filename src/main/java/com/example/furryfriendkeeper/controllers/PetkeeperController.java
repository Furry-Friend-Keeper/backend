package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.CategoriesDTO;
import com.example.furryfriendkeeper.dtos.PetKeeperEditDTO;
import com.example.furryfriendkeeper.dtos.PetkeeperDTO;
import com.example.furryfriendkeeper.dtos.PetkeeperDetailDTO;
import com.example.furryfriendkeeper.entities.Petkeepers;
import com.example.furryfriendkeeper.services.PetkeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
//@CrossOrigin(origins = "http://cp23at3.sit.kmutt.ac.th")
@CrossOrigin(origins = "*")
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
    @PatchMapping("/{keeperId}/profile-img")
    public String uploadProfile(@PathVariable Integer keeperId, @RequestParam("file") MultipartFile file){

        return service.uploadProfile(keeperId,file);
    }
    @PostMapping("/{keeperId}/gallery")
    public ResponseEntity<List<String>> uploadGallery(@PathVariable Integer keeperId, @RequestParam("file") List<MultipartFile> files){
        return service.uploadGallery(keeperId,files);
    }
    @PatchMapping("/{keeperId}/gallery")
    public String deleteGallery(@PathVariable Integer keeperId, @RequestParam("delete") List<String> delete,@RequestParam("file") List<MultipartFile> files){
        if(delete != null) {
            service.deleteGalley(keeperId, delete);
        }
        if (!files.get(0).isEmpty()) {
            service.uploadGallery(keeperId, files);
        }

        return "Update Succesfully!";
    }

}
