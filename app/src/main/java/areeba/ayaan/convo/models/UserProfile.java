package areeba.ayaan.convo.models;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {

    String id;
    String name;
    String avatar;
    List<Conversation> conversations;

    public UserProfile(String id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        conversations = new ArrayList<>();
    }

    public UserProfile() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public void addConversation(Conversation conversation) {
        this.conversations.add(conversation);
    }
}
