package com.example.furryfriendkeeper.repositories;


import com.example.furryfriendkeeper.entities.Petkeepernotification;
import com.example.furryfriendkeeper.entities.Petkeepers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetkeeperNotificationRepository extends JpaRepository<Petkeepernotification,Integer> {

//    @Query(value = "select * from Petkeepernotification p where p.PetKeeperId = :keeperId",nativeQuery = true)
//    List<Petkeepernotification> findAllByPet
}
