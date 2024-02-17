package com.example.furryfriendkeeper.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveReviewDTO {

    private Integer reviewId;
    @NotNull
    private String comment;
    @NotNull
    private ZonedDateTime date;
    @NotNull
    @Size(min = 1,max = 5,message = "Invalid Stars amount,please try again!")
    private Integer star;
    private Integer petownerId;
    private Integer petkeeperId;
}
