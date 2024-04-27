package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.ResponseMessage;
import com.example.furryfriendkeeper.entities.Petkeepernotification;
import com.example.furryfriendkeeper.entities.Petownernotification;
import com.example.furryfriendkeeper.repositories.PetkeeperNotificationRepository;
import com.example.furryfriendkeeper.repositories.PetownerNotificationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.model.IModel;

import java.util.List;

@Service
public class NotificationService {

    private SimpMessagingTemplate simpMessagingTemplate;


    private PetkeeperNotificationRepository petkeeperNotificationRepository;

    private PetownerNotificationRepository petownerNotificationRepository;

    private ModelMapper modelMapper;

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


    public void updateKeeperNotification(Integer keeperNotificationId){
        petkeeperNotificationRepository.updatePetkeeperRead(keeperNotificationId);
    }
    public void updateOwnerNotification(Integer ownerNotificationId){
        petownerNotificationRepository.updatePetownerRead(ownerNotificationId);
    }

    public ResponseMessage getKeeperNoti(Integer keeperId){
        List<Petkeepernotification> petkeepernotifications = petkeeperNotificationRepository.getAllNotiByKeeperId(keeperId);
        ResponseMessage responseNoti = modelMapper.map(petkeepernotifications,ResponseMessage.class);
        return responseNoti;
    }

    public ResponseMessage getOwnerNoti(Integer ownerId){
        List<Petownernotification> petownernotifications = petownerNotificationRepository.getAllNotiByOwnerId(ownerId);
        ResponseMessage responseNoti = modelMapper.map(petownernotifications,ResponseMessage.class);
        return responseNoti;
    }
}
