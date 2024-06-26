package com.example.furryfriendkeeper.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDTO {

    private Integer id;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200, message = "Firstname length cannot exceed 200 characters!!")
    private String firstname;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200, message = "Lastname length cannot exceed 200 characters!!")
    private String lastname;
    @NotNull(message = "Please fill this field")
    @Pattern(regexp = "\\d{10}", message = "Phone numbers must be 10 digits(0-9) only.")
    private String phone;
    private String petname;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 100, message = "Email length cannot exceed 100 characters!!")
    private String email;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200, message = "Password length cannot exceed 100 characters!!")
    private String password;
    @NotNull(message = "Please fill this field")
    @Positive(message = "Invalid role Id")
    private Integer role;

}
