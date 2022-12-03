package br.com.bp.chat.query.rooms.messages;

import br.com.bp.chat.query.rooms.participants.RoomParticipant;
import io.axoniq.axonserver.connector.query.QueryHandler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomIdOrderByTimestamp(String roomId);

    List<ChatMessage> findRoomMessagesByRoomId(String roomId);
}
