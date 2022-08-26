package areeba.ayaan.convo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    HashMap<String, String> logins = new HashMap<>();

    EditText editUsername, editPassword;
    Button loginButon, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editPassword = findViewById(R.id.editPassword);
        editUsername = findViewById(R.id.editUsername);
        loginButon = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.goto_signup_button);

        database = FirebaseDatabase.getInstance();

        myRef = database.getReference("Logins");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, String>> typeIndicator = new GenericTypeIndicator<HashMap<String, String>>() {};
                logins = snapshot.getValue(typeIndicator);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loginButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editUsername.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()){
                    if (editPassword.getText().toString().isEmpty()) {
                        editPassword.setError("Enter password");
                    }
                    if (editUsername.getText().toString().isEmpty()) {
                        editUsername.setError("Enter Username");
                    }
                }
                else {
                    if (!logins.containsKey(editUsername.getText().toString())) {
                        editUsername.setError("Username not found!");
                        Toast.makeText(getApplicationContext(), "Username not found!", Toast.LENGTH_SHORT).show();
                    }
                    else if (!logins.get(editUsername.getText().toString()).equals(editPassword.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_SHORT).show();
                        editPassword.setError("Wrong Password!");
                    }
                    else if(logins.get(editUsername.getText().toString()).equals(editPassword.getText().toString())) {
                        Intent intent = new Intent(LoginActivity.this, ConversationListActivity.class);
                        intent.putExtra("currentUser", editUsername.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}