package com.project.academicplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText Name, Password;
    private Button LogInbutton, SignUpbutton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        findViews();
    }

    private void findViews() {
        Name = findViewById(R.id.Name);
        Password = findViewById(R.id.Password);
        LogInbutton = findViewById(R.id.LogInbutton);
        SignUpbutton = findViewById(R.id.SignUp);

        LogInbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = Name.getText().toString() +"@academic.com";
                String pwd = Password.getText().toString();
                mAuth.signInWithEmailAndPassword( username, pwd )
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            DocumentReference docRef = mDatabase.collection("Users").document(Name.getText().toString());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Boolean Selected = document.getBoolean("Type");

                                            if(Selected){
                                                Intent intent = new Intent(MainActivity.this, Student_activity.class);
                                                intent.putExtra("Name", Name.getText().toString());
                                                intent.putExtra("Type", "Student");
                                                startActivity(intent);
                                            }else {
                                                Intent intent = new Intent(MainActivity.this, Admin_activity.class);
                                                intent.putExtra("Name", Name.getText().toString());
                                                intent.putExtra("Type", "Admin");
                                                startActivity(intent);
                                            }
                                        } else {
                                        }
                                    } else {
                                    }
                                }
                            });
                        }
                    }
                }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(), "error in creating user in firebase", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        SignUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Sign_activity.class);
                startActivity(intent);
            }
        });


    }


}