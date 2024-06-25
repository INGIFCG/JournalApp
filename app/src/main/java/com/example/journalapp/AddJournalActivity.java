package com.example.journalapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.journalapp.model.Journal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveButton;
    private ImageView addPhotoBtn, imageView;
    private ProgressBar progressBar;
    private EditText titleEdidText,thoughtsEditText;
    //Firebase(Firestore)
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Journal");

    //Firebase (Storage)
    private StorageReference storageReference;


    //Firebase (Auth)
    private String currentUserId;
    private String currentUserName;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private Uri imageUri;
    //USING ACTIVITY RESULT Launcher
    ActivityResultLauncher<String> mtakePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_journal);
        InicializeComponent();
    }

    private void InicializeComponent() {
        progressBar = findViewById(R.id.post_progressBar);
        titleEdidText = findViewById(R.id.post_title_et);
        thoughtsEditText = findViewById(R.id.post_description_et);
        imageView = findViewById(R.id.post_imageView);
        saveButton = findViewById(R.id.post_save_journal_button);
        saveButton.setOnClickListener(this);
        addPhotoBtn = findViewById(R.id.postCameraButton);
        addPhotoBtn.setOnClickListener(this);
        progressBar.setVisibility(ImageView.INVISIBLE);
        //firebase Storage Reference
        storageReference = FirebaseStorage.getInstance().getReference();
        //Auth
        firebaseAuth = FirebaseAuth.getInstance();

        //Getting the current User
        if(user != null){
            currentUserId = user.getUid();
            currentUserName=user.getDisplayName();
        }

        mtakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imageView.setImageURI(result);
                        imageUri = result;
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.post_save_journal_button){
            SaveJournal();
        }else if(v.getId() == R.id.postCameraButton){
            //GETTIN IMAGE FOR THE GALlERY
            mtakePhoto.launch("image/*");
        }
    }
    private void SaveJournal() {
        String title = titleEdidText.getText().toString().trim();
        String thougths = thoughtsEditText.getText().toString().toString();
        progressBar.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(title)&& !TextUtils.isEmpty(thougths)&& imageUri!=null){
            //storage the image in the firebase
            final StorageReference filepath= storageReference
                    .child("journal_img")//folder
                    .child("myImg"+ Timestamp.now().getSeconds());

            //uploading the img
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                          String imgUrl = uri.toString();
                          Journal journal = new Journal();
                          journal.setTitle(title);
                          journal.setThoughts(thougths);
                          journal.setImageUrl(imgUrl);
                          journal.setTimeAdded(new Timestamp(new Date()));
                          journal.setUserName(currentUserName);
                          journal.setUserId(currentUserId);

                          collectionReference.add(journal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                              @Override
                              public void onSuccess(DocumentReference documentReference) {
                                  progressBar.setVisibility(View.INVISIBLE);
                                  Intent i = new Intent(AddJournalActivity.this,JournalListActivity.class);
                                  startActivity(i);
                                  finish();
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  showMessage("Filed "+e.getMessage());
                              }
                          });
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          progressBar.setVisibility(View.INVISIBLE);
                          showMessage("failed");
                      }
                  });
                }
            });
        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
    }
     private void showMessage(String messaje) {
        Toast.makeText(this, messaje, Toast.LENGTH_SHORT).show();
    }
}