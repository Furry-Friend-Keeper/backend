package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.entities.Disableappointmentschedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisableScheduleRepository extends JpaRepository<Disableappointmentschedule,Integer>{

    @Query(value = "select * from disableappointmentschedule d where d.PetKeeperId = ?1",nativeQuery = true)
    List<Disableappointmentschedule> getDisableScheduleByPetkeeper(Integer petKeeper);
}
