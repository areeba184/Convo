package areeba.ayaan.convo.models;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class Message implements IMessage {
    String id;

    public Message(String id, String text, User user, Date createdAt) {
        this.text = text;
        this.id = id;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Message() {

    }

    String text;
    User user;
    Date createdAt;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
}
