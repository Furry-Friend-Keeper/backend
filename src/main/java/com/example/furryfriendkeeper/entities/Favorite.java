package com.example.furryfriendkeeper.entities;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FavoriteId", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PetKeeperId")
    private Petkeepers petKeeper;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PetOwnerId", nullable = false)
    private Petowner petOwner;


}