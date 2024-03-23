package com.example.furryfriendkeeper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDetailDTO {
    private Integer petOwnerId;
    private String firstName;
    private String lastName;
    private String phone;
    private String img;
    private String petName;
    private String email;
}
