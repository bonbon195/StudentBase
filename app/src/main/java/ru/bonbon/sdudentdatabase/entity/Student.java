package ru.bonbon.sdudentdatabase.entity;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("surname")
    String surname;
    @SerializedName("patronymic")
    String patronymic;
    @SerializedName("birth_date")
    String birthDate;
    @SerializedName("id_group")
    int id_group;

    public Student(int id, String name, String surname, String patronymic, String birthDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
    }

    public Student() {
    }
}
