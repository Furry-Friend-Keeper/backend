package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    @Query(value = "select Name from pets p where p.CategoriesId = ?1", nativeQuery = true)
    String CateName(Integer Id);
}
