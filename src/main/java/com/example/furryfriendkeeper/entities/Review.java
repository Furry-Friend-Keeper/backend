package com.example.furryfriendkeeper.entities;

import javax.persistence.*;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Setter;
import lombok.Getter;

@Entity
@Getter
@Setter
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewId", nullable = false)
    private Integer id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PetKeeperId")
    private Petkeepers petKeeper;

    @Column(name = "Comment", length = 200)
    private String comment;

    @Convert(disableConversion = true)
    @Column(name = "Date")
    private ZonedDateTime date;

    @Column(name = "Stars")
    private Integer stars;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PetOwnerId", nullable = false)
    private Petowner petOwner;


}