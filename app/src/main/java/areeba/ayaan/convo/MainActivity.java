package areeba.ayaan.convo;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import areeba.ayaan.convo.models.Conversation;
import areeba.ayaan.convo.models.Message;
import areeba.ayaan.convo.models.User;
import areeba.ayaan.convo.models.UserProfile;

public class MainActivity extends AppCompatActivity {

    MessageInput inputView;
    MessagesList messagesList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Conversation conversation;

    Map<String, Message> messages;

    Map<String, UserProfile> profiles;
    Map<String, Conversation> conversations;
    UserProfile currentProfile;
    String currentUser, currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputView = findViewById(R.id.message_input);
        messagesList = findViewById(R.id.messagesList);
        database = FirebaseDatabase.getInstance();

        currentUser = getIntent().getStringExtra("currentUser");
        currentContact = getIntent().getStringExtra("currentContact");

        messages = new HashMap<>();
        profiles = new HashMap<>();


        MessagesListAdapter messagesListAdapter = new MessagesListAdapter(currentUser, null);
        messagesList.setAdapter(messagesListAdapter);

        myRef = database.getReference("Users");
        DatabaseReference convoRef = myRef.child(currentUser).child("conversations");
        DatabaseReference convoToRef = myRef.child(currentContact).child("conversations");

        convoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<HashMap<String, Conversation>> typeIndicator = new GenericTypeIndicator<HashMap<String, Conversation>>() {};
                conversations = dataSnapshot.getValue(typeIndicator);
                if (conversations != null) {
                    conversation = conversations.get(currentContact);
                    if (conversation == null) {
                        conversation = new Conversation();
                    }
                    messagesListAdapter.clear();
                    List<Message> messages = conversation.getMessages();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        messages.sort(Comparator.comparing(Message::getCreatedAt));
                    }
                    messagesListAdapter.addToEnd(messages, true);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {



                DatabaseReference profileReference = myRef.child(currentUser);

                profileReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        GenericTypeIndicator<HashMap<String, User>> typeIndicator = new GenericTypeIndicator<HashMap<String, User>>() {};

                        HashMap<String, User> userProfile = snapshot.getValue(typeIndicator);

                        if (userProfile != null) {
                            User user = userProfile.get("profile");
                            Message message = new Message(currentUser, input.toString(), user, Calendar.getInstance().getTime());
                            conversation.addMessage(message);
                            HashMap<String, Object> conversationHashMap = new HashMap<>();
                            HashMap<String, Object> conversationToHashMap = new HashMap<>();
                            conversationHashMap.put(currentContact, conversation);
                            conversationToHashMap.put(currentUser, conversation);
                            convoRef.updateChildren(conversationHashMap);
                            convoToRef.updateChildren(conversationToHashMap);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                return true;
            }
        });




    }
}