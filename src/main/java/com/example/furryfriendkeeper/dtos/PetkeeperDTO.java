package com.example.furryfriendkeeper.dtos;

import com.example.furryfriendkeeper.repositories.ReviewRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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
    private Boolean available;

}
