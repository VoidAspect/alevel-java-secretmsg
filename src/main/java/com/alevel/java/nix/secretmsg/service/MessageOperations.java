package com.alevel.java.nix.secretmsg.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface MessageOperations {

    Mono<UUID> save(String message);

    Mono<String> pop(UUID messageId);

    Mono<Void> drop(UUID messageId);

}
