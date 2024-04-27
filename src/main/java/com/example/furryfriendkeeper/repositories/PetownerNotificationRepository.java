package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.Petkeepernotification;
import com.example.furryfriendkeeper.entities.Petownernotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PetownerNotificationRepository extends JpaRepository<Petownernotification,Integer> {
}
