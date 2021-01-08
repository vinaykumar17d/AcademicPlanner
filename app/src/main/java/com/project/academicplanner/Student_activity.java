package com.project.academicplanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Student_activity extends AppCompatActivity {

    private String Name , Type;
    private FirebaseFirestore mDatabase;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_activity);
        Name = getIntent().getExtras().getString("Name");
        Type = getIntent().getExtras().getString("Type");
        textView = findViewById(R.id.textView);

        mDatabase = FirebaseFirestore.getInstance();

        mDatabase.collection("Courses")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        List<String> cities = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("value") != null) {
                                cities.add(doc.getString("Name") + ":" + doc.getString("value"));
                            }
                        }
                        textView.setText(String.valueOf(cities));

                    }
                });
    }
}