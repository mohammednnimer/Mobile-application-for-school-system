package com.company.project_mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class useracoount extends AppCompatActivity {

    TextView name, email, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useracoount);

        name = findViewById(R.id.tvName);
        email = findViewById(R.id.tvEmail);
        role = findViewById(R.id.tvRole);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userIdStr = sharedPreferences.getString("user_id", "default_value");

        if ("default_value".equals(userIdStr)) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = Integer.parseInt(userIdStr);

        String url = "http://10.0.2.2:3000/user.php?id=" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                             JSONObject userObject = response.getJSONObject(0); // Get the first object
                            name.setText("Name: " + userObject.getString("name"));
                            email.setText("Email: " + userObject.getString("email"));
                            role.setText("Role: " + userObject.getString("role"));
                        } catch (JSONException e) {
                            Log.e("JSON Error", "Failed to parse JSON", e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error occurred: " + error.getMessage());
                        Toast.makeText(useracoount.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }
}
