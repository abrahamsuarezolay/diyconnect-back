package com.diyconnect.message.payload;

import com.diyconnect.message.Message;
import com.diyconnect.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDTO {

    private Long sender_id;
    private String sender_name;
    private String sender_email;

    private Long receiver_id;
    private String receiver_name;
    private String receiver_email;

    private List<MessageDTO> messages;

    public static class Builder {
        private Long sender_id;
        private String sender_name;
        private String sender_email;

        private Long receiver_id;
        private String receiver_name;
        private String receiver_email;

        private List<MessageDTO> messages = new ArrayList<>();

        public Builder senderId(Long sender_id) {
            this.sender_id = sender_id;
            return this;
        }

        public Builder senderName(String sender_name) {
            this.sender_name = sender_name;
            return this;
        }

        public Builder senderEmail(String sender_email) {
            this.sender_email = sender_email;
            return this;
        }

        public Builder receiverId(Long receiver_id) {
            this.receiver_id = receiver_id;
            return this;
        }

        public Builder receiverName(String receiver_name) {
            this.receiver_name = receiver_name;
            return this;
        }

        public Builder receiverEmail(String receiver_email) {
            this.receiver_email = receiver_email;
            return this;
        }

        public Builder messages(List<MessageDTO> messages) {
            this.messages = messages;
            return this;
        }

        public Builder addMessage(MessageDTO message) {
            this.messages.add(message);
            return this;
        }

        public ConversationDTO build() {
            return new ConversationDTO(
                    sender_id,
                    sender_name,
                    sender_email,
                    receiver_id,
                    receiver_name,
                    receiver_email,
                    messages
            );
        }
    }

}
