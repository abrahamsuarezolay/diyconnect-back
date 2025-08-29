package com.diyconnect.message;

import com.diyconnect.message.payload.ConversationDTO;
import com.diyconnect.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.sender = ?1 AND m.receiver = ?2) OR (m.sender = ?2 AND m.receiver = ?1) ORDER BY m.timestamp")
    Optional<List<Message>> getConversation(User sender, User receiver);

    @Query("SELECT m FROM Message m WHERE (m.sender = ?1) OR (m.receiver = ?1) ORDER BY m.receiver")
    Optional<List<Message>> findAllConversationsByUserSender(User sender);

}
