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
public class DisableDateDTO {

    private Integer id;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

}
