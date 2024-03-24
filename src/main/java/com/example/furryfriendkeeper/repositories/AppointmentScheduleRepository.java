package com.example.furryfriendkeeper.repositories;

import com.example.furryfriendkeeper.dtos.AppointmentScheduleDTO;
import com.example.furryfriendkeeper.entities.Appointmentschedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentScheduleRepository extends JpaRepository<Appointmentschedule,Integer> {

//    @Query(value = "select a.AppointmentId, o.Phone as ownerPhone o.PetName, a.Message, a.StartDate, a.EndDate, CONCAT(o.Firstname,' ',o.Lastname) as petOwner, p.Name as petKeeper , c.Name as category, s.Status  from appointmentschedule a " +
//            "join petkeepers p on a.PetKeeperId = p.PetKeeperId " +
//            "join petowner o on a.PetOwnerId = o.PetOwnerId join pets c on a.CategoryId = c.CategoriesId " +
//            "join schedulestatus s on a.StatusId = s.StatusId where a.PetkeeperId = ?1",nativeQuery = true)
//    @Query(value = "select a.id, o.phone as ownerPhone, o.petname, c.name as category, a.message , a.startDate, a.endDate , CONCAT(o.firstname, ' ', o.lastname) as petOwner, p.name as petKeeper , s.id  from Appointmentschedule a " +
//        "join Petkeepers p on a.petKeeper = p.id " +
//        "join Petowner o on a.petOwner = o.id join Pet c on a.category = c.id " +
//        "join Schedulestatus s on a.status = s.id where a.petKeeper = :petkeeperId", nativeQuery = false)
//    List<Appointmentschedule> getAppointmentByPetkeeper(@Param("petkeeperId")Integer petkeeperId);
    @Query(value = "SELECT a.AppointmentId, o.Phone, o.PetName, a.Message, a.StartDate, a.EndDate, CONCAT(o.Firstname,' ',o.Lastname) AS petOwner, p.Name AS petKeeper, c.Name AS category, s.Status " +
        "FROM appointmentschedule a " +
        "JOIN petkeepers p ON a.PetKeeperId = p.PetKeeperId " +
        "JOIN petowner o ON a.PetOwnerId = o.PetOwnerId " +
        "JOIN pets c ON a.CategoryId = c.CategoriesId " +
        "JOIN schedulestatus s ON a.StatusId = s.StatusId " +
        "WHERE a.PetKeeperId = :petkeeperId", nativeQuery = true)
    List<Appointmentschedule> getAppointmentByPetkeeper(@Param("petkeeperId") Integer petkeeperId);

    @Query(value = "select a.AppointmentId, o.Phone, o.PetName, a.Message, a.StartDate, a.EndDate, concat(o.Firstname,' ',o.Lastname) as petOwner, p.Name as petKeeper , c.Name as category, s.Status  from appointmentschedule a join petkeepers p on a.petkeeperId = p.petkeeperId " +
            "join petowner o on a.petownerId = o.petownerId join pets c on a.categoryId = c.categoriesId " +
            "join schedulestatus s on a.StatusId = s.StatusId where a.petownerId = ?1",nativeQuery = true)
    List<AppointmentScheduleDTO> getAppointmentByPetOwner(Integer petownerId);


}
