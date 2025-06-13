package com.company.project_mobile.classes;

public class grade {
    String title;
    double mark;

    public void setMark(int mark) {
        this.mark = mark;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public grade(String title, double mark) {
        this.title = title;
        this.mark = mark;
    }

    public String getTitle() { return title; }
    public double getMark() { return mark; }
}
