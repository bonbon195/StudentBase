package ru.bonbon.sdudentdatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.bonbon.sdudentdatabase.adapter.StudentAdapter;
import ru.bonbon.sdudentdatabase.dialog.AddStudentDialogFragment;
import ru.bonbon.sdudentdatabase.entity.Student;
import ru.bonbon.sdudentdatabase.service.StudentService;

public class StudentsActivity extends AppCompatActivity implements View.OnClickListener,
        AddStudentDialogFragment.AddStudentDialogListener{
    final String URL = "https://students-db1.herokuapp.com/";
    private ListView listView;
    private List<Student> students;
    private FloatingActionButton addStudent;
    private Student selectedStudent;
    private StudentAdapter studentAdapter;
    private AddStudentDialogFragment dialogFragment;
    private int groupId;
    private TextView tvEmpty;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    StudentService studentService = retrofit.create(StudentService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        init();
    }

    private void init(){
        addStudent = findViewById(R.id.add_student);
        addStudent.setOnClickListener(this);
        groupId = getIntent().getIntExtra("groupId", 0);
        listView = findViewById(R.id.students_lv);
        tvEmpty = findViewById(R.id.empty_students_tv);
                Log.d("tag", String.valueOf(groupId));
        getAllStudents();
    }

    private void populateListView() {
        if (students.isEmpty()){
            listView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }else {
            studentAdapter = new StudentAdapter(this, R.layout.item_student, students);
            listView.setAdapter(studentAdapter);
            this.registerForContextMenu(listView);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_student:
                addGroupDialog();
                break;
        }
    }

    private void addGroupDialog() {
        dialogFragment = new AddStudentDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "addStudent");
    }

    private void updateStudentDialog(Student student) {
        dialogFragment = new AddStudentDialogFragment(student);
        dialogFragment.show(getSupportFragmentManager(), "updateStudent");
    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        Student currentStudent = dialogFragment.getCurrentStudent();
        String name = dialogFragment.getEdName().getText().toString();
        String surname = dialogFragment.getEdSurname().getText().toString();
        String patronymic = dialogFragment.getEdPatronymic().getText().toString();
        String birthDate = dialogFragment.getEdBirthdate().getText().toString();
        if (currentStudent == null) {
            Student newStudent = new Student(name, surname, patronymic, birthDate,groupId);
            Log.d("tag", newStudent.toString());
            sendStudent(newStudent);
        } else {
            currentStudent.setName(name);
            currentStudent.setSurname(surname);
            currentStudent.setPatronymic(patronymic);
            currentStudent.setBirthDate(birthDate);
            updateStudent(currentStudent);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedStudent = studentAdapter.getItem(info.position);
        Log.d("tag", selectedStudent.toString());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu_item:
                deleteStudent(selectedStudent);
                break;
            case R.id.edit_menu_item:
                updateStudentDialog(selectedStudent);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void getAllStudents() {
        Call<List<Student>> groupCall = studentService.getByGroupId(groupId);
        groupCall.enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                Log.d("tag", "response " + response.body().toString());
                students = (response.body());
                if (!students.isEmpty())
                    for (Student student : students) {
                        Log.d("tag", "getall " + student.toString());
                    }
                if (studentAdapter == null) {
                    populateListView();
                } else {
                    studentAdapter.clear();
                    studentAdapter.addAll(students);
                    studentAdapter.notifyDataSetChanged();
                    Log.d("tag", "listview updated");
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendStudent(Student student) {
        Call<Student> studentCall = studentService.create(student);
        studentCall.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                getAllStudents();
                Student student1 = response.body();
                Log.d("tag", student1.toString());
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deleteStudent(Student student){
        Call<Void> groupCall = studentService.delete(student.getId());
        groupCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (students.contains(student)) students.remove(student);
                getAllStudents();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void updateStudent(Student student){
        Call<Void> studentCall = studentService.update(student);
        studentCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                getAllStudents();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
