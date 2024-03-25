package com.example.furryfriendkeeper.entities;

import lombok.Setter;
import lombok.Getter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoriesId", nullable = false)
    private Integer id;

    @Column(name = "Name", nullable = false, length = 200)
    private String name;

    @OneToMany(mappedBy = "categories")
    private Set<Petcategory> petcategories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<Appointmentschedule> appointmentschedules = new LinkedHashSet<>();

    public Set<Appointmentschedule> getAppointmentschedules() {
        return appointmentschedules;
    }

    public void setAppointmentschedules(Set<Appointmentschedule> appointmentschedules) {
        this.appointmentschedules = appointmentschedules;
    }

    public Set<Petcategory> getPetcategories() {
        return petcategories;
    }

    public void setPetcategories(Set<Petcategory> petcategories) {
        this.petcategories = petcategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return name;
    }
}