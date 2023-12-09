package com.example.furryfriendkeeper.dtos;

import com.example.furryfriendkeeper.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Integer id;
    private String address;
    private String district;
    private String province;
    private String postalCode;
    private String map;

}
