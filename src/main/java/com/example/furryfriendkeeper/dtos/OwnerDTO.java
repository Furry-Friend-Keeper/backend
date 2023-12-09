package com.example.furryfriendkeeper.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO {

    private Integer id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    @NotNull
    private Integer phone;
    private String petname;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotNull
    private Integer role;

}
