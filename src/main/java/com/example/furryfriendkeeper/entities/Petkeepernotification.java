package com.example.furryfriendkeeper.entities;

import lombok.Setter;
import lombok.Getter;
import javax.persistence.*;


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


}