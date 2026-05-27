package com.ekko.message.web;

import com.ekko.message.config.RabbitConfig;
import com.ekko.message.domain.Message;
import com.ekko.message.domain.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;
    private final RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message saved = messageRepository.save(message);
        rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_PUBLISHED, saved);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages() {
        return ResponseEntity.ok(messageRepository.findAllByOrderBySentAtAsc());
    }
}