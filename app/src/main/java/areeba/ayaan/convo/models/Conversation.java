package areeba.ayaan.convo.models;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    List<Message> messages;

    public Conversation() {
        messages = new ArrayList<>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }
}
