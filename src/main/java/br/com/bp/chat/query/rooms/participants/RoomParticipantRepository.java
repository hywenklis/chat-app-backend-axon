package br.com.bp.chat.query.rooms.participants;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, String> {

    List<RoomParticipant> findRoomParticipantsByRoomId(String roomId);
    void deleteByParticipantAndRoomId(String participant, String roomId);
}
