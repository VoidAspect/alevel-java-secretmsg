package com.alevel.java.nix.secretmsg.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class MessageNotFoundException extends ResponseStatusException {

    public MessageNotFoundException(UUID messageId) {
        super(HttpStatus.NOT_FOUND, "Message " + messageId + " was not found");
    }

}
