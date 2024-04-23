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

    public void notifyFrontend(String message){
        ResponseMessage response = new ResponseMessage(message);

        simpMessagingTemplate.convertAndSend("/topic/messages",response);
    }

    public void notifyUser(String message,String id){
        ResponseMessage response = new ResponseMessage(message);

        simpMessagingTemplate.convertAndSendToUser(id,"/topic/private-messages",response);
    }


    public void sendGlobalNotification(){
        ResponseMessage message = new ResponseMessage("Global Notification");
        simpMessagingTemplate.convertAndSend("/topic/global-notifications",message);
    }
    public void sendPrivateNotification(final String userId){
        ResponseMessage message = new ResponseMessage("Private Notification");
        simpMessagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications",message);
    }
}
