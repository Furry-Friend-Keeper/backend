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
public class ResponseMessage {

    private String message;
    private ZonedDateTime date;
    private Integer readStatus;
    private String name;
    private String status;


}


