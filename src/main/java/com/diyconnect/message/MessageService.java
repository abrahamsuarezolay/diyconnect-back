package com.diyconnect.message;

import com.diyconnect.message.payload.ConversationDTO;
import com.diyconnect.user.User;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    <S extends Message> S save(S entity);

    <S extends Message> Iterable<S> saveAll(Iterable<S> entities);

    Optional<Message> findById(Long aLong);

    boolean existsById(Long aLong);

    Iterable<Message> findAll();

    Iterable<Message> findAllById(Iterable<Long> longs);

    long count();

    void deleteById(Long aLong);

    void delete(Message entity);

    void deleteAllById(Iterable<? extends Long> longs);

    void deleteAll(Iterable<? extends Message> entities);

    void deleteAll();

    Optional<List<Message>> getConversation(User sender, User receiver);

    Optional<List<ConversationDTO>> findAllConversationsByUserSender(User sender);

}
