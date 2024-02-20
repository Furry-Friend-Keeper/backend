package com.example.furryfriendkeeper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetKeeperEditDTO {

    private Integer id;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200, message = "Name length cannot exceed 200 characters!!")
    private String name;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200, message = "Contact length cannot exceed 200 characters!!")
    private String contact;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200, message = "Detail length cannot exceed 200 characters!!")
    private String detail;
    @NotNull(message = "Please fill this field")
    @Pattern(regexp = "\\d{10}",message = "Phone numbers must be 10 digits(0-9) only.")
    private String phone;
    @NotNull(message = "Please fill this field")
    private AddressDTO address;
}
