package ru.bonbon.sdudentdatabase;

public class Student {
    int id;
    String name;
    String surname;
    String patronymic;
    String birthDate;

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
