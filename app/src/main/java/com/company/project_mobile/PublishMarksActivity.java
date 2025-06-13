package com.company.project_mobile;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.project_mobile.classes.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishMarksActivity extends AppCompatActivity {

    RequestQueue queue;
    int userId;
    EditText edt;
    ArrayList<com.company.project_mobile.classes.Student> newn=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_publish_marks);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edt=findViewById(R.id.etMarkTitle);
        Spinner spinnerMarkType = findViewById(R.id.spinnerMarkType);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.mark_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarkType.setAdapter(adapter);

        Button btnPublishMarks = findViewById(R.id.btnPublishMarks);
        btnPublishMarks.setOnClickListener(v -> publishMarks());


    }


    @Override
    protected void onStart() {
        super.onStart();
        queue = Volley.newRequestQueue(this);

        userId = -1;
        String userIdString = getIntent().getStringExtra("clickedItem");

        Log.d("TAG", "jhhhhhhhh "+userIdString);
        if (userIdString != null) {
            try {
                userId = Integer.parseInt(userIdString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        String url = "http://10.0.2.2:3000/student.php?id=" + userId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        newn = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);


                                int id = Integer.parseInt(obj.getString("student_id"));
                                String name = obj.getString("student_name");

                                Student s = new Student(id, name);
                                newn.add(s);
                            } catch (JSONException exception) {
                                Log.d("Error", exception.toString());
                            }
                        }

                        RecyclerView recyclerView = findViewById(R.id.recyclerViewStudents);
                        MarkAdpter adapter = new MarkAdpter(PublishMarksActivity.this, newn);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PublishMarksActivity.this));

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PublishMarksActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });

        queue.add(request);
    }
    private void publishMarks() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewStudents);
        MarkAdpter adapter = (MarkAdpter) recyclerView.getAdapter();

        Spinner spinnerMarkType = findViewById(R.id.spinnerMarkType);
        String markType = spinnerMarkType.getSelectedItem().toString();
        if (adapter != null) {
            HashMap<Integer, String> marksMap = adapter.getMarksMap();

            if(edt.getText().toString().isEmpty())
            {
                Toast.makeText(PublishMarksActivity.this, "Please Enter Title", Toast.LENGTH_SHORT).show();

                return ;
            }

            int i=0;
            for (Map.Entry<Integer, String> entry : marksMap.entrySet()) {
                int position = entry.getKey();

                String enteredMark = entry.getValue();

                if(!enteredMark.equals("")) {


                    Log.d("TAG", enteredMark+"");
                    Log.d("TAG", markType);
                    sendMarkToBackend(newn.get(i).getId(),Double.valueOf(enteredMark),markType);
                }
                i++;


            }
            Intent intent=new Intent(PublishMarksActivity.this,Techer_subject_mark.class);
            startActivity(intent);
            finish();;
        }
    }



    private boolean sendMarkToBackend(int studentId, double mark, String markType) {
        String url = "http://10.0.2.2:3000/publish_marks.php";



        final boolean[] isSuccessful = {false};
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        isSuccessful[0] = true;
                        Log.d("PublishMarks", "Response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isSuccessful[0] = false;
                        Log.d("PublishMarks", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", String.valueOf(studentId));
                params.put("mark", String.valueOf(mark));
                params.put("mark_type", markType);
                params.put("title", edt.getText().toString().trim());
                params.put("class_subject_id", userId + ""); // Assume you get class_subject_id dynamically
                return params;
            }
        };

        queue = Volley.newRequestQueue(this);
        queue.add(request);

        return isSuccessful[0];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (queue != null) {
            queue.cancelAll("TAG");
        }
    }

}

