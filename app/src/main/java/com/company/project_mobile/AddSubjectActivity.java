package com.company.project_mobile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


public class AddSubjectActivity extends AppCompatActivity {


        private EditText edtSubjectName, edtDescription;
        private Button btnAddSubject;

        private static final String BASE_IMAGE_URL = "http://10.0.2.2:3000/images/";
        private String selectedImageName = "";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_subject);

            edtSubjectName = findViewById(R.id.edtSbjectName);
            edtDescription = findViewById(R.id.edtDescription);
            btnAddSubject = findViewById(R.id.btnAddSubject);
            btnAddSubject.setOnClickListener(v -> addSubjectToDatabase());
        }

        private void addSubjectToDatabase() {
            String subjectName = edtSubjectName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            String url = "http://10.0.2.2:3000/addsubject.php";
            if (subjectName.isEmpty() || description.isEmpty() ) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AddSubjectActivity.this, RegistrarHomePageActivity.class);
                        startActivity(intent);
                    },
                    error -> {
                        Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("subject_name", subjectName);
                    params.put("description", description);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        }
    }