package ru.bonbon.sdudentdatabase.entity;

import com.google.gson.annotations.SerializedName;

public class Student {
    int id;
    String name;
    String surname;
    String patronymic;
    String birthDate;
    int idGroup;

    public Student(int id, String name, String surname, String patronymic, String birthDate,
                   int idGroup) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
        this.idGroup = idGroup;
    }

    public Student() {
    }

    public Student(String name, String surname, String patronymic, String birthDate,
                   int idGroup) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
        this.idGroup = idGroup;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }
}
