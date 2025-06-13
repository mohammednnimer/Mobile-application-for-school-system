package com.company.project_mobile;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

public class AddTeacherActivity extends AppCompatActivity {

    EditText edtFullName, edtUserName, edtEmail, edtPassword, edtConfirmPassword;

    Button btnAddUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_teacher);
        setUpViews();
    }
    private void setUpViews(){
        edtFullName = findViewById(R.id.edtFullName);
        edtUserName = findViewById(R.id.edtUserName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnAddUser = findViewById(R.id.btnAddUser);

        btnAddUser.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString().trim();
            String userName = edtUserName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String role =sharedPreferences.getString("role", "default_value");




            if (fullName.isEmpty() || userName.isEmpty() || email.isEmpty()  ||
                    password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            addUserToDatabase(fullName, userName, email, role, password);
        });
    }
    private void sendEmail(String email, String username, String password) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "School app account");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, welcome to our school \n The user_name is: " + username + "\n The password is: " + password);

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
    private void addUserToDatabase(String fullName, String userName, String email, String role, String password) {

        String url = "http://10.0.2.2:3000/add_Teacher.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    sendEmail(email,userName,password);
                    Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show();
                    edtFullName.setText("");
                    edtUserName.setText("");
                    edtEmail.setText("");
                    edtPassword.setText("");
                    edtConfirmPassword.setText("");
                },
                error -> {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("full_name", fullName);
                params.put("username", userName);
                params.put("email", email);
                params.put("role", role);
                params.put("password", password);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}