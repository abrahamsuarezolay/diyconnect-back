package com.diyconnect.message.payload;
import com.diyconnect.user.User;
import com.diyconnect.user.UserService;
import com.diyconnect.user.payload.UserConversationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private long message_id;
    private String message;
    private LocalDateTime timestamp;
    private UserConversationDTO sender;
    private UserConversationDTO receiver;

}
