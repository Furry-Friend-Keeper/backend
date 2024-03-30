package com.example.furryfriendkeeper.repositories;


import com.example.furryfriendkeeper.dtos.FavoriteDTO;
import com.example.furryfriendkeeper.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Integer> {

    @Modifying
    @Query(value = "Delete from favorites f where f.PetOwnerId = :ownerId and f.PetKeeperId = :keeperId",nativeQuery = true)
    void deleteFavorite(@Param("ownerId")Integer ownerId, @Param("keeperId")Integer keeperId);

    @Query(value = "select * from favorites f where f.PetOwnerId = :ownerId and f.PetKeeperId = :keeperId ",nativeQuery = true)
    Favorite findFavorite(@Param("ownerId")Integer ownerId, @Param("keeperId")Integer keeperId);

    @Query(value = "select * from favorites f where f.PetOwnerId = ?1",nativeQuery = true)
    List<Favorite> findAllByPetOwnerId(Integer ownerId);
}
