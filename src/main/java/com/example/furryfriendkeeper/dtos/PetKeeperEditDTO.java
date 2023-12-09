package com.example.furryfriendkeeper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetKeeperEditDTO {

    private Integer id;
    private String name;
    private String contact;
    private String detail;
    private Integer phone;
    private AddressDTO address;
}
