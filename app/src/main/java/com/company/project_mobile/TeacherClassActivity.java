package com.company.project_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class TeacherClassActivity extends AppCompatActivity {

    ListView listView;
    RequestQueue queue;
    List<String> displayedList = new ArrayList<>();
    ArrayAdapter<String> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class);

        listView = findViewById(R.id.listViewSubjects);
        queue = Volley.newRequestQueue(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = Integer.parseInt(sharedPreferences.getString("user_id", "default_value"));
        String url = "http://10.0.2.2:3000/Teacherclass.php?id=" + userId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> subjectList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                int id = Integer.parseInt(obj.getString("class_subject_id"));
                                String className = obj.getString("class_name");
                                String subject_name = obj.getString("subject_name");

                                subjectList.add(id + ") class name: " + className + "\n\n" + subject_name);

                            } catch (JSONException exception) {
                                Log.d("Error", exception.toString());
                            }
                        }

                         updateListView(subjectList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TeacherClassActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });

        queue.add(request);

         listView.setOnItemClickListener((parent, view, position, id) -> {
            String clickedItem = displayedList.get(position);
            int endIndex = clickedItem.indexOf(")");
            if (endIndex > 0) {
                try {
                    String numberStr = clickedItem.substring(0, endIndex);

                    Intent intent = new Intent(TeacherClassActivity.this, SendAssignmentActivity.class);
                    intent.putExtra("clickedItem", numberStr);
                    startActivity(intent);


                } catch (Exception e) {
                    Toast.makeText(this, "Invalid item format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateListView(List<String> subjectList) {
        displayedList.clear();
        displayedList.addAll(subjectList);

        listViewAdapter = new ArrayAdapter<>(TeacherClassActivity.this,
                R.layout.list_item_assignment,
                R.id.textItem,
                displayedList
        );
        listView.setAdapter(listViewAdapter);
    }
}
