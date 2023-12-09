package com.example.furryfriendkeeper.entities;

import javax.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Setter;
import lombok.Getter;


@Entity
@Getter
@Setter
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




    public Integer getId(){
        return id;
    }
}