package com.company.project_mobile.classes;

public class lecutre {
    private int row;
    private int colume;
    private String name;

    public lecutre(int row, int colume, String name) {
        this.row = row;
        this.colume = colume;
        this.name = name;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColume() {
        return colume;
    }

    public void setColume(int colume) {
        this.colume = colume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
