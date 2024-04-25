package com.example.furryfriendkeeper.entities;

import lombok.Setter;
import lombok.Getter;
import javax.persistence.*;
import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(name = "disableappointmentschedule")
public class Disableappointmentschedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DisableId", nullable = false)
    private Integer id;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PetKeeperId", nullable = false)
    private Petkeepers petKeeper;

    @Column(name = "Message",nullable = false)
    private String message;


}