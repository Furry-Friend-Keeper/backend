package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.NotificationUserDTO;
import com.example.furryfriendkeeper.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/notification")
public class WebSocketController {

    @Autowired
    private NotificationService notificationService;


    @PatchMapping("/keeper/{notiId}")
    public void updateKeeperNoti(@PathVariable Integer notiId) {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        notificationService.updateKeeperNotification(notiId, token);
    }

    @PatchMapping("/owner/{notiId}")
    public void updateOwnerNoti(@PathVariable Integer notiId) {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        notificationService.updateOwnerNotification(notiId, token);
    }

    @GetMapping("/keeper/all/{keeperId}")
    public List<NotificationUserDTO> getAllKeeperNoti(@PathVariable Integer keeperId) {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return notificationService.getKeeperNoti(keeperId, token);
    }

    @GetMapping("/owner/all/{ownerId}")
    public List<NotificationUserDTO> getAllOwnerNoti(@PathVariable Integer ownerId) {
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return notificationService.getOwnerNoti(ownerId, token);
    }

}
