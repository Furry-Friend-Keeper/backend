package com.example.furryfriendkeeper.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "address")
public class Address {
    @Id
    @Column(name = "AddressId", nullable = false)
    private Integer id;

    @Column(name = "Address", nullable = false, length = 200)
    private String address;

    @Column(name = "District", length = 50)
    private String district;

    @Column(name = "Province", length = 50)
    private String province;

    @Column(name = "PostalCode", length = 10)
    private String postalCode;

    @Column(name = "Map", nullable = false, length = 256)
    private String map;


}