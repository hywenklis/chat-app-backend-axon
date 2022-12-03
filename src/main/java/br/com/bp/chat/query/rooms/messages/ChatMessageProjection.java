package br.com.bp.chat.query.rooms.messages;

import br.com.bp.chat.coreapi.MessagePostedEvent;
import br.com.bp.chat.coreapi.RoomMessagesQuery;
import br.com.bp.chat.coreapi.RoomParticipantsQuery;
import br.com.bp.chat.query.rooms.participants.RoomParticipant;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMessageProjection {

    private final ChatMessageRepository chatMessageRepository;
    private final QueryUpdateEmitter updateEmitter;


    public ChatMessageProjection(ChatMessageRepository chatMessageRepository, QueryUpdateEmitter updateEmitter) {
        this.chatMessageRepository = chatMessageRepository;
        this.updateEmitter = updateEmitter;
    }

    @QueryHandler
    public List<String> handle(RoomMessagesQuery query) {
        return chatMessageRepository.findRoomMessagesByRoomId(query.getRoomId())
                .stream()
                .map(ChatMessage::getMessage).sorted().collect(Collectors.toList());
    }

    @EventHandler
    public void on(MessagePostedEvent event, @Timestamp Instant timeStamp) {
        ChatMessage chatMessage = new ChatMessage(
                timeStamp.toEpochMilli(),
                event.getRoomId(),
                event.getMessage(),
                event.getParticipant()
        );

        chatMessageRepository.save(chatMessage);
        updateEmitter.emit(RoomMessagesQuery.class, query -> query.getRoomId().equals(event.getRoomId()), chatMessage);
    }
}
