package com.example.project.repository;

import com.example.project.model.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<Message, String> {

  Optional<Message> findByEventId(String eventId);

  List<Message> findAllBySentFalse();

  boolean existsByEventId(String eventId);

  void deleteByEventId(String eventId);
}
