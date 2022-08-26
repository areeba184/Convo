package areeba.ayaan.convo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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



        User userAyaan = new User("ayaan", "Ayaan Khan", "https://www.static-contents.youth4work.com/y4w/Images/Users/2452690.png?v=20180402161648");

        // conversation = new Conversation(userAyaan);

        MessagesListAdapter messagesListAdapter = new MessagesListAdapter("areeba", null);
        messagesList.setAdapter(messagesListAdapter);

        currentProfile = new UserProfile("areeba", "Areeba Khan", "areebaAvatar");
        /*User userAreeba = new User("areeba", "Areeba Khan", "areebaAvatar");
        User userZaid = new User("zaid", "Mohd Zaid", "zaidAvatar");

        Message message = new Message("123", "Hello there", userAyaan, Calendar.getInstance().getTime());
        Message message2 = new Message("122", "Hello Ayaan", userAreeba, Calendar.getInstance().getTime());

        Conversation areebaAyaanConversation = new Conversation(userAyaan);
        areebaAyaanConversation.addMessage(message);
        areebaAyaanConversation.addMessage(message2);


        UserProfile areebaProfile = new UserProfile("areeba", "Areeba Khan", "areebaAvatar");
        areebaProfile.addConversation(areebaAyaanConversation);*/

        // UserProfile ayaanProfile = new UserProfile("ayaan", "Ayaan Khan", "ayaanAvatar");


        /*profiles.put("areeba", areebaProfile);
        // profiles.put("ayaan", ayaanProfile);


        messages.put("ayaan", message);
        messages.put("areeba", message2);*/

        myRef = database.getReference("Users");
        DatabaseReference convoRef = myRef.child(currentUser).child("conversations");
        DatabaseReference convoToRef = myRef.child(currentContact).child("conversations");

        /*User user = new User("areeba", "Areeba Khan", null);
        Message message = new Message("123", "hello", user, Calendar.getInstance().getTime());
        conversation = new Conversation();
        conversation.addMessage(message);
        HashMap<String, Conversation> conversationHashMap = new HashMap<>();
        HashMap<String, Conversation> conversationToHashMap = new HashMap<>();
        conversationHashMap.put(currentContact, conversation);
        conversationToHashMap.put(currentUser, conversation);
        convoRef.setValue(conversationHashMap);
        convoToRef.setValue(conversationToHashMap);*/

        convoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<HashMap<String, Conversation>> typeIndicator = new GenericTypeIndicator<HashMap<String, Conversation>>() {};
                conversations = dataSnapshot.getValue(typeIndicator);
                if (conversations != null) {
                    conversation = conversations.get(currentContact);
                    messagesListAdapter.clear();
                    messagesListAdapter.addToEnd(conversation.getMessages(), true);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // myRef.setValue(profiles);


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
                            HashMap<String, Conversation> conversationHashMap = new HashMap<>();
                            HashMap<String, Conversation> conversationToHashMap = new HashMap<>();
                            conversationHashMap.put(currentContact, conversation);
                            conversationToHashMap.put(currentUser, conversation);
                            convoRef.setValue(conversationHashMap);
                            convoToRef.setValue(conversationToHashMap);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                /*if (profiles == null) {
                    profiles = new HashMap<>();
                    currentConversation = new Conversation(userAyaan);
                } else {
                    if (currentConversation == null) {
                        currentConversation = profiles.get("areeba").getConversations().get(0);
                    }
                }

                User user = new User("areeba", "areeba","areebaAvatar");
                Message message = new Message("1", input.toString(), user, Calendar.getInstance().getTime());
                currentConversation.addMessage(message);
                currentProfile.addConversation(currentConversation);
                messagesListAdapter.addToStart(message, true);

                profiles.put("areeba", currentProfile);
                myRef.setValue(profiles);*/

                return true;
            }
        });




    }
}