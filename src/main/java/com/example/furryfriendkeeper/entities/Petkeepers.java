package com.example.furryfriendkeeper.entities;

import javax.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Setter;
import lombok.Getter;



@Entity
@Getter
@Setter
@Table(name = "petkeepers")
public class Petkeepers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PetKeeperId", nullable = false)
    private Integer id;

    @Column(name = "Name", nullable = false, length = 200)
    private String name;

    @Column(name = "Contact", length = 200)
    private String contact;

    @Column(name = "Detail", length = 45)
    private String detail;

    @Column(name = "Img", length = 256)
    private String img;

    @Column(name = "Available", nullable = false)
    private Integer available;

    @Column(name = "Phone", nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Email", nullable = false, referencedColumnName = "Email")
    private User email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "AddressId", nullable = false)
    private Address address;

    @JsonManagedReference
    @OneToMany(mappedBy = "petKeeper")
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "petKeeper")
    private Set<Gallery> galleries = new LinkedHashSet<>();

    @OneToMany(mappedBy = "petKeeper")
    private Set<Petcategory> petcategories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "petKeeper")
    private Set<Favorite> favorites = new LinkedHashSet<>();

    @OneToMany(mappedBy = "petKeeper")
    private Set<Reportsdetail> reportsdetails = new LinkedHashSet<>();

    @Column(name = "ClosedDay",length = 200)
    private String closedDay;

    @Override
    public String toString() {
        return name;
    }

}