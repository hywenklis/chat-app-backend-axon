package br.com.bp.chat.query.rooms.participants;

import br.com.bp.chat.coreapi.ParticipantJoinedRoomEvent;
import br.com.bp.chat.coreapi.ParticipantLeftRoomEvent;
import br.com.bp.chat.coreapi.RoomParticipantsQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomParticipantProjection {

    private final RoomParticipantRepository roomParticipantRepository;

    public RoomParticipantProjection(RoomParticipantRepository roomParticipantRepository) {
        this.roomParticipantRepository = roomParticipantRepository;
    }

    @QueryHandler
    public List<String> handle(RoomParticipantsQuery query) {
        return roomParticipantRepository.findRoomParticipantsByRoomId(query.getRoomId())
                .stream()
                .map(RoomParticipant::getParticipant).sorted().collect(Collectors.toList());
    }

    @EventHandler
    public void on(ParticipantJoinedRoomEvent event) {
        roomParticipantRepository.save(new RoomParticipant(event.getRoomId(), event.getParticipant()));
    }

    @EventHandler
    public void on(ParticipantLeftRoomEvent event) {
        roomParticipantRepository.deleteByParticipantAndRoomId(event.getRoomId(), event.getParticipant());
    }
}
