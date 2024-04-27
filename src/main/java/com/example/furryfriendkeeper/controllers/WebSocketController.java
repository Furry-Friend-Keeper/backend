package com.example.furryfriendkeeper.controllers;

import com.example.furryfriendkeeper.dtos.Message;
import com.example.furryfriendkeeper.dtos.NotificationDTO;
import com.example.furryfriendkeeper.dtos.ResponseMessage;
import com.example.furryfriendkeeper.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/notification")
public class WebSocketController {
    
    @Autowired
    private NotificationService notificationService;


//    @PostMapping("/send-message")
//    public void sendMessage(@RequestBody Message message){
//        notificationService.notifyFrontend(message.getMessageContent());
//    }
//    @PostMapping("/send-private-message/{id}")
//    public void sendPrivateMessage(@RequestBody Message message,@PathVariable String id){
//        notificationService.sendPrivateNotification(id);
//        notificationService.notifyUser(message.getMessageContent(),id);
//    }
//
//    @MessageMapping("/message")
//    @SendTo("/topic/messages")
//    public ResponseMessage getMessage(Message message) throws InterruptedException{
//        notificationService.sendGlobalNotification();
//        Thread.sleep(1000);
//        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
//
//    }
//
//    @MessageMapping("/private-message")
//    @SendToUser("/topic/private-messages")
//    public ResponseMessage getPrivateMessage(Message message, Principal principal) throws InterruptedException{
//        Thread.sleep(1000);
//        return new ResponseMessage(HtmlUtils.htmlEscape("Sending private message to user" + principal.getName()+": "
//                + message.getMessageContent()));
//    }

    @PatchMapping("/keeper/{notiId}")
    public void updateKeeperNoti(@PathVariable Integer notiId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        notificationService.updateKeeperNotification(notiId,token);
    }

    @PatchMapping("/owner/{notiId}")
    public void updateOwnerNoti(@PathVariable Integer notiId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        notificationService.updateOwnerNotification(notiId,token);
    }

    @GetMapping("/keeper/all/{keeperId}")
    public ResponseMessage getAllKeeperNoti(@PathVariable Integer keeperId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return notificationService.getKeeperNoti(keeperId,token);
    }
    @GetMapping("/owner/all/{ownerId}")
    public ResponseMessage getAllOwnerNoti(@PathVariable Integer ownerId){
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

        return notificationService.getOwnerNoti(ownerId,token);
    }

}
