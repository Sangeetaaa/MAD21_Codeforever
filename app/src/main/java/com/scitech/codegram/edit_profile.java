package com.scitech.codegram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class edit_profile extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;

    TextView textView;
    CircularImageView imageView;
    TextInputLayout username, mobile, status;

    Uri uriProfileImage;
    ProgressBar progressBar;

    String profileImageUrl;
    CheckBox hackerrank,codechef,hackerearth;
    EditText hackerrank_user, codechef_user, hackerearth_user;
    FirebaseAuth mAuth;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.editTextDisplayName);
        mobile = findViewById(R.id.editTextDisplayMobile);
        status = findViewById(R.id.editTextDisplayBio);
        imageView = findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        textView = (TextView) findViewById(R.id.textViewVerified);
        hackerrank = (CheckBox)findViewById(R.id.hackerrank);
        hackerearth = (CheckBox)findViewById(R.id.hackerearth);
        codechef = (CheckBox)findViewById(R.id.codechef);
        hackerrank_user = (EditText) findViewById(R.id.hackerrank_user);
        codechef_user = (EditText)findViewById(R.id.codechef_user);
        hackerearth_user = (EditText)findViewById(R.id.hackerearth_user);

        databaseReference = FirebaseDatabase.getInstance().getReference("CodeStalk_users");

        hackerrank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked != true)
                    hackerrank_user.setVisibility(View.GONE);
                else
                    hackerrank_user.setVisibility(View.VISIBLE);
            }
        });
        codechef.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked != true)
                    codechef_user.setVisibility(View.GONE);
                else
                    codechef_user.setVisibility(View.VISIBLE);
            }
        });
        hackerearth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked != true)
                    hackerearth_user.setVisibility(View.GONE);
                else
                    hackerearth_user.setVisibility(View.VISIBLE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        loadUserInformation();

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });
    }


    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot walker : dataSnapshot.getChildren()) {
                        if(walker.getKey().equals(user.getUid())) {
                            Codegram_user currentUser = walker.getValue(Codegram_user.class);

                            mobile.getEditText().setText(currentUser.getMobile());
                            status.getEditText().setText(currentUser.getStatus());
                            if(!currentUser.getHackerrank_user().isEmpty()){
                                hackerrank.setChecked(true);
                                hackerrank_user.setText(currentUser.getHackerrank_user());
                            }
                            if(!currentUser.getHackerearth_user().isEmpty()){
                                hackerearth.setChecked(true);
                                hackerearth_user.setText(currentUser.getHackerearth_user());
                            }
                            if(!currentUser.getCodechef_user().isEmpty()){
                                codechef.setChecked(true);
                                codechef_user.setText(currentUser.getCodechef_user());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NotNull DatabaseError databaseError) {

                }
            });
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }

            if (user.getDisplayName() != null) {
                username.getEditText().setText(user.getDisplayName());
            }

            if (user.isEmailVerified()) {
                textView.setText("Email Verified");
            } else {
                textView.setText("Email Not Verified (Click to Verify)");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(edit_profile.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }
    }


    private void saveUserInformation() {

        String displayName = username.getEditText().getText().toString();
        String displayMobile = mobile.getEditText().getText().toString();
        String displayStatus = status.getEditText().getText().toString();
        String hackerrankUser = hackerrank_user.getText().toString();
        String codechefUser = codechef_user.getText().toString();
        String hackerearthUser = hackerearth_user.getText().toString();


        if (displayName.isEmpty()) {
            username.setError("Username required");
            username.requestFocus();
            return;
        }
        else if(displayMobile.isEmpty())
        {
            mobile.setError("Mobile Number required");
            mobile.requestFocus();
            return;
        }
        else if(displayStatus.isEmpty())
        {
            status.setError("Bio required");
            status.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        String picUrl = profileImageUrl;
       // Toast.makeText(getApplicationContext(), picUrl, Toast.LENGTH_SHORT).show();
        String uid = user.getUid();
        String emailId = user.getEmail();

        Codegram_user newUser = new Codegram_user(uid, emailId, displayName, displayMobile, displayStatus,
                picUrl, hackerrankUser, codechefUser, hackerearthUser);


        //Toast.makeText(edit_profile.this,"Added", Toast.LENGTH_SHORT).show();

        String way = getIntent().getStringExtra("Intent");
        if (user != null && profileImageUrl != null) {

            databaseReference.child(uid).setValue(newUser);

            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(edit_profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                if (!way.equals("Profile")) {
                                    startActivity(new Intent(edit_profile.this, profile_view.class));
                                }
                                finish();

                            }
                            else{
                                Toast.makeText(edit_profile.this, "Profile Update Failed !!! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            newUser.picUrl = user.getPhotoUrl().toString();
            //Toast.makeText(getApplicationContext(), newUser.getPicUrl(), Toast.LENGTH_SHORT).show();
            databaseReference.child(uid).setValue(newUser);

            Toast.makeText(edit_profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            if (!way.equals("Profile")) {
                startActivity(new Intent(edit_profile.this, profile_view.class));
            }
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageUrl = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(edit_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

}
