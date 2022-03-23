package ru.bonbon.sdudentdatabase;

import java.util.ArrayList;
import java.util.List;

public class Group {
    String number;
    List<Student> students;

    public Group(String number, List<Student> students) {
        this.number = number;
        this.students = students;
    }

    public Group(String number) {
        this.number = number;
        students = new ArrayList<>();
    }
}
