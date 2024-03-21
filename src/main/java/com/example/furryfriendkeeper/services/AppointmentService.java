package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.AppointmentDTO;
import com.example.furryfriendkeeper.dtos.AppointmentScheduleDTO;
import com.example.furryfriendkeeper.entities.Appointmentschedule;
import com.example.furryfriendkeeper.repositories.AppointmentScheduleRepository;
import com.example.furryfriendkeeper.utils.ListMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    @Autowired
    private AppointmentScheduleRepository appointmentScheduleRepository;

    private final ModelMapper modelMapper;

    private final ListMapper listMapper;


    public List<Appointmentschedule> getAllSchedule(){
        List<Appointmentschedule> appointmentschedule = appointmentScheduleRepository.findAll();
        return appointmentschedule;
    }

    public List<AppointmentScheduleDTO> getAllScheduleByPetkeeper(Integer petkeeperId){
        List<AppointmentScheduleDTO> listAppointment = appointmentScheduleRepository.getAppointmentByPetkeeper(petkeeperId);
        return listAppointment;
    }

    public List<AppointmentScheduleDTO> getAllScheduleByPetOwner(Integer petownerId){
        List<AppointmentScheduleDTO> listAppointment = appointmentScheduleRepository.getAppointmentByPetOwner(petownerId);
        return listAppointment;
    }

    @Transactional
    public AppointmentDTO createRequest(AppointmentDTO newAppointment){
        try {
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            Appointmentschedule appointmentschedule = modelMapper.map(newAppointment,Appointmentschedule.class);
            appointmentschedule.setId(null);
            appointmentScheduleRepository.saveAndFlush(appointmentschedule);
        }catch (Exception e){
            throw e;
        }

        return newAppointment;

    }
}
