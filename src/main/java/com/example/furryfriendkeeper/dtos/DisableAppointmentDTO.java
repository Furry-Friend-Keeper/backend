package com.example.furryfriendkeeper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisableAppointmentDTO {

    private Integer id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer petKeeperId;
}
