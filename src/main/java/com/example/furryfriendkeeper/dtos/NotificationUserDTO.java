package com.example.furryfriendkeeper.dtos;

import com.example.furryfriendkeeper.entities.Schedulestatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor

public class NotificationUserDTO {

    private String message;
    private ZonedDateTime date;
    private Integer readStatus;
    private String name;
    private String status;

    public NotificationUserDTO(String message, ZonedDateTime date, Integer readStatus, String name, String status) {
        this.message = message;
        this.date = date;
        this.readStatus = readStatus;
        this.name = name;
        this.status = status;
    }
}
