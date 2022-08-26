package areeba.ayaan.convo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Map;

import areeba.ayaan.convo.models.User;

public class SignupActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference loginRef, userRef;
    HashMap<String, String> logins = new HashMap<>();

    EditText editUsername, editPassword, editConfirmPassword, editFullName;
    Button signupButton;

    Boolean signupAllowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editUsername = findViewById(R.id.signup_username);
        editPassword = findViewById(R.id.signup_password);
        editConfirmPassword = findViewById(R.id.signup_password_confirm);
        editFullName = findViewById(R.id.signup_full_name);
        signupButton = findViewById(R.id.signup_button);

        database = FirebaseDatabase.getInstance();

        loginRef = database.getReference("Logins");
        userRef = database.getReference("Users");

        loginRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, String>> typeIndicator = new GenericTypeIndicator<HashMap<String, String>>() {};
                logins = snapshot.getValue(typeIndicator);
                signupAllowed = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logins == null) {
                    logins = new HashMap<>();
                }

                if (logins.containsKey(editUsername.getText().toString())) {
                    editUsername.setError("Username already exists");
                    Toast.makeText(getApplicationContext(), "Username already exists!", Toast.LENGTH_SHORT).show();
                }
                else if (!editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords should be same!", Toast.LENGTH_SHORT).show();
                }
                else if (signupAllowed){
                    logins.put(editUsername.getText().toString(), editPassword.getText().toString());
                    loginRef.setValue(logins);

                    DatabaseReference userProfileRef = userRef.child(editUsername.getText().toString());
                    User user = new User(editUsername.getText().toString(), editFullName.getText().toString(), null);

                    Map<String, User> users = new HashMap<>();
                    users.put("profile", user);
                    userProfileRef.setValue(users);


                    Toast.makeText(getApplicationContext(), "Signup Success!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}