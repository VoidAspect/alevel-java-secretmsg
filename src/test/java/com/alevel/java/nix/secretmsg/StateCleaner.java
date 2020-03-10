package com.alevel.java.nix.secretmsg;

import com.alevel.java.nix.secretmsg.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class StateCleaner {

    @Autowired
    private MessageRepository messages;

    void clean() {
        messages.deleteAll().block();
    }

}
