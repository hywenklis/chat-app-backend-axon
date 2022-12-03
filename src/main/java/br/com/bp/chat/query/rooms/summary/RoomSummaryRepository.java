package br.com.bp.chat.query.rooms.summary;

import net.bytebuddy.build.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomSummaryRepository extends JpaRepository<RoomSummary, String> {
}
