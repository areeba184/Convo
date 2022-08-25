package areeba.ayaan.convo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import areeba.ayaan.convo.models.Dialog;
import areeba.ayaan.convo.models.Message;
import areeba.ayaan.convo.models.User;

public class ConversationListActivity extends AppCompatActivity implements DialogsListAdapter.OnDialogClickListener<Dialog>{

    DialogsList dialogsList;

    List<Dialog> conversations = new ArrayList<>();
    ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        dialogsList = findViewById(R.id.dialogsList);

        User userAyaan = new User("ayaan", "Ayaan Khan", "https://www.static-contents.youth4work.com/y4w/Images/Users/2452690.png");
        User userZaid = new User("zaid", "Mohd Zaid", "zaidAvatar");

        Message message = new Message("123", "Hello there", userAyaan, Calendar.getInstance().getTime());
        Message message2 = new Message("122", "Hello Ayaan", userZaid, Calendar.getInstance().getTime());

        Dialog dialog1 = new Dialog(null, "Ayaan Khan", Arrays.asList(userAyaan), message, 2);
        Dialog dialog2 = new Dialog("https://www.static-contents.youth4work.com/y4w/Images/Users/2452690.png", "Mohd Zaid", Arrays.asList(userZaid), message2, 0);
        //Dialog dialog2 = new Dialog("Mohd Zaid", Arrays.asList(userZaid));

        conversations.add(dialog1);
        conversations.add(dialog2);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Glide.with(ConversationListActivity.this).load(url).into(imageView);
            }
        };



         DialogsListAdapter<Dialog> dialogsListAdapter= new DialogsListAdapter<>(imageLoader);
         dialogsListAdapter.setItems(conversations);
         dialogsListAdapter.setOnDialogClickListener(this);

         dialogsList.setAdapter(dialogsListAdapter);
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        Toast.makeText(getApplicationContext(), dialog.getDialogName(), Toast.LENGTH_SHORT).show();
    }
}