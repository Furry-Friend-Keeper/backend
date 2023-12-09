package com.example.furryfriendkeeper.entities;

import javax.persistence.*;
import lombok.Setter;
import lombok.Getter;


@Entity
@Getter
@Setter
@Table(name = "petcategories")
public class Petcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PetCategoriesId", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PetKeeperId")
    private Petkeepers petKeeper;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CategoriesId", nullable = false)
    private Pet categories;

    public Petcategory() {
    }
    public Petcategory(Integer petKeeperId, Pet categories) {
        this.petKeeper = new Petkeepers();
        this.petKeeper.setId(petKeeperId);
        this.categories = categories;
    }

}