package com.example.project.repository;

import com.example.project.enums.MessageType;
import com.example.project.model.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

  Optional<Message> findByEventId(String eventId);

  List<Message> findAllByMessageTypeAndSentFalse(MessageType messageType);
}
