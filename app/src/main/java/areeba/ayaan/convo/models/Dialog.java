package areeba.ayaan.convo.models;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.List;

public class Dialog implements IDialog {

    String id;
    String dialogPhoto;
    String dialogName;
    List<User> users;
    Message lastMessage;
    int unreadCount;

    public Dialog(String dialogName, List<User> users) {
        this.dialogName = dialogName;
        this.users = users;
    }

    public Dialog(String dialogPhoto, String dialogName, List<User> users, Message lastMessage, int unreadCount) {
        this.dialogPhoto = dialogPhoto;
        this.dialogName = dialogName;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public Message getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage message) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }
}
