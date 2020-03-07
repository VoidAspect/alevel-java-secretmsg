package com.alevel.java.nix.secretmsg.repository;

import com.alevel.java.nix.secretmsg.model.persistent.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface MessageRepository extends ReactiveMongoRepository<Message, UUID> {

    Flux<Message> removeByIdIn(Iterable<UUID> id);

}
