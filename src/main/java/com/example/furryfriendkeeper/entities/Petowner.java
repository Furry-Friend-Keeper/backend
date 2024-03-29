package com.example.furryfriendkeeper.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.Setter;
import lombok.Getter;


@Entity
@Getter
@Setter
@Table(name = "petowner")
public class Petowner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PetOwnerId", nullable = false)
    private Integer id;

    @Column(name = "Firstname", nullable = false, length = 200)
    private String firstname;

    @Column(name = "Lastname", nullable = false, length = 200)
    private String lastname;

    @Column(name = "Phone", nullable = false)
    private String phone;

    @Column(name = "Img", length = 100)
    private String img;

    @Column(name = "Petname", length = 200)
    private String petname;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Email", nullable = false, referencedColumnName = "Email")
    private User email;

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }

}