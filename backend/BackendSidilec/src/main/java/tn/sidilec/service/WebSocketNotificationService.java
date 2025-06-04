package tn.sidilec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyControleurs(String message) {
        messagingTemplate.convertAndSend("/topic/notifications/controleur", message);
    }
}