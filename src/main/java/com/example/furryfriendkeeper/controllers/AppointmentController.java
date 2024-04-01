package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.AppointmentDTO;
import com.example.furryfriendkeeper.dtos.AppointmentScheduleDTO;
import com.example.furryfriendkeeper.dtos.DisableAppointmentDTO;
import com.example.furryfriendkeeper.dtos.ReviewDTO;
import com.example.furryfriendkeeper.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService service;


    @GetMapping("/owner/{petownerId}")
    private List<AppointmentScheduleDTO> getSchedulebyOwner(@PathVariable Integer petownerId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.getAllScheduleByPetOwner(petownerId,token);
    }
    @GetMapping("/keeper/{petkeeperId}")
    private List<AppointmentScheduleDTO> getSchedulebyKeeper(@PathVariable Integer petkeeperId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return service.getAllScheduleByPetkeeper(petkeeperId,token);
    }

    @PostMapping("/create")
    private AppointmentDTO createSchedule(@Valid @RequestBody AppointmentDTO newAppointment){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.createRequest(newAppointment,token);
    }

    @PatchMapping("/confirm/{appointmentId}")
    private String confirmAppointment(@PathVariable Integer appointmentId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return service.confirmAppointment(appointmentId,token);
    }

    @PatchMapping("/cancel/{appointmentId}")
    private String cancelAppointment(@PathVariable Integer appointmentId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.cancelAppointment(appointmentId,token);
    }
    @PatchMapping("/in-care/{appointmentId}")
    private String inCareAppointment(@PathVariable Integer appointmentId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.inCareAppointment(appointmentId,token);
    }

    @PatchMapping("/keeper-completed/{appointmentId}")
    private String keeperCompletedAppointment(@PathVariable Integer appointmentId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.keeperCompletedAppointment(appointmentId,token);
    }
    @PatchMapping("/owner-completed/{appointmentId}")
    private ReviewDTO ownerCompletedAppointment(@PathVariable Integer appointmentId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.ownerCompletedAppointment(appointmentId,token);
    }
    @PostMapping("/disable-schedule/{petkeeperId}")
    private DisableAppointmentDTO addDisableSchedule(@PathVariable Integer petkeeperId,@RequestBody @Valid DisableAppointmentDTO disableSchedule){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.addDisableAppointment(petkeeperId,disableSchedule,token);
    }

    @DeleteMapping("/disable-schedule/{disableId}")
    private String deleteDisableSchedule(@PathVariable Integer disableId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return service.deleteDisableAppointment(disableId,token);
    }

}
