package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.AppointmentDTO;
import com.example.furryfriendkeeper.dtos.AppointmentScheduleDTO;
import com.example.furryfriendkeeper.entities.Appointmentschedule;
import com.example.furryfriendkeeper.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService service;


    @GetMapping("/owner/{petownerId}")
    private List<AppointmentScheduleDTO> getSchedulebyOwner(@PathVariable Integer petOwnerId){
        return service.getAllScheduleByPetOwner(petOwnerId);
    }
    @GetMapping("/keeper/{petkeeperId}")
    private List<AppointmentScheduleDTO> getSchedulebyKeeper(@PathVariable Integer petKeeperId){
        return service.getAllScheduleByPetkeeper(petKeeperId);
    }

    @PostMapping("/create")
    private AppointmentDTO createSchedule(@RequestBody AppointmentDTO newAppointment){
        return service.createRequest(newAppointment);
    }


}
