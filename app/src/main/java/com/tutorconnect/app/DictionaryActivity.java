package com.tutorconnect.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tutorconnect.app.adapter.DictionaryAdapter;
import com.tutorconnect.app.model.Dictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DictionaryActivity extends AppCompatActivity {

    private EditText etWord;
    private Button btnSearch;
    private RecyclerView rvMeanings;
    private DictionaryAdapter adapter;
    private ProgressDialog progressDialog;
    private AsyncHttpClient client;
    private List<Dictionary> dictionaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        setTitle("Dictionary");

        etWord = findViewById(R.id.etWord);
        btnSearch = findViewById(R.id.btnSearch);
        rvMeanings = findViewById(R.id.rvMeanings);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching word meanings...");
        progressDialog.setCancelable(false);

        dictionaryList = new ArrayList<>();

        rvMeanings.setLayoutManager(new LinearLayoutManager(this));

        client = new AsyncHttpClient();

        btnSearch.setOnClickListener(v -> {
            if (etWord.getText().toString().trim().isEmpty()) {
                etWord.setError("Cannot be empty");
            } else {
                String word = etWord.getText().toString().trim();
                fetchWordMeaning(word);
            }
        });
    }

    private void fetchWordMeaning(String word) {
        progressDialog.show();  // Show the progress dialog before making the API call

        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.dismiss();  // Dismiss the progress dialog on success

                String response = new String(responseBody);

                JSONArray arr;
                try {
                    arr = new JSONArray(response);
                    JSONObject obj = arr.getJSONObject(0);
                    JSONArray meanings = obj.getJSONArray("meanings");

                    for (int i = 0; i < meanings.length(); i++) {
                        JSONObject jsonObject = meanings.getJSONObject(i);
                        String speech = jsonObject.getString("partOfSpeech");
                        JSONArray definitions = jsonObject.getJSONArray("definitions");
                        List<String> defList = new ArrayList<>();
                        for (int j = 0; j < definitions.length(); j++) {
                            JSONObject def = definitions.getJSONObject(j);
                            defList.add(def.getString("definition"));
                        }
                        dictionaryList.add(new Dictionary(speech, defList));
                    }
                    adapter = new DictionaryAdapter(dictionaryList);
                    rvMeanings.setAdapter(adapter);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();  // Dismiss the progress dialog on failure
                Toast.makeText(DictionaryActivity.this, "Error fetching word meaning", Toast.LENGTH_SHORT).show();
                Log.e("API Error", "Request failed", error);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Handle the back button action
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
