package ru.bonbon.sdudentdatabase.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Group {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("id_faculty")
    private int idFaculty;

    public Group(int id, String name, int idFaculty) {
        this.id = id;
        this.name = name;
        this.idFaculty = idFaculty;
    }

    public Group(String name, int idFaculty) {
        this.name = name;
        this.idFaculty = idFaculty;
    }

    public Group() {
    }

    public int getId() {
        return id;
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

    public int getIdFaculty() {
        return idFaculty;
    }

    public void setIdFaculty(int idFaculty) {
        this.idFaculty = idFaculty;
    }
}


