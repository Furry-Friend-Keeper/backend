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
public class AppointmentScheduleDTO {

    private Integer id;
    private String ownerPhone;
    private String petName;
    private String category;
    private String message;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String petOwner;
    private Integer keeperId;
    private String petKeeper;
    private String keeperImg;
    private String keeperPhone;
    private String status;

}
