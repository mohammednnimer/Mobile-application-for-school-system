package com.company.project_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView text ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text=findViewById(R.id.forgotPassword);
        text.setOnClickListener(e->
        {
            Intent intent = new Intent(Login.this, ForgotPassword.class);
            startActivity(intent);
        });
        edtUsername = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            } else {
                loginToServer(username, password);
            }
        });
    }

    private void loginToServer(String username, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://10.0.2.2:3000/login.php",
                response -> {
                    try {
                        Log.d("Response", response);
                        JSONObject json = new JSONObject(response);

                        if (json.getBoolean("success")) {
                            String role = json.getString("role");
                            String id=json.getString("id");
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", id);
                            editor.apply();

                            if (role.equals("Teacher")) {
                                Log.d("TAG", "loggggggggg ");
                                Intent intent = new Intent(Login.this, Teacher_Main_Dashboard.class);
                                startActivity(intent);
                            }
                            else if(role.equals("Student"))
                            {

                                Intent intent = new Intent(Login.this, Main_Dashboard.class);
                                startActivity(intent);
                            }
                            else if (role.equals("Registrar"))
                            {

                                Intent intent = new Intent(Login.this, RegistrarHomePageActivity.class);
                                startActivity(intent);

                            }
                        } else {
                            Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("JSON Error", "Failed to parse JSON", e);
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("Volley Error", "Error occurred: " + error.getMessage());
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        // Send the request using Volley
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
