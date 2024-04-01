package com.example.furryfriendkeeper.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Integer reviewId;
    private String comment;
    private ZonedDateTime date;
    private Integer stars;
    private Integer petownerId;
    private String petownerFirstname;
    private String petownerLastname;
    private String petownerImg;
    private Integer petKeeperId;

}
