package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.dtos.AppointmentScheduleDTO;
import com.example.furryfriendkeeper.entities.Appointmentschedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentScheduleRepository extends JpaRepository<Appointmentschedule,Integer> {


    @Query("SELECT a FROM Appointmentschedule a " +
        "JOIN FETCH a.petKeeper pk " +
        "JOIN FETCH a.petOwner po " +
        "JOIN FETCH a.category c " +
        "JOIN FETCH a.status s " +
        "WHERE pk.id = :petkeeperId")
    List<Appointmentschedule> getAppointmentByPetkeeper(@Param("petkeeperId") Integer petkeeperId);

//    List<AppointmentScheduleDTO> getAppointmentByPetkeeper(@Param("petkeeperId") Integer petkeeperId);
//       @Query(value = "SELECT a.AppointmentId, a.OwnerPhone, a.PetName, a.Message, a.StartDate, a.EndDate, " +
//        "a.PetOwner.firstname AS petOwnerFirstName, a.PetOwner.lastname AS petOwnerLastName, " +
//        "a.PetKeeper.name AS petKeeperName, a.CategoriesId AS categoryId, a.StatusId AS statusId " +
//        "FROM appointmentschedule a WHERE a.petKeeperId = :petkeeperId", nativeQuery = true)
//        List<Appointmentschedule> getAppointmentByPetkeeper(@Param("petkeeperId") Integer petkeeperId);



    @Query("SELECT a FROM Appointmentschedule a " +
            "JOIN FETCH a.petKeeper pk " +
            "JOIN FETCH a.petOwner po " +
            "JOIN FETCH a.category c " +
            "JOIN FETCH a.status s " +
            "WHERE po.id = :petownerId")
    List<Appointmentschedule> getAppointmentByPetOwner(@Param("petownerId") Integer petownerId);

    @Modifying
    @Query(value = "update appointmentschedule set StatusId = :status where AppointmentId = :appointmentId",nativeQuery = true)
    void updateStatus(@Param("status")Integer status,@Param("appointmentId")Integer Id);

    @Query(value = "select * from appointmentschedule a where a.AppointmentId = ?1",nativeQuery = true)
    Appointmentschedule getAppointmentscheduleById(Integer appointmentId);

    @Modifying
    @Query(value = "update appointmentschedule set Message = :message where AppointmentId = :appointmentId",nativeQuery = true)
    void updateMessage(@Param("message")String message,@Param("appointmentId")Integer appointmentId);
}
