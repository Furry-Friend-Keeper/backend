package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.Petkeepers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PetkeeperRepository extends JpaRepository<Petkeepers, Integer> {

    @Query(value= "select PetKeeperId from petkeepers where Email = :email",nativeQuery = true)
    Integer getPetkeepersIdByEmail(String email);
}
