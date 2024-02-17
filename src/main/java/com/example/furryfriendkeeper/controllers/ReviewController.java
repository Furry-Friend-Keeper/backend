package com.example.furryfriendkeeper.controllers;



import com.example.furryfriendkeeper.dtos.SaveReviewDTO;
import com.example.furryfriendkeeper.entities.Review;
import com.example.furryfriendkeeper.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping("/save")
    public SaveReviewDTO saveReview(@RequestBody SaveReviewDTO saveReviewDTO){
        return service.saveReview(saveReviewDTO);
    }

    @PatchMapping("/edit/{reviewId}")
    public String updateReview(@RequestBody SaveReviewDTO updateReview, @PathVariable Integer reviewId){
        return service.updateReview(updateReview, reviewId);
    }

    @DeleteMapping("/delete/{reviewId}")
    public void deleteReview(@PathVariable Integer reviewId,@RequestParam Integer ownerId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        service.deleteReview(reviewId,ownerId,token);
    }
}
