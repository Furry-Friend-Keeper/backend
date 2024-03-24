package com.example.furryfriendkeeper.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentScheduleDTO {

    private Integer appointmentId;
    private String ownerPhone;
    private String petName;
    private String category;
    private String message;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String petOwner;
    private String petKeeper;
    private String status;

}
