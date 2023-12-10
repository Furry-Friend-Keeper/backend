package com.example.furryfriendkeeper.repositories;


import com.example.furryfriendkeeper.entities.Petowner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Petowner,Integer> {
    @Query(value = "select PetOwnerId from Petowner where Email = :email",nativeQuery = true)
    Integer getPetownerIdByEmail(String email);
}
