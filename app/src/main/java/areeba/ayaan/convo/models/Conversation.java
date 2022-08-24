package areeba.ayaan.convo.models;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    User user;
    List<Message> messages;

    public Conversation(User user) {
        this.user = user;
        messages = new ArrayList<>();
    }

    public Conversation() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
