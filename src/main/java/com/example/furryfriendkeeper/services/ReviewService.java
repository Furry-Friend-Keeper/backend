package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.SaveReviewDTO;
import com.example.furryfriendkeeper.entities.Review;
import com.example.furryfriendkeeper.jwt.JwtTokenUtil;
import com.example.furryfriendkeeper.repositories.OwnerRepository;
import com.example.furryfriendkeeper.repositories.PetkeeperRepository;
import com.example.furryfriendkeeper.repositories.ReviewRepository;
import com.example.furryfriendkeeper.repositories.UserRepository;
import com.example.furryfriendkeeper.utils.ListMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @Autowired
    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final ReviewRepository repository;

    private final PetkeeperRepository petkeeperRepository;

    private final OwnerRepository ownerRepository;

    private final ReviewRepository reviewRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

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

    public String updateReview(SaveReviewDTO newReview, Integer reviewId){

        Review review = reviewRepository.getById(reviewId);
        review.setId(reviewId);
//        review.setPetOwner(ownerRepository.getById(newReview.getPetownerId()));
//        review.setPetKeeper(petkeeperRepository.getById(newReview.getPetkeeperId()));
        review.setComment(newReview.getComment());
        review.setStars(newReview.getStar());
        review.setDate(newReview.getDate());
        reviewRepository.saveAndFlush(review);
        return "Update review successfully";
    }


    public Review mapReview(Review oldReview,Review newReview){

        if(!oldReview.getStars().equals(oldReview.getStars())){
            oldReview.setStars(newReview.getStars());
        }
        if(!oldReview.getComment().equals(oldReview.getComment())){
            oldReview.setComment(newReview.getComment());
        }
        oldReview.setDate(newReview.getDate());
        return oldReview;
    }

    public void deleteReview(Integer reviewId,Integer ownerId,String token){
        Review review = reviewRepository.findById(reviewId).orElseThrow(()-> new ResponseStatusException
                (HttpStatus.NOT_FOUND, "This review id" + reviewId + "does not exist!"));
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        if(role == "Owner") {
            if (ownerId == review.getPetOwner().getId()) {
                reviewRepository.deleteById(reviewId);
            }else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission");
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission");
    }
}
