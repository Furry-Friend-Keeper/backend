package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.AppointmentDTO;
import com.example.furryfriendkeeper.dtos.AppointmentScheduleDTO;
import com.example.furryfriendkeeper.entities.Appointmentschedule;
import com.example.furryfriendkeeper.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService service;


    @GetMapping("/owner/{petownerId}")
    private List<AppointmentScheduleDTO> getSchedulebyOwner(@PathVariable Integer petOwnerId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.getAllScheduleByPetOwner(petOwnerId,token);
    }
    @GetMapping("/keeper/{petkeeperId}")
    private List<AppointmentScheduleDTO> getSchedulebyKeeper(@PathVariable Integer petKeeperId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return service.getAllScheduleByPetkeeper(petKeeperId,token);
    }

    @PostMapping("/create")
    private AppointmentDTO createSchedule(@RequestBody AppointmentDTO newAppointment){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.createRequest(newAppointment,token);
    }


}
