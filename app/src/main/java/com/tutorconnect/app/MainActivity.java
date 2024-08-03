package com.tutorconnect.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.tutorconnect.app.parent.ParentSignIn;
import com.tutorconnect.app.student.StudentSignIn;
import com.tutorconnect.app.tutor.TutorSignIn;

public class MainActivity extends AppCompatActivity {

    LinearLayout btn_tutor;
    LinearLayout btn_student;
    LinearLayout btn_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_tutor = findViewById(R.id.btn_tutor);
        btn_student = findViewById(R.id.btn_student);
        btn_parent = findViewById(R.id.btn_parent);

        btn_tutor.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TutorSignIn.class)));

        btn_student.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, StudentSignIn.class)));

        btn_parent.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ParentSignIn.class)));


    }
}