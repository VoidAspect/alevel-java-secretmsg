package com.alevel.java.nix.secretmsg.controller;

import com.alevel.java.nix.secretmsg.model.Message;
import com.alevel.java.nix.secretmsg.model.MessageId;
import com.alevel.java.nix.secretmsg.service.MessageOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping
public class MessageController {

    private final MessageOperations messageOperations;

    public MessageController(MessageOperations messageOperations) {
        this.messageOperations = messageOperations;
    }

    @PostMapping
    public Mono<ResponseEntity<MessageId>> create(@RequestBody Mono<Message> message) {
        return message.map(Message::getText)
                .flatMap(messageOperations::save)
                .map(messageId -> ResponseEntity
                        .created(URI.create("/" + messageId))
                        .body(new MessageId(messageId)));
    }

    @GetMapping("/{id}")
    public Mono<Message> getById(@PathVariable UUID id) {
        return messageOperations.pop(id).map(Message::new);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> dropById(@PathVariable UUID id) {
        return messageOperations.drop(id);
    }

}
