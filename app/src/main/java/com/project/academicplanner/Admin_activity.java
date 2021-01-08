package com.project.academicplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Admin_activity extends AppCompatActivity {

    private EditText edit;
    private Button Submit;
    private FirebaseFirestore mDatabase;
    private String Name , Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_activity);
        mDatabase = FirebaseFirestore.getInstance();
        Name = getIntent().getExtras().getString("Name");
        Type = getIntent().getExtras().getString("Type");

        findViews();
    }

    private void findViews() {
        edit = findViewById(R.id.Edit);
        Submit = findViewById(R.id.Submit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value  = edit.getText().toString();

                Map<String, Object> data = new HashMap<>();
                data.put("value", edit.getText().toString());
                data.put("Name", Name);
                data.put("Type", Type);

                mDatabase.collection("Courses").document(String.valueOf(System.currentTimeMillis()))
                        .set(data, SetOptions.merge());
            }
        });
    }
}