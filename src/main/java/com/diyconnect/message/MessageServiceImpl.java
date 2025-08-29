package com.diyconnect.message;

import com.diyconnect.exception.messageException.MessageEmptyException;
import com.diyconnect.message.payload.ConversationDTO;
import com.diyconnect.message.payload.MessageDTO;
import com.diyconnect.user.User;
import com.diyconnect.user.payload.UserConversationDTO;
import com.diyconnect.utils.checkers.MessageContentChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    private MessageContentChecker messageContentChecker = new MessageContentChecker();

    @Override
    public <S extends Message> S save(S entity) {

        if(!messageContentChecker.isValidMessage(entity.getMessage())){
            throw new MessageEmptyException();
        }

        entity.setTimestamp(LocalDateTime.now());

        return messageRepository.save(entity);
    }

    @Override
    public <S extends Message> Iterable<S> saveAll(Iterable<S> entities) {
        return messageRepository.saveAll(entities);
    }

    @Override
    public Optional<Message> findById(Long aLong) {
        return messageRepository.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return messageRepository.existsById(aLong);
    }

    @Override
    public Iterable<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Iterable<Message> findAllById(Iterable<Long> longs) {
        return messageRepository.findAllById(longs);
    }

    @Override
    public long count() {
        return messageRepository.count();
    }

    @Override
    public void deleteById(Long aLong) {
        messageRepository.deleteById(aLong);
    }

    @Override
    public void delete(Message entity) {
        messageRepository.delete(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        messageRepository.deleteAllById(longs);
    }

    @Override
    public void deleteAll(Iterable<? extends Message> entities) {
        messageRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        messageRepository.deleteAll();
    }

    public Optional<List<Message>> getConversation(User sender, User receiver) {
        return messageRepository.getConversation(sender, receiver);
    }

    @Override
    public Optional<List<ConversationDTO>> findAllConversationsByUserSender(User sender) {

        //Here we obtain all the messages of a user, whether he is the sender or the receiver
        List<Message> messagesList = messageRepository.findAllConversationsByUserSender(sender).get();

        List<ConversationDTO> conversations = new ArrayList<ConversationDTO>();

        //We iterate over all the messages from that user
        for(Message message: messagesList){

            //Here we check if the conversation is already in our conversationsDTO. The first iteration always returns null.
            ConversationDTO conversation = conversations.stream()
                    .filter(convo ->
                            (convo.getReceiver_id().equals(message.getReceiver().getUser_id())
                                    && convo.getSender_id().equals(message.getSender().getUser_id()))
                                    ||
                                    (convo.getReceiver_id().equals(message.getSender().getUser_id())
                                            && convo.getSender_id().equals(message.getReceiver().getUser_id()))
                    )
                    .findFirst()
                    .orElse(null);

            if(conversation == null){
                //We add a new conversation to the list
                List<MessageDTO> messagesOfConversation = new ArrayList<MessageDTO>();
                messagesOfConversation.add(new MessageDTO(
                        message.getMessage_id(),
                        message.getMessage(),
                        message.getTimestamp(),
                        new UserConversationDTO(message.getSender().getUser_id(), message.getSender().getUsername(), message.getSender().getEmail()),
                        new UserConversationDTO(message.getReceiver().getUser_id(), message.getReceiver().getUsername(), message.getReceiver().getEmail()))
                );

                conversations.add(new ConversationDTO.Builder()
                                .senderId(message.getSender().getUser_id())
                                .senderName(message.getSender().getUsername())
                                .senderEmail(message.getSender().getEmail())
                                .receiverId(message.getReceiver().getUser_id())
                                .receiverName(message.getReceiver().getUsername())
                                .receiverEmail(message.getReceiver().getEmail())
                                .messages(messagesOfConversation)
                                .build()
                        );
            }else{
                //If the convo it's already added to the list we add the message
                conversation.getMessages().add(new MessageDTO(
                        message.getMessage_id(),
                        message.getMessage(),
                        message.getTimestamp(),
                        new UserConversationDTO(message.getSender().getUser_id(), message.getSender().getUsername(), message.getSender().getEmail()),
                        new UserConversationDTO(message.getReceiver().getUser_id(), message.getReceiver().getUsername(), message.getReceiver().getEmail())));
            }

        }

        conversations.forEach(c ->
                c.getMessages().sort(Comparator.comparing(MessageDTO::getTimestamp))
        );

        return Optional.of(conversations);
    }
}
