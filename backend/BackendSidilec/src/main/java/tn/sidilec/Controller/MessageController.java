package tn.sidilec.Controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tn.sidilec.Entity.Message;
import tn.sidilec.Entity.Personnel;
import tn.sidilec.Entity.Role;
import tn.sidilec.Repository.MessageRepository;
import tn.sidilec.Repository.PersonnelRepository;
import tn.sidilec.dto.MessageDTO;

import org.springframework.messaging.simp.SimpMessagingTemplate;


@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {
	@Autowired
	private SimpMessagingTemplate messagingTemplate;


    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    public MessageRepository getMessageRepository() {
		return messageRepository;
	}

	public void setMessageRepository(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}
	

	public PersonnelRepository getPersonnelRepository() {
		return personnelRepository;
	}

	public void setPersonnelRepository(PersonnelRepository personnelRepository) {
		this.personnelRepository = personnelRepository;
	}
	
	
	@PutMapping("/{messageId}/read")
	public Message markMessageAsRead(@PathVariable Long messageId) {
	    Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
	    message.setRead(true);
	    return messageRepository.save(message);
	}
	@GetMapping("/received/{userId}")
	public List<Message> getReceivedMessages(@PathVariable Long userId) {
	    return messageRepository.findByReceiver_IdOrderByTimestampDesc(userId);
	}
	@GetMapping("/receivers/by-role/{role}")
	public List<Personnel> getReceiversByRole(@PathVariable Role role) {
	    return personnelRepository.findByRole(role);
	}
	@GetMapping("/unread-count/{senderId}/{receiverId}")
	public int getUnreadMessageCount(@PathVariable Long senderId, @PathVariable Long receiverId) {
	    return messageRepository.countBySender_IdAndReceiver_IdAndIsReadFalse(senderId, receiverId);
	}
	@GetMapping("/unread-senders/{receiverId}")
	public List<Long> getUnreadSenderIds(@PathVariable Long receiverId) {
	    List<Message> unreadMessages = messageRepository.findByReceiver_IdAndIsReadFalse(receiverId);
	    return unreadMessages.stream()
	                         .map(m -> m.getSender().getId())
	                         .distinct()
	                         .toList();
	}



	@GetMapping("/unread/{receiverId}")
	public List<MessageDTO> getUnreadMessagesByReceiver(@PathVariable Long receiverId) {
	    List<Message> messages = messageRepository.findByReceiver_IdAndIsReadFalse(receiverId);
	    return messages.stream()
	                   .map(MessageDTO::new)
	                   .collect(Collectors.toList());
	}

	@PostMapping
	public Message saveMessage(@RequestBody Message message) {
	    message.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
	    Message saved = messageRepository.save(message);

	    // 🔔 Envoi de la notification WebSocket privée au destinataire
	    if (saved.getReceiver() != null) {
	        String destination = "/queue/messages";
	        messagingTemplate.convertAndSendToUser(
	            String.valueOf(saved.getReceiver().getId()), // receiverId doit correspondre à l'identifiant côté frontend
	            destination,
	            saved
	        );
	    }

	    return saved;
	}
	


    @GetMapping("/{senderId}/{receiverId}")
    public List<Message> getMessages(@PathVariable Long senderId, @PathVariable Long receiverId) {
        return messageRepository.findBySender_IdAndReceiver_IdOrReceiver_IdAndSender_IdOrderByTimestampAsc(
            senderId, receiverId, senderId, receiverId
        );
    }
    
    @GetMapping("/last")
    public MessageDTO getLastMessageBetweenUsers(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {

        Message lastMessage = messageRepository.findTopByUsersOrderByTimestampDesc(senderId, receiverId);
        if (lastMessage == null) {
            return null; // ou lève une exception ou retourne ResponseEntity.notFound().build()
        }

        return new MessageDTO(lastMessage);
    }

   
}
