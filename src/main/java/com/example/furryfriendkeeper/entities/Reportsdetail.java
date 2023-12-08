package com.example.furryfriendkeeper.entities;

import javax.persistence.*;

import java.time.Instant;
import lombok.Setter;
import lombok.Getter;

@Entity
@Getter
@Setter
@Table(name = "reportsdetail")
public class Reportsdetail {
    @Id
    @Column(name = "ReportId", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PetKeeperId")
    private Petkeeper petKeeper;

    @Column(name = "Detail", length = 200)
    private String detail;

    @Convert(disableConversion = true)
    @Column(name = "ReportDate")
    private Instant reportDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PetOwnerId", nullable = false)
    private Petowner petOwner;


}