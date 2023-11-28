package com.example.furryfriendkeeper.dtos;

import com.example.furryfriendkeeper.repositories.ReviewRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetkeeperDTO {

    private Integer petkeeperId;
    private String name;
    private Set<String> Categories;
    private double reviewStars;
    private String Img;

}
