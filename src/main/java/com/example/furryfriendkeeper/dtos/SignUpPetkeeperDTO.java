package com.example.furryfriendkeeper.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpPetkeeperDTO {
    private Integer petkeeperId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String detail;
    @NotEmpty
    private String contact;
    @NotNull
    private Integer phone;
    @NotEmpty
    private Set<Integer> categoryId;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotNull
    @Positive
    private Integer role;
    @NotNull
    private AddressDTO address;

}
