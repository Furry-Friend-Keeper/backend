package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.FavoriteDTO;
import com.example.furryfriendkeeper.dtos.OwnerDetailDTO;
import com.example.furryfriendkeeper.dtos.OwnerEditDTO;

import com.example.furryfriendkeeper.services.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping("/{petOwnerId}")
    private OwnerDetailDTO getOwnerDetail(@PathVariable Integer petOwnerId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return ownerService.getOwnerDetail(petOwnerId,token);
    }

    @PatchMapping("/{petOwnerId}")
    private String editPetOwner(@PathVariable Integer petOwnerId, @RequestBody OwnerEditDTO newOwner){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return ownerService.editOwner(newOwner,petOwnerId,token);
    }

    @PatchMapping("/{ownerId}/profile-img")
    public String uploadProfile(@PathVariable Integer ownerId, @RequestParam("file") MultipartFile file){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return ownerService.uploadProfile(ownerId,file,token);
    }

    @PutMapping("/favorite/{ownerId}")
    public String updateFavorite(@PathVariable Integer ownerId, @RequestBody FavoriteDTO favoriteDTO){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return ownerService.updateFavorite(ownerId,favoriteDTO,token);
    }
}
