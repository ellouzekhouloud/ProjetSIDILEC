package tn.sidilec.Entity;

import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Personnel sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Personnel receiver;

    private String content;

    private Timestamp timestamp;
    private boolean isRead = false;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Personnel getSender() {
        return sender;
    }

    public void setSender(Personnel sender) {
        this.sender = sender;
    }

    public Personnel getReceiver() {
        return receiver;
    }

    public void setReceiver(Personnel receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
