package com.example.furryfriendkeeper.entities;

import javax.persistence.*;
import lombok.Setter;
import lombok.Getter;


@Entity
@Getter
@Setter
@Table(name = "gallery")
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GalleryId", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PetKeeperId")
    private Petkeeper petKeeper;

    @Column(name = "Gallery", length = 256)
    private String gallery;

}