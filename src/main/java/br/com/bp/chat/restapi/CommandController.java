package br.com.bp.chat.restapi;

import br.com.bp.chat.coreapi.CreateRoomCommand;
import br.com.bp.chat.coreapi.JoinRoomCommand;
import br.com.bp.chat.coreapi.LeaveRoomCommand;
import br.com.bp.chat.coreapi.PostMessageCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;
import java.util.concurrent.Future;

@RestController
public class CommandController {

    private final CommandGateway commandGateway;

    public CommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/rooms")
    public Future<String> createChatRoom(@RequestBody @Valid Room room) {
        final String message = "O nome é obrigatório para uma sala";
        Assert.notNull(room.getName(), () -> message);
        String roomId = room.getRoomId() == null ? UUID.randomUUID().toString() : room.getRoomId();
        return commandGateway.send(new CreateRoomCommand(roomId, room.getName()));
    }

    @PostMapping("/rooms{roomId}/participants")
    public Future<Void> joinChatRoom(@PathVariable String roomId, @RequestBody @Valid Participant participant) {
        final String message = "Nome é obrigatório para um participante";
        Assert.notNull(participant.getName(), () -> message);
        return commandGateway.send(new JoinRoomCommand(roomId, participant.getName()));
    }

    @PostMapping("/rooms/{roomId}/messages")
    public Future<Void> postMessage(@PathVariable String roomId, @RequestBody @Valid Message message) {
        Assert.notNull(message.getName(), () -> "Informe o nome de um participante");
        Assert.notNull(message.getMessage(), () -> "Informe uma mensagem");

        return commandGateway.send(new PostMessageCommand(roomId, message.getName(), message.getMessage()));
    }

    @PostMapping("/rooms/{roomId}/leave")
    public Future<Void> leaveChatRoom(@PathVariable String roomId, @RequestBody @Valid Participant participant) {
        Assert.notNull(participant.getName(), () -> "O nome é obrigatório para sair da sala");
        return commandGateway.send(new LeaveRoomCommand(roomId, participant.getName()));
    }

    public static class Room {
        private String roomId;
        @NotEmpty
        private String name;

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Participant {
        @NotEmpty
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Message {
        @NotEmpty
        private String name;
        @NotEmpty
        private String message;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
