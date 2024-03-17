package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.AppointmentScheduleDTO;
import com.example.furryfriendkeeper.entities.Appointmentschedule;
import com.example.furryfriendkeeper.repositories.AppointmentScheduleRepository;
import com.example.furryfriendkeeper.utils.ListMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<AppointmentScheduleDTO> getAllScheduleByPetkeeperId(Integer petkeeperId){
        List<AppointmentScheduleDTO> listAppointment = appointmentScheduleRepository.getAppointmentByPetkeeper(petkeeperId);

        return listAppointment;
    }
}
