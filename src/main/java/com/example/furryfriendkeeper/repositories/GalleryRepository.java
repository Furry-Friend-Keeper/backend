package com.example.furryfriendkeeper.repositories;


import com.example.furryfriendkeeper.entities.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery , Integer> {
    @Query("SELECT g.id AS id, g.gallery AS gallery FROM Gallery g WHERE g.petKeeper.id = :petkeeperId")
    List<String> findGalleriesByPetkeeperId(Integer petkeeperId);
}
