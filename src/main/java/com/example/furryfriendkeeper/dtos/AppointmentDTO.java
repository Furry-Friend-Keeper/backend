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
public class AppointmentDTO {
    private Integer appointmentId;
    private String ownerPhone;
    private String petName;
    private String message;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private Integer petOwnerId;
    private Integer petKeeperId;
    private Integer categoryId;
    private Integer statusId;

}
