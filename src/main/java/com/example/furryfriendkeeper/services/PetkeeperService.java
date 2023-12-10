package com.example.furryfriendkeeper.services;

import com.example.furryfriendkeeper.dtos.*;
import com.example.furryfriendkeeper.entities.Address;
import com.example.furryfriendkeeper.entities.Gallery;
import com.example.furryfriendkeeper.entities.Pet;
import com.example.furryfriendkeeper.entities.Petkeepers;
import com.example.furryfriendkeeper.repositories.*;
import com.example.furryfriendkeeper.utils.ListMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PetkeeperService {

    @Autowired
    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final PetkeeperRepository petkeeperRepository;

    private final PetRepository petRepository;

    private final CategoriesRepository categoriesRepository;

    private final ReviewRepository reviewRepository;

    private final GalleryRepository galleryRepository;

    private final FileService fileService;
    @Autowired
    private AddressRepository addressRepository;


    public List<PetkeeperDTO> getPetkeeperList(){
        List<Petkeepers> petkeepersList = petkeeperRepository.findAll();
        List<PetkeeperDTO> keepers = listMapper.mapList(petkeepersList, PetkeeperDTO.class, modelMapper);


        for(int i = 0; i < petkeepersList.size(); i++){
            Set<String> categories = new LinkedHashSet<>();
            List<Integer> petcats = categoriesRepository.FindKeeperCategories(petkeepersList.get(i).getId());
            Double avgStars = reviewRepository.avgStars(petkeepersList.get(i).getId());
            if(avgStars == null){
                avgStars = 0.0;
            }
            keepers.get(i).setReviewStars(avgStars);

            for(int a = 0; a < petcats.size(); a++){
                String name = petRepository.CateName(petcats.get(a));
                categories.add(name);
                keepers.get(i).setCategories(categories);

            }

        }


        return keepers;
    }

    public PetkeeperDetailDTO getPetkeeperDetails(Integer petkeepersId){
       Optional<Petkeepers> petkeeperDetails = petkeeperRepository.findById(petkeepersId);
       List<String> galleries = galleryRepository.findGalleriesByPetkeeperId(petkeepersId);
       PetkeeperDetailDTO petkeeperDetailDTO = modelMapper.map(petkeeperDetails, PetkeeperDetailDTO.class);
       Double avgReviewStar = reviewRepository.avgStars(petkeepersId);
       List<String> categories = categoriesRepository.FindCategoriesByPetkeeperId(petkeepersId);
        if(avgReviewStar == null){
            avgReviewStar = 0.0;
        }
       petkeeperDetailDTO.setGallery(galleries);
       petkeeperDetailDTO.setReviewStars(avgReviewStar);
       petkeeperDetailDTO.setCategories(categories);
       return petkeeperDetailDTO;

    }
    public Petkeepers save(PetkeeperDetailDTO newPetkeeper){
        return petkeeperRepository.saveAndFlush(modelMapper.map(newPetkeeper, Petkeepers.class));
    }

    public List<CategoriesDTO> AllCategories(){
        List<Pet> pet = petRepository.findAll();
        return listMapper.mapList(pet, CategoriesDTO.class, modelMapper);
    }

    @Transactional(rollbackOn = Exception.class)
    public Petkeepers updatePetkeeper(PetKeeperEditDTO updatePetkeepers, Integer petkeeperId){
        try{
            Petkeepers petkeeperDetail = modelMapper.map(updatePetkeepers, Petkeepers.class);
        Petkeepers petkeepers = petkeeperRepository.findById(petkeeperId).map(oldDetail -> mapPetkeeper(oldDetail, petkeeperDetail)).orElseGet(()->
                {
                    petkeeperDetail.setId(petkeeperId);
                    return petkeeperDetail;
                });

        if(updatePetkeepers.getAddress() != null){
            Address oldAddress = addressRepository.findAddressDetailByPetkeeperId(petkeeperId);
            mapAddress(oldAddress, updatePetkeepers);
            addressRepository.saveAndFlush(oldAddress);

        }

        return petkeeperRepository.saveAndFlush(petkeepers);
        }catch (Exception e){
            throw e;
        }
    }


    private Petkeepers mapPetkeeper(Petkeepers oldDetail, Petkeepers newDetail){
        if(!newDetail.getName().isEmpty()) {
            oldDetail.setName(newDetail.getName());
        }
        if(!newDetail.getContact().isEmpty()) {
            oldDetail.setContact(newDetail.getContact());
        }
        if (newDetail.getDetail() != null) {
            oldDetail.setDetail(newDetail.getDetail());
        }
        if (newDetail.getPhone() != null) {
            oldDetail.setPhone(newDetail.getPhone());
        }
        if (newDetail.getAvailable() != null) {
            oldDetail.setAvailable(newDetail.getAvailable());
        }
        return oldDetail;
    }
    public Address mapAddress(Address oldAddress, PetKeeperEditDTO newDetail){


            if(newDetail.getAddress().getAddress() != null){
                oldAddress.setAddress(newDetail.getAddress().getAddress());
            }
            if (newDetail.getAddress().getDistrict() != null){
                oldAddress.setDistrict(newDetail.getAddress().getDistrict());
            }
            if(newDetail.getAddress().getProvince() != null){
                oldAddress.setProvince(newDetail.getAddress().getProvince());
            }
            if(newDetail.getAddress().getPostalCode() != null){
                oldAddress.setPostalCode(newDetail.getAddress().getPostalCode());
            }
            if(newDetail.getAddress().getMap() != null){
                oldAddress.setMap(newDetail.getAddress().getMap());
            }
            return oldAddress;
    }

    @Transactional(rollbackOn = Exception.class)
    public String uploadProfile(Integer keeperId, MultipartFile file){

        Petkeepers petkeeper = modelMapper.map(petkeeperRepository.findById(keeperId), Petkeepers.class);
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            fileService.store(file, keeperId);
            petkeeper.setImg(fileName);

            petkeeperRepository.saveAndFlush(petkeeper);
            return "Upload Profile Succesfully!";
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public ResponseEntity<List<String>> uploadGallery(Integer keeperId, List<MultipartFile> files){
        try {
            Gallery gallery = new Gallery();
            Petkeepers petkeeper = modelMapper.map(petkeeperRepository.findById(keeperId),Petkeepers.class);
            gallery.setPetKeeper(petkeeper);
            for(MultipartFile file : files) {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                gallery.setGallery(fileName);
                galleryRepository.saveAndFlush(gallery);
            }

        } catch (Exception e){
            throw new RuntimeException("There is error",e);
        }


        return new ResponseEntity<>(fileService.storeMultiple(files,keeperId), HttpStatus.OK);
    }


}
