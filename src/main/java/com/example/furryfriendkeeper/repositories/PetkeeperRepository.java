package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.Petkeepers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetkeeperRepository extends JpaRepository<Petkeepers, Integer> {
}
