package com.example.furryfriendkeeper.controllers;



import com.example.furryfriendkeeper.dtos.SaveReviewDTO;
import com.example.furryfriendkeeper.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping("/save")
    public SaveReviewDTO saveReview(@RequestBody SaveReviewDTO saveReviewDTO){
        return service.saveReview(saveReviewDTO);
    }

}
