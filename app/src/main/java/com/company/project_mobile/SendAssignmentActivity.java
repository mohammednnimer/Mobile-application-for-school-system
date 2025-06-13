package com.company.project_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SendAssignmentActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDueDate;
    private Spinner spinnerSubject;
    private Button btnSend;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    private ArrayAdapter<String> subjectAdapter;
    private Calendar calendar;


    private String classsubjectId; // class_subject_id which is passed via Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_assignment);

        Intent intent = getIntent();
        classsubjectId = intent.getStringExtra("clickedItem");


         initializeViews();
    }

    private void initializeViews() {
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDueDate = findViewById(R.id.etDueDate);

        btnSend = findViewById(R.id.btnSend);

        sharedPreferences = getSharedPreferences("TeacherPrefs", MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);

        calendar = Calendar.getInstance();


        // Date Picker for due date
        etDueDate.setOnClickListener(v -> showDatePicker());
        btnSend.setOnClickListener(v -> sendAssignment());

        // Load the subjects from the server (if necessary)
        loadSubjects();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateLabel();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateDateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        etDueDate.setText(sdf.format(calendar.getTime()));
    }

    private void loadSubjects() {
        // You can implement this function to load subjects from the server if needed
    }

    private void sendAssignment() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String dueDate = etDueDate.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:3000/insertAss.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Successful response
                    Toast.makeText(this, "Assignment sent successfully", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity after successful submission
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error sending assignment", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("classsubjectId", String.valueOf(classsubjectId));  // Use class_subject_id from the selected subject
                params.put("title", title);
                params.put("description", description);
                params.put("due_date", dueDate);
                return params;
            }
        };

        requestQueue.add(request);
    }
}
