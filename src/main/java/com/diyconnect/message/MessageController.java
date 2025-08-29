package com.diyconnect.message;

import com.diyconnect.exception.messageException.MessageEmptyException;
import com.diyconnect.exception.userException.UserNotFoundException;
import com.diyconnect.message.payload.ConversationDTO;
import com.diyconnect.message.payload.GetConversationRequest;
import com.diyconnect.message.payload.MessageSendRequest;
import com.diyconnect.user.User;
import com.diyconnect.user.UserService;
import com.diyconnect.utils.mappers.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    private EntityMapper entityMapper = new EntityMapper();

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody MessageSendRequest messageSendRequest){
        try{
            Message message = new Message( messageSendRequest.getMessage(),
                    userService.findByEmail(messageSendRequest.getSenderEmail()).get(),
                    userService.findByEmail(messageSendRequest.getReceiverEmail()).get()
            );

            messageService.save(message);
            return new ResponseEntity<>(message, HttpStatus.OK);

        }catch(MessageEmptyException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getConversation")
    public ResponseEntity<?> getConversation(@RequestBody GetConversationRequest getConversationRequest){
        try{

            List<Message> conversation = messageService.getConversation(
                    userService.findByEmail(getConversationRequest.getSenderEmail()).get(),
                    userService.findByEmail(getConversationRequest.getReceiverEmail()).get()
            ).get();

            return new ResponseEntity<>(conversation, HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllConversationsByUser")
    public ResponseEntity<?> getAllConversationsByUser(@RequestParam Long user_id){
        try{

            User user = userService.findById(user_id).get();

            List<ConversationDTO> conversations = messageService.findAllConversationsByUserSender(user).get();

            return new ResponseEntity<>(conversations, HttpStatus.OK);
        }catch(UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
