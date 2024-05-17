package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.NotificationUserDTO;
import com.example.furryfriendkeeper.dtos.ResponseMessage;
import com.example.furryfriendkeeper.entities.Petkeepernotification;
import com.example.furryfriendkeeper.entities.Petownernotification;
import com.example.furryfriendkeeper.jwt.JwtTokenUtil;
import com.example.furryfriendkeeper.repositories.OwnerRepository;
import com.example.furryfriendkeeper.repositories.PetkeeperNotificationRepository;
import com.example.furryfriendkeeper.repositories.PetkeeperRepository;
import com.example.furryfriendkeeper.repositories.PetownerNotificationRepository;
import com.example.furryfriendkeeper.utils.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.model.IModel;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class NotificationService {

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private PetkeeperNotificationRepository petkeeperNotificationRepository;
    @Autowired
    private PetownerNotificationRepository petownerNotificationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PetkeeperRepository petkeeperRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ListMapper listMapper;

    @Autowired
    public NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    public void sendRequestNotification(final String userId, ResponseMessage responseMessage) {

        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/private-notifications", responseMessage);
    }

    public void sendRequestCancelNotification(final String userId, ResponseMessage reponseMessage) {
        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/private-notifications", reponseMessage);
    }

    public void sendConfirmNotification(final String userId, ResponseMessage reponseMessage) {

        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/private-notifications", reponseMessage);
    }

    public void sendIncareNotification(final String userId, ResponseMessage reponseMessage) {

        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/private-notifications", reponseMessage);
    }

    public void sendKeeperCompleteNotification(final String userId, ResponseMessage reponseMessage) {
        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/private-notifications", reponseMessage);
    }

    public void sendOwnerCompleteNotification(final String userId, ResponseMessage reponseMessage) {
        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/private-notifications", reponseMessage);
    }

    @Transactional
    public void updateKeeperNotification(Integer keeperNotificationId, String token) {
        token = token.replace("Bearer ", "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        Petkeepernotification petkeepernotification = petkeeperNotificationRepository.getById(keeperNotificationId);
        if (petkeepernotification != null) {
            if (keeperId == petkeepernotification.getPetKeeper().getId()) {
                petkeeperNotificationRepository.updatePetkeeperRead(keeperNotificationId);
            } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission");
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there is no notification found");
    }

    @Transactional
    public void updateOwnerNotification(Integer ownerNotificationId, String token) {
        token = token.replace("Bearer ", "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        Integer ownerId = ownerRepository.getPetownerIdByEmail(emailCheck);
        Petownernotification petownernotification = petownerNotificationRepository.getById(ownerNotificationId);
        if (petownernotification != null) {
            if (ownerId == petownernotification.getPetOwner().getId()) {
                petownerNotificationRepository.updatePetownerRead(ownerNotificationId);
            } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission");

        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there is no notification found");
    }

    public List<NotificationUserDTO> getKeeperNoti(Integer keeperId, String token) {
        token = token.replace("Bearer ", "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        Integer checkKeeper = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        if (checkKeeper == keeperId) {
            List<Petkeepernotification> petkeepernotifications = petkeeperNotificationRepository.getAllNotiByKeeperId(keeperId);
            List<NotificationUserDTO> responseNoti = listMapper.mapList(petkeepernotifications, NotificationUserDTO.class, modelMapper);
            for (int i = 0; i < petkeepernotifications.size(); i++) {
                responseNoti.get(i).setName(petkeepernotifications.get(i).getPetKeeper().getName());
            }

            return responseNoti;
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission");
    }

    public List<NotificationUserDTO> getOwnerNoti(Integer ownerId, String token) {
        token = token.replace("Bearer ", "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        Integer checkOwner = ownerRepository.getPetownerIdByEmail(emailCheck);
        if (checkOwner == ownerId) {
            List<Petownernotification> petownernotifications = petownerNotificationRepository.getAllNotiByOwnerId(ownerId);
            List<NotificationUserDTO> responseNoti = listMapper.mapList(petownernotifications, NotificationUserDTO.class, modelMapper);
            for (int i = 0; i < petownernotifications.size(); i++) {
                String ownerName = petownernotifications.get(i).getPetOwner().getFirstname() + " " + petownernotifications.get(i).getPetOwner().getLastname();
                responseNoti.get(i).setName(ownerName);

            }

            return responseNoti;
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You dont have permission");
    }
}
