package com.ai.springaidemo.controller;

import com.ai.springaidemo.service.IngestionService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatClient chatClient;
    private final IngestionService ingestionService;
    private final JdbcTemplate jdbcTemplate;

    public ChatController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, VectorStore vectorStore, IngestionService ingestionService, JdbcTemplate jdbcTemplate) {
        this.ingestionService = ingestionService;
        this.jdbcTemplate = jdbcTemplate;
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .build();
    }

    @GetMapping("/ask")
    public String ask(
            @RequestParam String message,
            @RequestParam(defaultValue = "default-session") String chatId) {

        return chatClient.prompt()
                .user(message)
                // This 'advisors' call maps the message to the session ID
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .content();
    }

    @GetMapping(value = "/ask-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> askStream(
            @RequestParam String message,
            @RequestParam(defaultValue = "default-session") String chatId) {

        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        ingestionService.ingestPdf(new InputStreamResource(file.getInputStream()));
        return "File :" + file.getOriginalFilename() + " is now in vector store for future reference";
    }

//Delete all memory in vector store

    @DeleteMapping("/clearKnowledge")
    public String ClearVectorStore() {
        jdbcTemplate.execute("TRUNCATE TABLE vector_store");

        return "cleared Knowledge";
    }
}