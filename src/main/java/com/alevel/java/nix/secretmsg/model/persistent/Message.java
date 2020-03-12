package com.alevel.java.nix.secretmsg.model.persistent;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;
import java.util.UUID;

@Document(collection = "messages")
public final class Message {

    @Id
    private final UUID id;

    @Field
    private final String text;

    @PersistenceConstructor
    public Message(UUID id, String text) {
        this.id = id;
        this.text = text;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
