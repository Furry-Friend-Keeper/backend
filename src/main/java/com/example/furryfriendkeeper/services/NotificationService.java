package com.example.furryfriendkeeper.services;


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
    public NotificationService(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

//    public void notifyFrontend(String message){
//        ResponseMessage response = new ResponseMessage(message);
//
//        simpMessagingTemplate.convertAndSend("/topic/messages",response);
//    }
//
//    public void notifyUser(String message,String id){
//        ResponseMessage response = new ResponseMessage(message);
//
//        simpMessagingTemplate.convertAndSendToUser(id,"/topic/private-messages",response);
//    }
//
//
//    public void sendGlobalNotification(){
//        ResponseMessage message = new ResponseMessage("Global Notification");
//        simpMessagingTemplate.convertAndSend("/topic/global-notifications",message);
//    }
//
//    public void sendPrivateNotification(final String userId){
//        ResponseMessage message = new ResponseMessage("Private Notification");
//        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",message);
//    }

    public void sendRequestNotification(final String userId,ResponseMessage responseMessage){

        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",responseMessage);
    }
    public void sendRequestCancelNotification(final String userId, ResponseMessage reponseMessage){
        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",reponseMessage);
    }
    public void sendConfirmNotification(final String userId, ResponseMessage reponseMessage){

        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",reponseMessage);
    }
    public void sendIncareNotification(final String userId, ResponseMessage reponseMessage){

        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",reponseMessage);
    }
    public void sendKeeperCompleteNotification(final String userId, ResponseMessage reponseMessage){
        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",reponseMessage);
    }
    public void sendOwnerCompleteNotification(final String userId, ResponseMessage reponseMessage){
        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",reponseMessage);
    }


    public void updateKeeperNotification(Integer keeperNotificationId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        Integer keeperId = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        Petkeepernotification petkeepernotification = petkeeperNotificationRepository.getById(keeperNotificationId);
        if(petkeepernotification != null) {
            if(keeperId == petkeepernotification.getPetKeeper().getId()) {
                petkeeperNotificationRepository.updatePetkeeperRead(keeperNotificationId);
            }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You dont have permission");
        }else throw new ResponseStatusException(HttpStatus.NOT_FOUND,"there is no notification found");
    }
    public void updateOwnerNotification(Integer ownerNotificationId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        Integer ownerId = ownerRepository.getPetownerIdByEmail(emailCheck);
        Petownernotification petownernotification = petownerNotificationRepository.getById(ownerNotificationId);
        if(petownernotification != null) {
            if(ownerId == petownernotification.getPetOwner().getId()) {
                petownerNotificationRepository.updatePetownerRead(ownerNotificationId);
            }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You dont have permission");

        }else throw new ResponseStatusException(HttpStatus.NOT_FOUND,"there is no notification found");
    }

    public List<ResponseMessage> getKeeperNoti(Integer keeperId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        Integer checkKeeper = petkeeperRepository.getPetkeepersIdByEmail(emailCheck);
        if(checkKeeper == keeperId) {
            List<Petkeepernotification> petkeepernotifications = petkeeperNotificationRepository.getAllNotiByKeeperId(keeperId);
            List<ResponseMessage> responseNoti = listMapper.mapList(petkeepernotifications, ResponseMessage.class,modelMapper);
            return responseNoti;
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You dont have permission");
    }

    public List<ResponseMessage> getOwnerNoti(Integer ownerId,String token){
        token = token.replace("Bearer " , "");
        String emailCheck = jwtTokenUtil.getUsernameFromToken(token);
        Integer checkOwner = ownerRepository.getPetownerIdByEmail(emailCheck);
        if(checkOwner == ownerId) {
            List<Petownernotification> petownernotifications = petownerNotificationRepository.getAllNotiByOwnerId(ownerId);
            List<ResponseMessage> responseNoti = listMapper.mapList(petownernotifications,ResponseMessage.class,modelMapper);
            return responseNoti;
        }else throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You dont have permission");
    }
}
