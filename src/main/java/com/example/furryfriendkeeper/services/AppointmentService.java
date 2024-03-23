package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.AppointmentDTO;
import com.example.furryfriendkeeper.dtos.AppointmentScheduleDTO;
import com.example.furryfriendkeeper.entities.*;
import com.example.furryfriendkeeper.jwt.JwtTokenUtil;
import com.example.furryfriendkeeper.repositories.*;
import com.example.furryfriendkeeper.utils.ListMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    @Autowired
    private AppointmentScheduleRepository appointmentScheduleRepository;

    private final ModelMapper modelMapper;

    private final ListMapper listMapper;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private final OwnerRepository ownerRepository;

    private final DisableScheduleRepository disableScheduleRepository;

    private final CategoriesRepository categoriesRepository;

    private final PetkeeperRepository petkeeperRepository;


    public List<Appointmentschedule> getAllSchedule(){
        List<Appointmentschedule> appointmentschedule = appointmentScheduleRepository.findAll();
        return appointmentschedule;
    }

    public List<AppointmentScheduleDTO> getAllScheduleByPetkeeper(Integer petkeeperId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer petkeeper = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        if(role.equals("PetKeeper") && petkeeper == petkeeperId) {
            List<AppointmentScheduleDTO> listAppointment = appointmentScheduleRepository.getAppointmentByPetkeeper(petkeeperId);
            return listAppointment;
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission.");
    }

    public List<AppointmentScheduleDTO> getAllScheduleByPetOwner(Integer petownerId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);

        List<AppointmentScheduleDTO> listAppointment = appointmentScheduleRepository.getAppointmentByPetOwner(petownerId);
        return listAppointment;
    }

    @Transactional
    public AppointmentDTO createRequest(AppointmentDTO newAppointment,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        String role = userRepository.findRole(emailCheck);
        Integer ownerId = ownerRepository.getPetownerIdByEmail(emailCheck);
        List<Integer> checkCategories = categoriesRepository.FindKeeperCategories(newAppointment.getPetKeeperId());
        Petkeepers checkKeeper = petkeeperRepository.getById(newAppointment.getPetKeeperId());
        Petowner checkOwner = ownerRepository.getById(newAppointment.getPetOwnerId());
        if(checkKeeper == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is no Petkeeper.");
        }
        if(checkOwner == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is no Petowner.");
        }
        if(newAppointment.getEndDate().isBefore(newAppointment.getStartDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End Date Cannot be before Start Date.");
        }
        if(newAppointment.getStartDate().isBefore(ZonedDateTime.now())){

        }
        if(!checkCategories.contains(newAppointment.getCategoryId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Category,Please try again.");
        }

        if(role.equals("Owner") && ownerId == newAppointment.getPetOwnerId()) {
            List<Disableappointmentschedule> checkDate = disableScheduleRepository.getDisableScheduleByPetkeeper(newAppointment.getPetKeeperId());
            LocalDate startDateAppointment = LocalDate.from(newAppointment.getStartDate().toInstant());
            LocalDate endDateAppointment = LocalDate.from(newAppointment.getEndDate().toInstant());
            for (Disableappointmentschedule checkDate1: checkDate) {
                if((startDateAppointment.isBefore(checkDate1.getStartDate()) && endDateAppointment.isBefore(checkDate1.getStartDate()))
                        || startDateAppointment.isAfter(checkDate1.getEndDate())) {
                    //No Overlap
                }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot make appointment at these period.");
            }         

                try {
                    newAppointment.setStatusId(1);
                    modelMapper.getConfiguration().setAmbiguityIgnored(true);
                    Appointmentschedule appointmentschedule = modelMapper.map(newAppointment, Appointmentschedule.class);
                    appointmentschedule.setId(null);
                    appointmentScheduleRepository.saveAndFlush(appointmentschedule);
                } catch (Exception e) {
                    throw e;
                }

        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have permission.");

        return newAppointment;

    }




}
