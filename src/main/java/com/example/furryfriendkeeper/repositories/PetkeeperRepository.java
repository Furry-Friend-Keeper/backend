package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.Petkeepers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PetkeeperRepository extends JpaRepository<Petkeepers, Integer> {

    @Query(value= "select PetKeeperId from petkeepers where Email = :email",nativeQuery = true)
    Integer getPetkeepersIdByEmail(String email);

    @Query(value= "select Email from petkeepers where PetKeeperId = :id",nativeQuery = true)
    String getPetkeeperEmailById(Integer id);

    @Modifying
    @Query(value = "update petkeepers set ClosedDay = :closedDay where PetKeeperId = :petkeeperId",nativeQuery = true)
    void updateClosedDay(@Param("closedDay")String closedDay, @Param("petkeeperId")Integer petkeeperId);

    @Modifying
    @Query(value = "update petkeepers set Available = :available where PetKeeperId = :petkeeperId",nativeQuery = true)
    void updateAvailable(@Param("available")Integer available,@Param("petkeeperId")Integer petkeeperId);
}
