package ru.bonbon.studentbase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ru.bonbon.studentbase.R;
import ru.bonbon.studentbase.entity.Student;

public class StudentAdapter extends ArrayAdapter<Student> {
    List<Student> list;
    TextView studentName;
    TextView studentSurname;
    TextView studentPatronymic;
    TextView studentBirthDate;

    public StudentAdapter(@NonNull Context context, int resource, @NonNull List<Student> objects) {
        super(context, resource, objects);
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Student student = getItem(position);
        if (convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_student,
                    null);
        }
        studentName = convertView.findViewById(R.id.student_name);
        studentName.setText(student.getName());
        studentSurname = convertView.findViewById(R.id.student_surname);
        studentSurname.setText(student.getSurname());
        studentPatronymic = convertView.findViewById(R.id.student_patronymic);
        studentPatronymic.setText(student.getPatronymic());
        studentBirthDate = convertView.findViewById(R.id.student_birth_date);
        studentBirthDate.setText(student.getBirthDate());
        return convertView;
    }
}
