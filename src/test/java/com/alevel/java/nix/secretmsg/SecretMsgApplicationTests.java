package com.alevel.java.nix.secretmsg;

import com.alevel.java.nix.secretmsg.model.Message;
import com.alevel.java.nix.secretmsg.model.MessageId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecretMsgApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(SecretMsgApplicationTests.class);

    public static final Pattern UUID_REGEX = Pattern.compile(
            "/[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    @LocalServerPort
    private int port;

    @Autowired
    private StateCleaner stateCleaner;

    private WebTestClient rest;

    @BeforeEach
    void setUp() {
        rest = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void whenSendingPostToMessages_ThenShouldCreateMessageId() {
        var text = "secret message text";
        var message = new Message(text);
        rest.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectHeader().value("Location", string -> matchesRegex(UUID_REGEX))
                .expectBody(MessageId.class)
                .value(messageId -> log.info("Response body {}", messageId))
                .value(messageId -> assertNotNull(messageId.getId()));
    }

    @Test
    void whenRetrievingSavedMessageById_ThenShouldGetItsText_ThenShouldNotFindIt() {
        var text = "secret message text";
        String resource = postForMessageURI(text);

        rest.get().uri(resource)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Message.class)
                .isEqualTo(new Message(text));

        rest.get().uri(resource)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenRetrievingNonExistingMessage_ThenShouldReturn404NotFound() {
        var resource = "/" + UUID.randomUUID();

        rest.get().uri(resource)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenMessageIsDeleted_ThenShouldNotBeAbleToFindIt() {
        var text = "to be deleted";
        var resource = postForMessageURI(text);

        rest.delete().uri(resource)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        rest.get().uri(resource)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenDeletingNonExistingMessage_ThenShouldReturn200_Ok() {
        rest.delete().uri("/" + UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

    @AfterEach
    void tearDown() {
        stateCleaner.clean();
    }

    private String postForMessageURI(String text) {
        var message = new Message(text);

        var location = rest.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .exchange()
                .returnResult(MessageId.class)
                .getResponseHeaders()
                .getLocation();

        assertNotNull(location);

        return location.toString();
    }

}
