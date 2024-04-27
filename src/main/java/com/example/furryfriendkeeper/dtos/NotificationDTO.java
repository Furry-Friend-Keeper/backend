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
public class NotificationDTO {
    private Integer id;
    private String message;
    private Integer statusId;
    private Integer petkeeperId;
    private Integer readStatus;
    private ZonedDateTime dateStart;

}
