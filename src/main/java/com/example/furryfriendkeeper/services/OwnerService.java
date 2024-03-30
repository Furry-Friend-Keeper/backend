package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.OwnerDetailDTO;
import com.example.furryfriendkeeper.dtos.OwnerEditDTO;
import com.example.furryfriendkeeper.entities.Address;
import com.example.furryfriendkeeper.entities.Petkeepers;
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
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

    @Autowired
    private final OwnerRepository ownerRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final FileService fileService;

    public List<Petowner> getAllOwners(){
        return ownerRepository.findAll();
    }

    public OwnerDetailDTO getOwnerDetail(Integer petOwnerId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer petOwner = ownerRepository.getPetownerIdByEmail(emailCheck);
        if(role.equals("Owner") && petOwner == petOwnerId) {
            Petowner owner = ownerRepository.getOwnerDetail(petOwnerId);
            OwnerDetailDTO ownerDetail = modelMapper.map(owner,OwnerDetailDTO.class);
            ownerDetail.setPetOwnerId(owner.getId());
            return ownerDetail;
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have permission.");
    }

    public String editOwner(OwnerEditDTO newPetOwner , Integer ownerId, String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer editOwnerId = ownerRepository.getPetownerIdByEmail(emailCheck);
        if(role.equals("Owner") && editOwnerId == ownerId) {
            Petowner petownerDetail = modelMapper.map(newPetOwner, Petowner.class);
            Petowner petowner = ownerRepository.findById(ownerId).map(oldDetail -> mapPetowner(oldDetail, petownerDetail)).orElseGet(() ->
            {
                petownerDetail.setId(ownerId);
                return petownerDetail;
            });
            ownerRepository.saveAndFlush(petowner);
        return "Updated Successfully";
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You dont have permission!");

    }

    private Petowner mapPetowner(Petowner oldDetail, Petowner newDetail){
        if(newDetail.getFirstname() != null) {
            oldDetail.setFirstname(newDetail.getFirstname());
        }
        if(newDetail.getLastname() != null) {
            oldDetail.setLastname(newDetail.getLastname());
        }
        if (newDetail.getPetname() != null) {
            oldDetail.setPetname(newDetail.getPetname());
        }
        if (newDetail.getPhone() != null) {
            oldDetail.setPhone(newDetail.getPhone());
        }

        return oldDetail;
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public String uploadProfile(Integer ownerId, MultipartFile file, String token){
        Petowner petowner = ownerRepository.getById(ownerId);
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer owner = ownerRepository.getPetownerIdByEmail(emailCheck);
        if(role.equals("Owner") && owner == ownerId) {
            if(fileService.isSupportedContentType(file.getContentType())) {
                try {
                    if (petowner.getImg() != null && file != null) {
                        boolean isImageExist = fileService.doesImageExist(petowner.getImg(),ownerId,role);
                        if (isImageExist) {
                            fileService.deleteProfileImg(petowner.getImg(),ownerId,role);
                        }
                    }
                    if (file == null || file.isEmpty()) {
                        petowner.setImg(null);
                        ownerRepository.saveAndFlush(petowner);
                    } else {
                        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                        fileService.store(file, ownerId,role);
                        petowner.setImg(fileName);
                        ownerRepository.saveAndFlush(petowner);
                    }
                    return "Upload Profile Succesfully!";

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Invalid file type(jpg,png and jpeg only),please try again");
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have permission!");
    }



}
