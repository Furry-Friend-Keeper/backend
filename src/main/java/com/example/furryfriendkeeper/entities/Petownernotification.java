package com.example.furryfriendkeeper.entities;

import lombok.Setter;
import lombok.Getter;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "petownernotification")
public class Petownernotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PetOwnerNotiId", nullable = false)
    private Integer id;

    @Column(name = "Message", length = 200)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "StatusId", nullable = false)
    private Schedulestatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PetOwnerId", nullable = false)
    private Petowner petOwner;

    @Column(name = "ReadStatus",nullable = false)
    @Min(value = 0, message = "ReadStatus must be either 0 or 1")
    @Max(value = 1, message = "ReadStatus must be either 0 or 1")
    private Boolean readStatus;

    @Column(name = "DateStart",nullable = false)
    private ZonedDateTime dateStart;


}