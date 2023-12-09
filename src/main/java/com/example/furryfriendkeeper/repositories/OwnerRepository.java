package com.example.furryfriendkeeper.repositories;


import com.example.furryfriendkeeper.entities.Petowner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Petowner,Integer> {
}
