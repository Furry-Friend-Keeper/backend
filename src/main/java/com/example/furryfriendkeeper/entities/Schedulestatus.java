package com.example.furryfriendkeeper.entities;

import javax.persistence.*;

@Entity
@Table(name = "schedulestatus")
public class Schedulestatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StatusId", nullable = false)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}