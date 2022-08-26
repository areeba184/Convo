package areeba.ayaan.convo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import areeba.ayaan.convo.models.Conversation;
import areeba.ayaan.convo.models.Dialog;
import areeba.ayaan.convo.models.Message;
import areeba.ayaan.convo.models.User;

public class ConversationListActivity extends AppCompatActivity implements DialogsListAdapter.OnDialogClickListener<Dialog>{

    DialogsList dialogsList;

    List<Dialog> conversations = new ArrayList<>();
    ImageLoader imageLoader;
    String currentUser;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DialogsListAdapter<Dialog> dialogsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        dialogsList = findViewById(R.id.dialogsList);

        currentUser = getIntent().getStringExtra("currentUser");

        database = FirebaseDatabase.getInstance();

        myRef = database.getReference("Users").child(currentUser).child("conversations");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, Conversation>> typeIndicator = new GenericTypeIndicator<HashMap<String, Conversation>>() {};
                HashMap<String, Conversation> convos = snapshot.getValue(typeIndicator);
                loadConversationList(convos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Glide.with(ConversationListActivity.this).load(url).into(imageView);
            }
        };



         dialogsListAdapter= new DialogsListAdapter<>(imageLoader);
         dialogsListAdapter.setOnDialogClickListener(this);

         dialogsList.setAdapter(dialogsListAdapter);
    }

    private void loadConversationList(HashMap<String, Conversation> convos) {
        if (convos == null) {
            return;
        }
        for (String username : convos.keySet()) {
            DatabaseReference userReference = database.getReference("Users").child(username);
            List<Message> messages = convos.get(username).getMessages();
            Message lastMessage = messages.get(messages.size()-1);

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<HashMap<String, User>> typeIndicator = new GenericTypeIndicator<HashMap<String, User>>() {};
                    HashMap<String, User> userHashMap = snapshot.getValue(typeIndicator);
                    if (userHashMap != null) {
                        User user = userHashMap.get("profile");

                        if (!conversationContainsDialogForThisUser(conversations, user.getId())) {
                            Dialog dialog = new Dialog(user.getId(), user.getAvatar(), user.getName(), Arrays.asList(user), lastMessage, 0);
                            conversations.add(dialog);
                            dialogsListAdapter.setItems(conversations);
                        } else {
                            conversations = updateLastMessage(conversations, username, lastMessage);
                            dialogsListAdapter.setItems(conversations);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }



    @Override
    public void onDialogClick(Dialog dialog) {
        Intent intent = new Intent(ConversationListActivity.this, MainActivity.class);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("currentContact", dialog.getId());
        startActivity(intent);
    }

    private List<Dialog> updateLastMessage(List<Dialog> conversations, String username, Message lastMessage) {
        for (int i = 0 ; i < conversations.size() ; i++) {
            if (conversations.get(i).getId().equals(username)) {
                conversations.get(i).setLastMessage(lastMessage);
            }
        }
        return conversations;
    }

    private boolean conversationContainsDialogForThisUser(List<Dialog> conversations, String username) {
        for (Dialog dialog : conversations) {
            if (dialog.getId().equals(username)) {
                return true;
            }
        }
        return false;
    }
}