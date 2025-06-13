package com.company.project_mobile.classes;

public class ASS {

    private String subjectName;
    private String title;
    private String date;
    private String desc;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ASS(String subjectName, String title, String date, String desc, int id) {
        this.subjectName = subjectName;
        this.title = title;
        this.date = date;
        this.desc=desc;
        this.id=id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}

