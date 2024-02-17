package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.SaveReviewDTO;
import com.example.furryfriendkeeper.entities.Petowner;
import com.example.furryfriendkeeper.entities.Review;
import com.example.furryfriendkeeper.repositories.OwnerRepository;
import com.example.furryfriendkeeper.repositories.PetkeeperRepository;
import com.example.furryfriendkeeper.repositories.ReviewRepository;
import com.example.furryfriendkeeper.utils.ListMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @Autowired
    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final ReviewRepository repository;

    private final PetkeeperRepository petkeeperRepository;

    private final OwnerRepository ownerRepository;
    public SaveReviewDTO saveReview(SaveReviewDTO newReview){
//        Review review = modelMapper.map(newReview, Review.class);
//        modelMapper.createTypeMap(SaveReviewDTO.class, Review.class).addMappings(mapper -> {
//            mapper.skip(Review::setId);
//            mapper.map(SaveReviewDTO::getPetownerId, Review :: setPetOwner);
//            mapper.map(SaveReviewDTO::getPetkeeperId,Review :: setPetKeeper);
//        });
        Review review = new Review();
        review.setId(null);
        review.setPetKeeper(petkeeperRepository.getById(newReview.getPetkeeperId()));
        review.setPetOwner(ownerRepository.getById(newReview.getPetownerId()));
        review.setComment(newReview.getComment());
        review.setStars(newReview.getStar());
        review.setDate(newReview.getDate());
        repository.saveAndFlush(review);
        newReview.setReviewId(review.getId());
        return newReview;
    }
}
