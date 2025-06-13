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
import com.company.project_mobile.classes.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

    public class Techer_subject_mark extends AppCompatActivity {

        ListView listView;
        RequestQueue queue;
        List<Item> displayedList = new ArrayList<>();
        ArrayAdapter<Item> listViewAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_techer_subject_mark);

            listView = findViewById(R.id.listViewSubjects);
            queue = Volley.newRequestQueue(this);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            int userId = Integer.parseInt(sharedPreferences.getString("user_id", "default_value"));
            String url = "http://10.0.2.2:3000/Teacherclass.php?id=" + userId;

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            List<Item> subjectList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    Log.d("TAG", "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
                                    int id = Integer.parseInt(obj.getString("class_subject_id"));
                                    String className = obj.getString("class_name");
                                    String subject_name = obj.getString("subject_name");

                                    Item item=new Item(id,"class name: " + className + "\n\n" + subject_name);
                                    subjectList.add(item);

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
                            Toast.makeText(com.company.project_mobile.Techer_subject_mark.this, error.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("Error_json", error.toString());
                        }
                    });

            queue.add(request);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                Item clickedItem = displayedList.get(position);

                    try {

                        Intent intent = new Intent(com.company.project_mobile.Techer_subject_mark.this, PublishMarksActivity.class);
                        intent.putExtra("clickedItem", clickedItem.getId()+"");

                        startActivity(intent);


                    } catch (Exception e) {
                        Toast.makeText(this, "Invalid item format", Toast.LENGTH_SHORT).show();
                    }

            });
        }

        private void updateListView(List<Item> subjectList) {
            displayedList.clear();
            displayedList.addAll(subjectList);

            listViewAdapter = new ArrayAdapter<>(com.company.project_mobile.Techer_subject_mark.this,
                    R.layout.list_item_assignment,
                    R.id.textItem,
                    displayedList
            );
            listView.setAdapter(listViewAdapter);
        }
    }
