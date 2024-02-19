package com.example.furryfriendkeeper.dtos;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpPetkeeperDTO {
    private Integer petkeeperId;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200, message = "Name length cannot exceed 200 characters!!")
    private String name;
    @Size(max = 200, message = "Detail length cannot exceed 200 characters!!")
    private String detail;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200, message = "Contact length cannot exceed 200 characters!!")
    private String contact;
    @NotNull(message = "Please fill this field")
    @Size(min = 10, max = 10, message = "Invalid length of phone number!!")
    private String phone;
    @NotEmpty(message = "Please fill this field")
    private Set<Integer> categoryId;
    @NotEmpty(message = "Please fill this field")
    @Email(message = "Invalid email, Please try again")
    @Size(max = 200, message = "Email length cannot exceed 100 characters!!")
    private String email;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200,message = "Password cannot exceed 200 characters!!")
    private String password;
    @NotNull(message = "Please fill this field")
    @Positive(message = "Invalid role Id")
    private Integer role;
    @NotNull(message = "Please fill this field")
    private AddressDTO address;

}
