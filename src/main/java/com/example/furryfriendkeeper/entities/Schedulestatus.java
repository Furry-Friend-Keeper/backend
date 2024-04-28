package com.example.furryfriendkeeper.entities;

import lombok.Setter;
import lombok.Getter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "schedulestatus")
public class Schedulestatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StatusId", nullable = false)
    private Integer id;

    @Column(name = "Status", nullable = false, length = 30)
    private String status;

    @OneToMany(mappedBy = "status")
    private Set<Petkeepernotification> petkeepernotifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "status")
    private Set<Petownernotification> petownernotifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "status")
    private Set<Appointmentschedule> appointmentschedules = new LinkedHashSet<>();


    @Override
    public String toString() {
        return status;
    }
}