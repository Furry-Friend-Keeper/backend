package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.ReviewDTO;
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

    public SaveReviewDTO saveReview(SaveReviewDTO newReview, String token) {

        token = token.replace("Bearer ", "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer checkOwnerId = ownerRepository.getPetownerIdByEmail(emailCheck);
//        System.out.println("PetKeeper : " + newReview.getPetkeeperId());
//        System.out.println("PetOwner : " + newReview.getPetownerId());
        Review checkReview = reviewRepository.findReviewByPetowner(newReview.getPetkeeperId(), newReview.getPetownerId());
//        System.out.println(checkReview);
        if (role.equals("Owner") && newReview.getPetownerId() == checkOwnerId) {
            if (checkReview == null) {
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
            } else
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have already reviewed this petkeeper.");
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission");
    }

    public String updateReview(SaveReviewDTO newReview, Integer reviewId, String token) {

        token = token.replace("Bearer ", "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer ownerId = ownerRepository.getPetownerIdByEmail(emailCheck);
        Review review = reviewRepository.getById(reviewId);
        if (review.equals(null)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no review!");
        }
        if (role.equals("Owner") && ownerId == review.getPetOwner().getId()) {
            review.setId(reviewId);
            if (newReview.getComment() != null) {
                review.setComment(newReview.getComment());
            }
            if (newReview.getStar() != null) {
                review.setStars(newReview.getStar());
            }
            review.setDate(newReview.getDate());


            reviewRepository.saveAndFlush(review);
            return "Update review successfully";
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission");
    }


    public void deleteReview(Integer reviewId, Integer ownerId, String token) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResponseStatusException
                (HttpStatus.NOT_FOUND, "This review id" + reviewId + "does not exist!"));
        token = token.replace("Bearer ", "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        if (role.equals("Owner")) {
            if (ownerId == review.getPetOwner().getId()) {
                reviewRepository.deleteById(reviewId);
            } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission");
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission");
    }

    public ReviewDTO checkIfReviewExist(Integer keeperId, Integer ownerId, String token) {
        token = token.replace("Bearer ", "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer checkOwnerId = ownerRepository.getPetownerIdByEmail(emailCheck);
        if (role.equals("Owner") && checkOwnerId == ownerId) {
            Review review = reviewRepository.findReviewByPetowner(keeperId, ownerId);
            if (review != null) {
                ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
                return reviewDTO;
            } else return null;

        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission");
    }
}
