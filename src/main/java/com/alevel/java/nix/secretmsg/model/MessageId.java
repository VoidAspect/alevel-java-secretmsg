package com.alevel.java.nix.secretmsg.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public final class MessageId {

    private final UUID id;

    @JsonCreator
    public MessageId(@JsonProperty(required = true, value = "id") UUID id) {
        this.id = Objects.requireNonNull(id);
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageId)) return false;

        MessageId messageId = (MessageId) o;

        return id.equals(messageId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "MessageId{" +
                "id=" + id +
                '}';
    }
}
