package tn.sidilec.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tn.sidilec.Entity.Message;


public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findBySender_IdAndReceiver_IdOrReceiver_IdAndSender_IdOrderByTimestampAsc(
	        Long senderId1, Long receiverId1, Long receiverId2, Long senderId2
	    );
	    List<Message> findByReceiver_IdOrderByTimestampDesc(Long receiverId);
	    int countBySender_IdAndReceiver_IdAndIsReadFalse(Long senderId, Long receiverId);
	    List<Message> findByReceiver_IdAndIsReadFalse(Long receiverId);
	   
	 // Récupère le dernier message échangé entre deux utilisateurs, peu importe le sens
	    @Query("SELECT m FROM Message m " +
	           "WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) " +
	           "   OR (m.sender.id = :receiverId AND m.receiver.id = :senderId) " +
	           "ORDER BY m.timestamp DESC LIMIT 1")
	    Message findTopByUsersOrderByTimestampDesc(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

}