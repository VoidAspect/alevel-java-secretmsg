package com.alevel.java.nix.secretmsg.service.impl;

import com.alevel.java.nix.secretmsg.exceptions.MessageNotFoundException;
import com.alevel.java.nix.secretmsg.model.persistent.Message;
import com.alevel.java.nix.secretmsg.repository.MessageRepository;
import com.alevel.java.nix.secretmsg.service.MessageOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

@Service
public class MessageService implements MessageOperations {

    private final MessageRepository messageRepository;

    private final String password;

    public MessageService(MessageRepository messageRepository,
                          @Value("${secretmsg.encryption.password}") String password) {
        this.messageRepository = messageRepository;
        this.password = password;
    }

    @Override
    public Mono<UUID> save(String message) {
        return messageRepository.save(encrypt(message)).map(Message::getId);
    }

    @Override
    public Mono<String> pop(UUID messageId) {
        return messageRepository.removeByIdIn(Collections.singletonList(messageId))
                .singleOrEmpty()
                .switchIfEmpty(Mono.error(() -> new MessageNotFoundException(messageId)))
                .map(this::decrypt);
    }

    @Override
    public Mono<Void> drop(UUID messageId) {
        return messageRepository.deleteById(messageId);
    }

    private Message encrypt(String message) {
        var id = UUID.randomUUID();
        var text = crypto(id).encrypt(message);
        return new Message(id, text);
    }

    private String decrypt(Message message) {
        return crypto(message.getId()).decrypt(message.getText());
    }

    private TextEncryptor crypto(UUID id) {
        return Encryptors.delux(password, salt(id));
    }

    private String salt(UUID id) {
        return Long.toHexString(id.getLeastSignificantBits()) + Long.toHexString(id.getMostSignificantBits());
    }
}
