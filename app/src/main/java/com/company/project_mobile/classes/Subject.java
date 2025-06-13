package com.company.project_mobile.classes;


import java.util.List;

public class Subject {
    private String name;
    private int id;
    private String url;
    private String disc;

    public void setGrades(List<grade> grades) {
        this.grades = grades;
    }

    public List<grade> getGrades() {
        return grades;
    }

    private String teacher;
    private List<grade> grades;

    public Subject(int id, String name, String url,String disc,String teacher) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.disc = disc;
        this.teacher = teacher;
    }
    public Subject( String name,List<grade> grades) {

        this.name = name;
        this.grades=grades;
    }
    public Subject() {

    }

    public int getId() {
        return id;
    }
    public String getdisc() {
        return disc;
    }
    public String getTeacher() {
        return teacher;
    }



    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
