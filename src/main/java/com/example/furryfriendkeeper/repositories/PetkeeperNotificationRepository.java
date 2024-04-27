package com.example.furryfriendkeeper.repositories;


import com.example.furryfriendkeeper.entities.Petkeepernotification;
import com.example.furryfriendkeeper.entities.Petkeepers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetkeeperNotificationRepository extends JpaRepository<Petkeepernotification,Integer> {

    @Query(value = "Update petkeepernotification p set ReadStatus = 1 where p.PetKeeperNotiId = :notiId",nativeQuery = true)
    void updatePetkeeperRead(@Param("notiId")Integer notiId);

    @Query(value = "Select * from petkeepernotification p where p.PetKeeperId = :keeperId",nativeQuery = true)
    List<Petkeepernotification> getAllNotiByKeeperId(@Param("keeperId")Integer keeperId);

    @Query(value = "Select * from petkeepernotification p where p.PetKeeperId = :keeperId And p.PetKeeperNotiId = :notiId",nativeQuery = true)
    Petkeepernotification getNotiByKeeperIdAndNotiId(@Param("keeperId")Integer keeperId ,@Param("notiId")Integer notiId);
}
