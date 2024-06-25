package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button singInBtn, createAcountBtn;
    EditText userPaswordImput,emailImput;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initializeComponent();
    }
    private void initializeComponent() {
        singInBtn = findViewById(R.id.sing_in_btn);
        singInBtn.setOnClickListener(this);
        createAcountBtn=findViewById(R.id.create_acount);
        createAcountBtn.setOnClickListener(this);
        userPaswordImput=findViewById(R.id.password);
        emailImput=findViewById(R.id.email);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId()==R.id.sing_in_btn){
            loginEmailPassUser(emailImput.getText().toString().trim(),userPaswordImput.getText().toString().trim());
        }else if(v.getId()==R.id.create_acount){
            intent = new Intent(this, SingUpActivity.class);
            startActivity(intent);
        }
    }

    private void loginEmailPassUser(String user, String pass) {
        if(!TextUtils.isEmpty(user)&& !TextUtils.isEmpty(pass)){
            firebaseAuth.signInWithEmailAndPassword(user,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Intent intent = new Intent(getApplicationContext(), JournalListActivity.class);
                    startActivity(intent);
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showMessage("Opss! Somethin went wrong");
                }
            });
        }else{
            showMessage("The imputs dont be empty");
        }
    }

    private void showMessage(String messaje) {
        Toast.makeText(this, messaje, Toast.LENGTH_SHORT).show();
    }
}