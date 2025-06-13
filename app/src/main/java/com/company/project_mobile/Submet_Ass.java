package com.company.project_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.company.project_mobile.classes.ASS;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Submet_Ass extends AppCompatActivity {

    private TextView assTitle, assDesc, dueDate;
    private Button uploadFileButton, sendAssignmentButton;

    private Uri selectedFileUri = null;

    private int id;

    private ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submet_ass);
        assTitle = findViewById(R.id.assTitle);
        assDesc = findViewById(R.id.assDesc);
        dueDate = findViewById(R.id.DueDate);

        uploadFileButton = findViewById(R.id.uploadFile);
        sendAssignmentButton = findViewById(R.id.sendAssignment);

         Intent intent = getIntent();
        String json = intent.getStringExtra("this.Ass");
        if (json != null) {
            Gson gson = new Gson();
            ASS assignment = gson.fromJson(json, ASS.class);
            if (assignment != null) {
                assTitle.setText(assignment.getTitle());
                assDesc.setText(assignment.getDesc());
                dueDate.setText("Due Date : " + assignment.getDate());

                id=assignment.getId();
            }
        }

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedFileUri = uri;
                        Toast.makeText(this, "File selected: " + uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        uploadFileButton.setOnClickListener(v -> {
            filePickerLauncher.launch("*/*");
        });

        sendAssignmentButton.setOnClickListener(v -> {
            if (selectedFileUri == null) {
                Toast.makeText(this, "Please upload a file first.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Assignment sent with file: " + selectedFileUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();

            uploadFile(selectedFileUri);
        });
    }
    private void uploadFile(Uri fileUri) {
        new Thread(() -> {
            try {
                String boundary = "*****" + System.currentTimeMillis() + "*****";
                String LINE_FEED = "\r\n";

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                int userId =Integer.parseInt(sharedPreferences.getString("user_id", "default_value"));
                URL url = new URL("http://10.0.2.2:3000/abload.php?idass=" + id + "&id=" + userId);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                String fileName = getFileName(fileUri);

                outputStream.writeBytes("--" + boundary + LINE_FEED);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + LINE_FEED);
                outputStream.writeBytes("Content-Type: application/octet-stream" + LINE_FEED);
                outputStream.writeBytes(LINE_FEED);

                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();

                outputStream.writeBytes(LINE_FEED);
                outputStream.writeBytes("--" + boundary + "--" + LINE_FEED);
                outputStream.flush();
                outputStream.close();

                 int responseCode = connection.getResponseCode();
                InputStream responseStream = responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                runOnUiThread(() -> {
                    Toast.makeText(this, "Response: " + response.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Submet_Ass.this, Assignments.class);
                    startActivity(intent);
                    finish();

                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();



    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

}
