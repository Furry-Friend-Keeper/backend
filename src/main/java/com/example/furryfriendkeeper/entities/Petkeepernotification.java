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
@Table(name = "petkeepernotification")
public class Petkeepernotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PetKeeperNotiId", nullable = false)
    private Integer id;

    @Column(name = "Message", length = 200)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "StatusId", nullable = false)
    private Schedulestatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PetKeeperId", nullable = false)
    private Petkeepers petKeeper;

    @Column(name = "ReadStatus",nullable = false)
    @Min(value = 0, message = "ReadStatus must be either 0 or 1")
    @Max(value = 1, message = "ReadStatus must be either 0 or 1")
    private Integer readStatus;

    @Column(name = "DateStart",nullable = false)
    private ZonedDateTime dateStart;


}