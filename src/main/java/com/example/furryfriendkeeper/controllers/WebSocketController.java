package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.Message;
import com.example.furryfriendkeeper.dtos.ResponseMessage;
import com.example.furryfriendkeeper.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@RestController
public class WebSocketController {
    
    @Autowired
    private NotificationService notificationService;

    
    @PostMapping("/send-message")
    public void sendMessage(@RequestBody Message message){
        notificationService.notifyFrontend(message.getMessageContent());
    }
    @PostMapping("/send-private-message/{id}")
    public void sendPrivateMessage(@RequestBody Message message,@PathVariable String id){
        notificationService.sendPrivateNotification(id);
        notificationService.notifyUser(message.getMessageContent(),id);
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(Message message) throws InterruptedException{
        notificationService.sendGlobalNotification();
        Thread.sleep(1000);
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));

    }

    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public ResponseMessage getPrivateMessage(Message message, Principal principal) throws InterruptedException{
        Thread.sleep(1000);
        return new ResponseMessage(HtmlUtils.htmlEscape("Sending private message to user" + principal.getName()+": "
                + message.getMessageContent()));
    }


}
