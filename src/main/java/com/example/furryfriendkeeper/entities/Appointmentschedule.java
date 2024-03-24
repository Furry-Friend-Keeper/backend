package com.example.furryfriendkeeper.entities;

import lombok.Setter;
import lombok.Getter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "appointmentschedule")
public class Appointmentschedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AppointmentId", nullable = false)
    private Integer id;

    @Column(name = "OwnerPhone", nullable = false, length = 10)
    private String ownerPhone;

    @Column(name = "PetName", length = 200)
    private String petName;

    @Column(name = "Message", length = 200)
    private String message;

    @Column(name = "StartDate", nullable = false)
    private Instant startDate;

    @Column(name = "EndDate", nullable = false)
    private Instant endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PetOwnerId", nullable = false)
    private Petowner petOwner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PetKeeperId", nullable = false)
    private Petkeepers petKeeper;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CategoryId", nullable = false)
    private Pet category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "StatusId", nullable = false)
    private Schedulestatus status;


}