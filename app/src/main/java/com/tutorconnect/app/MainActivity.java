package com.tutorconnect.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tutorconnect.app.parent.ParentSignIn;
import com.tutorconnect.app.student.StudentSignIn;
import com.tutorconnect.app.tutor.TutorSignIn;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_tutor).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,
                TutorSignIn.class)));

        findViewById(R.id.btn_student).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,
                StudentSignIn.class)));

        findViewById(R.id.btn_parent).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,
                ParentSignIn.class)));

        findViewById(R.id.btn_dictionary).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,
                DictionaryActivity.class)));

    }
}