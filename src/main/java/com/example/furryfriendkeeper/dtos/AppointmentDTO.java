package com.example.furryfriendkeeper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Integer appointmentId;
    @NotEmpty(message = "Please fill this field")
    @Pattern(regexp = "\\d{10}",message = "Phone numbers must be 10 digits(0-9) only.")
    private String ownerPhone;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200, message = "Email length cannot exceed 100 characters!!")
    private String petName;
    @NotEmpty(message = "Please fill this field")
    @Size(max = 200, message = "Email length cannot exceed 100 characters!!")
    private String message;
    @NotNull(message = "Please fill this field")
    private ZonedDateTime startDate;
    @NotNull(message = "Please fill this field")
    private ZonedDateTime endDate;
    @NotNull(message = "Please fill this field")
    private Integer petOwnerId;
    @NotNull(message = "Please fill this field")
    private Integer petKeeperId;
    @NotNull(message = "Please fill this field")
    private Integer categoryId;
    private Integer statusId;


}
