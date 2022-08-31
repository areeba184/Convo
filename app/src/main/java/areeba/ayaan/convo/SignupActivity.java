package areeba.ayaan.convo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import areeba.ayaan.convo.models.User;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class SignupActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference loginRef, userRef;
    HashMap<String, String> logins = new HashMap<>();

    EditText editUsername, editPassword, editConfirmPassword, editFullName;
    Button signupButton;

    Boolean signupAllowed = false;

    AppCompatImageView profileImageView;
    FloatingActionButton uploadButton;
    FirebaseStorage firebaseStorage;

    String profilePictureLink;

    Uri profilePicturePath;

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

        profileImageView = findViewById(R.id.signup_image_view);
        uploadButton = findViewById(R.id.action_upload_image);
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference().child("ProfileImages");


        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        Uri uri=result.getData().getData();
                        Glide.with(SignupActivity.this).load(uri).circleCrop().into(profileImageView);

                        this.profilePicturePath = uri;
                        Toast.makeText(this, uri.getPath(), Toast.LENGTH_SHORT).show();

                    }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                    }
                });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ImagePicker.Companion.with(SignupActivity.this)
                        .cropSquare()                                         //Crop image(Optional), Check Customization for more option
                        .setMultipleAllowed(false)                       //Let the user to pick multiple files or single file in gallery mode
                        .setOutputFormat(Bitmap.CompressFormat.PNG)    //Let the user defines the output format
                        .galleryOnly()                                  //We have to define what image provider we want to use
                        .maxResultSize(1080,1080, true)                       //Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent();

                launcher.launch(intent);
            }
        });




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



                    StorageReference profileRef = storageRef.child(editUsername.getText().toString()+profilePicturePath.getLastPathSegment());
                    UploadTask uploadTask = profileRef.putFile(profilePicturePath);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(SignupActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri downloadUrl)
                                {

                                    profilePictureLink = downloadUrl.toString();
                                    //do something with downloadurl
                                    //Glide.with(SignupActivity.this).load(downloadUrl.toString()).into(profileImageView);
                                    Toast.makeText(getApplicationContext(), downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                                    profilePictureLink = downloadUrl.toString();



                                    DatabaseReference userProfileRef = userRef.child(editUsername.getText().toString());
                                    User user = new User(editUsername.getText().toString(), editFullName.getText().toString(), profilePictureLink);

                                    Map<String, User> users = new HashMap<>();
                                    users.put("profile", user);
                                    userProfileRef.setValue(users);
                                    Toast.makeText(getApplicationContext(), "Signup Success!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });
                }
            }
        });
    }
}