package com.example.furryfriendkeeper.services;


import com.example.furryfriendkeeper.dtos.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private SimpMessagingTemplate simpMessagingTemplate;

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
    public void sendIncareNotification(final String userId, String reponseMessage){
        ResponseMessage message = new ResponseMessage(reponseMessage);
        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",message);
    }
    public void sendKeeperCompleteNotification(final String userId, String reponseMessage){
        ResponseMessage message = new ResponseMessage(reponseMessage);
        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",message);
    }
    public void sendOwnerCompleteNotification(final String userId, String reponseMessage){
        ResponseMessage message = new ResponseMessage(reponseMessage);
        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",message);
    }


    public void updateKeeperNotification(Integer keeperNotificationId){

    }
    public void updateOwnerNotification(Integer ownerNotificationId){

    }
}
