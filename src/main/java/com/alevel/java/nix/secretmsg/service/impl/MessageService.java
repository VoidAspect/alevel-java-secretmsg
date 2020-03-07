package com.alevel.java.nix.secretmsg.service.impl;

import com.alevel.java.nix.secretmsg.exceptions.MessageNotFoundException;
import com.alevel.java.nix.secretmsg.model.persistent.Message;
import com.alevel.java.nix.secretmsg.repository.MessageRepository;
import com.alevel.java.nix.secretmsg.service.MessageOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

@Service
public class MessageService implements MessageOperations {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Mono<UUID> save(String message) {
        return messageRepository.save(new Message(message)).map(Message::getId);
    }

    @Override
    public Mono<String> pop(UUID messageId) {
        return messageRepository.removeByIdIn(Collections.singletonList(messageId))
                .singleOrEmpty()
                .switchIfEmpty(Mono.error(() -> new MessageNotFoundException(messageId)))
                .map(Message::getText);
    }

    @Override
    public Mono<Void> drop(UUID messageId) {
        return messageRepository.deleteById(messageId);
    }
}
