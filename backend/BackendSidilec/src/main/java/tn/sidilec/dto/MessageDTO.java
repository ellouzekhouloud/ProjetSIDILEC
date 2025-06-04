package tn.sidilec.dto;

import lombok.Getter;
import lombok.Setter;
import tn.sidilec.Entity.Message;

@Getter
@Setter
//MessageDTO.java
public class MessageDTO {
 private Long id;
 private String content;
 private String timestamp;
 private boolean isRead;
 private Long senderId;
 private String senderNom;

 // Constructeur
 public MessageDTO(Message message) {
     this.id = message.getId();
     this.content = message.getContent();
     this.timestamp = message.getTimestamp().toString();
     this.isRead = message.isRead();
     this.senderId = message.getSender().getId();
     this.senderNom = message.getSender().getNom(); // Assure-toi que "getNom()" existe
 }

 
}
