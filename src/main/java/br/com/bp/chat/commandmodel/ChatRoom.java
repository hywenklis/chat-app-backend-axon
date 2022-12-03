package br.com.bp.chat.commandmodel;

import br.com.bp.chat.coreapi.CreateRoomCommand;
import br.com.bp.chat.coreapi.JoinRoomCommand;
import br.com.bp.chat.coreapi.LeaveRoomCommand;
import br.com.bp.chat.coreapi.MessagePostedEvent;
import br.com.bp.chat.coreapi.ParticipantJoinedRoomEvent;
import br.com.bp.chat.coreapi.ParticipantLeftRoomEvent;
import br.com.bp.chat.coreapi.PostMessageCommand;
import br.com.bp.chat.coreapi.RoomCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashSet;
import java.util.Set;

@Aggregate
public class ChatRoom {
    @AggregateIdentifier
    private String roomId;
    private Set<String> participants;

    public ChatRoom() {}

    @CommandHandler
    public ChatRoom(CreateRoomCommand command) {
        AggregateLifecycle.apply(new RoomCreatedEvent(command.getRoomId(), command.getName()));
    }

    @CommandHandler
    public void handle(JoinRoomCommand command) {
        if (!participants.contains(command.getParticipant())) {
            AggregateLifecycle.apply(new ParticipantJoinedRoomEvent(roomId, command.getParticipant()));
        }
    }

    @CommandHandler
    public void handle(LeaveRoomCommand command) {
        if (participants.contains(command.getParticipant())) {
            AggregateLifecycle.apply(new ParticipantLeftRoomEvent(command.getRoomId(), command.getParticipant()));
        }
    }

    @CommandHandler
    public void handler(PostMessageCommand command) {
        final String message = "Você não pode postar mensagens até entrar em uma sala de bate papo.";
        Assert.state(participants.contains(command.getParticipant()), () -> message);
        AggregateLifecycle.apply(new MessagePostedEvent(roomId, command.getParticipant(), command.getMessage()));
    }

    @EventSourcingHandler
    public void on(RoomCreatedEvent event) {
        this.roomId = event.getRoomId();
        this.participants = new HashSet<>();
    }

    @EventSourcingHandler
    protected void on(ParticipantJoinedRoomEvent event) {
        this.participants.add(event.getParticipant());
    }

    @EventSourcingHandler
    protected void on(ParticipantLeftRoomEvent event) {
        this.participants.remove(event.getParticipant());
    }

}
