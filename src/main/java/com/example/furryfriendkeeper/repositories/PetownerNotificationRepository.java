package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.Petkeepernotification;
import com.example.furryfriendkeeper.entities.Petownernotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PetownerNotificationRepository extends JpaRepository<Petownernotification,Integer> {
    @Query(value = "Update petownernotification p set ReadStatus = 1 where p.PetOwnerNotiId = :notiId",nativeQuery = true)
    void updatePetownerRead(@Param("notiId")Integer notiId);

    @Query(value = "Select * from petownernotification p where p.PetOwnerId = :ownerId",nativeQuery = true)
    List<Petownernotification> getAllNotiByOwnerId(@Param("ownerId")Integer ownerId);

}
