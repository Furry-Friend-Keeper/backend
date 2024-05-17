package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.CategoriesDTO;
import com.example.furryfriendkeeper.dtos.PetKeeperEditDTO;
import com.example.furryfriendkeeper.dtos.PetkeeperDTO;
import com.example.furryfriendkeeper.dtos.PetkeeperDetailDTO;

import com.example.furryfriendkeeper.services.PetkeeperService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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
    public ResponseEntity<String> updatePetkeeper( @PathVariable Integer keeperId, @Valid @RequestBody PetKeeperEditDTO petkeepers){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        service.updatePetkeeper(petkeepers,keeperId,token);
        return ResponseEntity.ok("Petkeeper updated successfully");
    }

    @PatchMapping("/{keeperId}/profile-img")
    public String uploadProfile(@PathVariable Integer keeperId, @RequestParam("file") MultipartFile file){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return service.uploadProfile(keeperId,file,token);
    }
    @PostMapping("/{keeperId}/gallery")
    public ResponseEntity<List<String>> uploadGallery(@PathVariable Integer keeperId, @RequestParam("file") List<MultipartFile> files){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.uploadGallery(keeperId,files,token);
    }
    @PatchMapping("/{keeperId}/gallery")
    public String deleteGallery(@PathVariable Integer keeperId, @RequestParam("delete") List<String> delete,@RequestParam("file") List<MultipartFile> files){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        if(delete != null) {
            service.deleteGalley(keeperId, delete, token);
        }
        if (!files.get(0).isEmpty()) {
            service.uploadGallery(keeperId, files, token);
        }

        return "Update Succesfully!";
    }

    @PatchMapping("/closed/{keeperId}")
    public String updateClosedDay(@PathVariable Integer keeperId, @RequestBody List<String> closedDays){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return service.updateClosedDay(keeperId,closedDays,token);
    }

    @PatchMapping("/available/{keeperId}")
    public String updateAvailable(@PathVariable Integer keeperId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return service.closedPetkeeper(keeperId,token);
    }

}
