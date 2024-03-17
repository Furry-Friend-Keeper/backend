package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.dtos.AppointmentScheduleDTO;
import com.example.furryfriendkeeper.entities.Appointmentschedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentScheduleRepository extends JpaRepository<Appointmentschedule,Integer> {

    @Query(value = "select a.AppointmentId, o.OwnerPhone, o.PetName, a.Message, a.StartDate, a.EndDate, concat(o.Firstname,' ',Lastname) as petOwner, p.Name as petKeeper , c.Name as category  from appointmentschedule a join petkeepers p on a.petkeeperId = p.petkeeperId " +
            "join petowners o on a.petownerId = o.petownerId join pets c on a.categoryId = c.categoriesId where a.petkeeperId = ?1",nativeQuery = true)
    List<AppointmentScheduleDTO> getAppointmentByPetkeeper(Integer petkeeperId);


}
