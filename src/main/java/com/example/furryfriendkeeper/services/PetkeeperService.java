package com.example.furryfriendkeeper.services;

import com.example.furryfriendkeeper.dtos.PetkeeperDTO;
import com.example.furryfriendkeeper.dtos.PetkeeperDetailDTO;
import com.example.furryfriendkeeper.entities.Petkeeper;
import com.example.furryfriendkeeper.repositories.*;
import com.example.furryfriendkeeper.utils.ListMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public List<PetkeeperDTO> getPetkeeperList(){
        List<Petkeeper> petkeeperList = petkeeperRepository.findAll();
        List<PetkeeperDTO> keepers = listMapper.mapList(petkeeperList, PetkeeperDTO.class, modelMapper);


        for(int i = 0; i < petkeeperList.size();i++){
            Set<String> categories = new LinkedHashSet<>();
            List<Integer> petcats = categoriesRepository.FindKeeperCategories(petkeeperList.get(i).getId());
            Double avgStars = reviewRepository.avgStars(petkeeperList.get(i).getId());
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
       Optional<Petkeeper> petkeeperDetails = petkeeperRepository.findById(petkeepersId);
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
    public Petkeeper save(PetkeeperDetailDTO newPetkeeper){
        return petkeeperRepository.saveAndFlush(modelMapper.map(newPetkeeper, Petkeeper.class));
    }
}
