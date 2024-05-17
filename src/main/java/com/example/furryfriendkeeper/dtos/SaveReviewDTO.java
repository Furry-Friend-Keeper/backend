package com.example.furryfriendkeeper.dtos;


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
    private String comment;
    @NotNull(message = "Please fill in date & time")
    private ZonedDateTime date;
    @NotNull(message = "Invalid rating star ,please try again!")
    @Size(min = 1, max = 5, message = "Invalid Stars amount,please try again!")
    private Integer star;
    @NotNull(message = "filt his field")
    private Integer petownerId;
    @NotNull(message = "filt his field")
    private Integer petkeeperId;
}
