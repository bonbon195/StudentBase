package ru.bonbon.studentbase.entity;

import com.google.gson.annotations.SerializedName;

public class Faculty {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;

    public Faculty(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Faculty(String name) {
        this.name = name;
    }

    public Faculty(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
