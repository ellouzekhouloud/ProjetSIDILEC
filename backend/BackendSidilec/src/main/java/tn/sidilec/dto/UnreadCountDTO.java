
package tn.sidilec.dto;

public class UnreadCountDTO {
    private Long senderId;
    private Long unreadCount;

    public UnreadCountDTO(Long senderId, Long unreadCount) {
        this.senderId = senderId;
        this.unreadCount = unreadCount;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Long unreadCount) {
        this.unreadCount = unreadCount;
    }
}
