package com.example.furryfriendkeeper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerEditDTO {

    private Integer petOwnerId;
    @Size(max = 200, message = "Firstname length cannot exceed 200 characters!!")
    private String firstName;
    @Size(max = 200, message = "Lastname length cannot exceed 200 characters!!")
    private String lastName;
    @Pattern(regexp = "\\d{10}", message = "Phone numbers must be 10 digits(0-9) only.")
    private String phone;
    private String img;
    @Size(max = 200, message = "Lastname length cannot exceed 200 characters!!")
    private String petName;
    private String email;

}
