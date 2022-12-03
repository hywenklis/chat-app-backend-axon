package br.com.bp.chat.query.rooms.messages;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue
    private Long id;
    private long timestamp;
    private String roomId;
    private String message;
    private String participant;

    public ChatMessage() {}

    public ChatMessage(long timestamp, String roomId, String message, String participant) {
        this.timestamp = timestamp;
        this.roomId = roomId;
        this.message = message;
        this.participant = participant;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getParticipant() {
        return participant;
    }
}
