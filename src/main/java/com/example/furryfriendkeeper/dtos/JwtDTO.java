package com.example.furryfriendkeeper.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtDTO {
    private String accessToken;
    private String refreshToken;
    private String role;
    private Integer id;
    private String name;
    private Integer userId;
    private String img;
}
