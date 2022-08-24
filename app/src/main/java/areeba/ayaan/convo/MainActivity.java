package areeba.ayaan.convo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import areeba.ayaan.convo.models.Message;
import areeba.ayaan.convo.models.User;

public class MainActivity extends AppCompatActivity {

    MessageInput inputView;
    MessagesList messagesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputView = findViewById(R.id.message_input);
        messagesList = findViewById(R.id.messagesList);


        MessagesListAdapter messagesListAdapter = new MessagesListAdapter("areeba", null);
        messagesList.setAdapter(messagesListAdapter);


        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                User user = new User("areeba", "areeba",null);
                Message message = new Message("1", input.toString(), user, Calendar.getInstance().getTime());
                messagesListAdapter.addToStart(message, true);

                return true;
            }
        });




    }
}