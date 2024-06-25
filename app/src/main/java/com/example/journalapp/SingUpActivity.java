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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SingUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText userCreateImput, passwordCreateImput,emailCreateImput;
    Button sinUpBtn;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firebase Connection
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_up);
        inicializeComponent();
    }

    private void inicializeComponent() {
        userCreateImput = findViewById(R.id.username_create_ET);
        passwordCreateImput = findViewById(R.id.password_create_ET);
        emailCreateImput = findViewById(R.id.email_create);
        sinUpBtn = findViewById(R.id.singUp_btn);
        sinUpBtn.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();// devuelve la informacion del usuario una vez es autenticado
                //check if user is logged or not
                if(currentUser!= null){
                    //user Already Logged in
                }else{
//                    the user sing out
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.singUp_btn) {
            if(!TextUtils.isEmpty(emailCreateImput.getText())&&
                    !TextUtils.isEmpty(passwordCreateImput.getText())&&
                    !TextUtils.isEmpty(userCreateImput.getText())){
                String email = emailCreateImput.getText().toString().trim();
                String pass = passwordCreateImput.getText().toString().trim();
                String username = userCreateImput.getText().toString().trim();
                CreateUserEmailAccount(email,pass,username);
            }else{
                String emptyFields = !TextUtils.isEmpty(emailCreateImput.getText().toString().trim()) ?
                        !TextUtils.isEmpty(passwordCreateImput.getText().toString().trim()) ?
                                !TextUtils.isEmpty(userCreateImput.getText().toString().trim()) ?
                        "":"User cant be Empty":"Password cant be Empty" :"Email cant be Empty";
                showMessage("No Empty Fields are allowed/"+ emptyFields);
            }
        }
    }
    private void CreateUserEmailAccount(String email,String pass, String username){
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(username)){
            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //the user is create successfully!
                        showMessage("The user is created successfully!");
                        Intent intent = new Intent(SingUpActivity.this,JournalListActivity.class);
                        startActivity(intent);
                    }else{
                        showMessage("Error to create user");
                    }
                }
            });
        }else{
            showMessage("No Empty Fields are allowed");
        }
    }
    private void showMessage(String messaje) {
        Toast.makeText(this, messaje, Toast.LENGTH_SHORT).show();
    }
}