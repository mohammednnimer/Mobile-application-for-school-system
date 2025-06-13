package com.company.project_mobile.classes;

import android.widget.EditText;

public class Student {
    private int id;
    private String name;
    private EditText markEditText;

    // Constructor
    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter and Setter for ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for EditText
    public EditText getMarkEditText() {
        return markEditText;
    }

    public void setMarkEditText(EditText markEditText) {
        this.markEditText = markEditText;
    }
}
