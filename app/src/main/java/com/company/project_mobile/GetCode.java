package com.company.project_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetCode extends AppCompatActivity {
    private EditText userName, email;
    private Button btnVerify;
    TextView text;
    private static final String URL_RECOVERY_CHECK = "http://10.0.2.2:3000/GetCode.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_code);
        bindViews();
        btnVerify.setOnClickListener(this::onVerifyClicked);
    }

    private void bindViews() {
        userName = findViewById(R.id.name);
        email = findViewById(R.id.edtEmail); // Email input
        btnVerify = findViewById(R.id.btnVerify);
        text = findViewById(R.id.ettxt); // TextView to show the recovery code
    }

    private void onVerifyClicked(View v) {
        String userNameInput = userName.getText().toString().trim();
        String emailInput = email.getText().toString().trim();

        if (userNameInput.isEmpty() || emailInput.isEmpty()) {
            Toast.makeText(this,
                    "Please fill in both fields",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        checkRecoveryCodeInDatabase(userNameInput, emailInput);
    }

    private void checkRecoveryCodeInDatabase(String userNameInput, String emailInput) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL_RECOVERY_CHECK,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean success = json.optBoolean("success", false);

                        if (success) {
                            String code = json.optString("Code", "No Code Found");
                            text.setText("Code: " + code);
                        } else {
                            Toast.makeText(
                                    this,
                                    "✖ Invalid name or email. Please try again.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(
                                this,
                                "Response parse error",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                },
                error -> Toast.makeText(
                        this,
                        "Network error: " + error.getMessage(),
                        Toast.LENGTH_LONG
                ).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", userNameInput);
                params.put("email", emailInput);
                return params;
            }
        };

        queue.add(request);
    }
}
