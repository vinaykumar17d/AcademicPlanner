package com.project.academicplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Sign_activity extends AppCompatActivity implements View.OnClickListener{

    private EditText Name, Password;
    private Button  SignUpbutton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private Spinner spinner;
    private String Selected = "Student";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_activity);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        findViews();
    }

    private void findViews() {
        Name = findViewById(R.id.Name);
        Password = findViewById(R.id.Password);
        SignUpbutton = findViewById(R.id.SignUp);
        spinner = findViewById(R.id.spinner);

        SignUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = Name.getText().toString() + "@academic.com";
                String pwd  = Password.getText().toString();
                mAuth.createUserWithEmailAndPassword(username, pwd )
                        .addOnCompleteListener(Sign_activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Boolean stu = true;
                                    Selected  = spinner.getSelectedItem().toString();
                                    if (Selected == "Student") {
                                        stu = true;
                                    }else {
                                        stu = false;
                                    }
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("Username", Name.getText().toString());
                                    data.put("Type", stu);


                                    mDatabase.collection("Users").document(Name.getText().toString())
                                            .set(data, SetOptions.merge());

                                    if(Selected == "Student"){
                                        Intent intent = new Intent(Sign_activity.this, Student_activity.class);
                                        intent.putExtra("Name", Name.getText().toString());
                                        intent.putExtra("Type", Selected);
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(Sign_activity.this, Admin_activity.class);
                                        intent.putExtra("Name", Name.getText().toString());
                                        intent.putExtra("Type", Selected);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }).addOnFailureListener(Sign_activity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(), "error in creating user in firebase", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}