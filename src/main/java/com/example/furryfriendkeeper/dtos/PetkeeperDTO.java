package com.example.furryfriendkeeper.dtos;


import lombok.*;


import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetkeeperDTO {

    private Integer id;
    private String name;
    private Set<String> categories;
    private double reviewStars;
    private String img;
    private List<String> map;
    private String detail;
    private Boolean available;


}
