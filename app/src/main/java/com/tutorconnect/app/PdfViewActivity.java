package com.tutorconnect.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PdfViewActivity extends AppCompatActivity {
    private static final String EXTRA_FILE_URL = "EXTRA_FILE_URL";
    private ProgressBar progressBar;

    public static void start(Context context, String fileUrl) {
        Intent intent = new Intent(context, PdfViewActivity.class);
        intent.putExtra(EXTRA_FILE_URL, fileUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        progressBar = findViewById(R.id.progressBar);

        String fileUrl = getIntent().getStringExtra(EXTRA_FILE_URL);
        if (fileUrl != null && !fileUrl.isEmpty()) {
            openUrlInBrowser(fileUrl);
        } else {
            Toast.makeText(this, "Invalid file URL.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void openUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
        finish();

//        // Check if there's an app to handle the intent
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        } else {
//            // Handle the case where no app can handle the URL
//            Toast.makeText(this, "No application found to open the URL", Toast.LENGTH_SHORT).show();
//        }
    }
}
