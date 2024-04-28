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

    @Query(value = "Select p from Petownernotification p" +
            " join fetch p.status s" +
            " join fetch p.petOwner po" +
            " where po.id = :ownerId")
    List<Petownernotification> getAllNotiByOwnerId(@Param("ownerId")Integer ownerId);

    @Query(value = "Select * from petkeepernotification p where p.PetOwnerId = :ownerId And p.PetOwnerNotiId = :notiId",nativeQuery = true)
    Petkeepernotification getNotiByOwnerIdAndNotiId(@Param("ownerId")Integer ownerId ,@Param("notiId")Integer notiId);

}
