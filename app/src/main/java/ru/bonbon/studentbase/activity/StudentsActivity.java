package ru.bonbon.studentbase.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.bonbon.studentbase.R;
import ru.bonbon.studentbase.adapter.StudentAdapter;
import ru.bonbon.studentbase.dialog.AddStudentDialogFragment;
import ru.bonbon.studentbase.entity.Student;
import ru.bonbon.studentbase.service.StudentService;

public class StudentsActivity extends AppCompatActivity implements View.OnClickListener,
        AddStudentDialogFragment.AddStudentDialogListener, AdapterView.OnItemSelectedListener{
    final String URL = "https://students-db1.herokuapp.com/";
    ListView listView;
    List<Student> students;
    FloatingActionButton addStudent;
    Student selectedStudent;
    StudentAdapter studentAdapter;
    AddStudentDialogFragment dialogFragment;
    int groupId;
    String groupName;
    TextView tvEmpty;
    Toolbar toolbar;
    Spinner spinner;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    StudentService studentService = retrofit.create(StudentService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        addStudent = findViewById(R.id.add_student);
        addStudent.setOnClickListener(this);
        spinner = findViewById(R.id.sort_by_spinner);
        setSpinnerAdapter();
        spinner.setOnItemSelectedListener(this);
        groupId = getIntent().getIntExtra("groupId", 0);
        groupName = getIntent().getStringExtra("groupName");
        listView = findViewById(R.id.students_lv);
        tvEmpty = findViewById(R.id.empty_tv);
        Log.d("tag", String.valueOf(groupId));
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(groupName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getAllStudents();

    }

    private void populateListView() {
        if (students.isEmpty()){
            listView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }else {
            listView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
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

    private void setSpinnerAdapter(){
        List<String> list = Arrays.asList("Имени", "Фамилии");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
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
        Log.d("tag", "length " + String.valueOf(name.toCharArray().length));
        if (name.isEmpty() || surname.isEmpty() || birthDate.isEmpty()){
            Toast.makeText(this, "Не заполнены обязательные поля",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentStudent == null) {
            Student newStudent = new Student(name, surname, patronymic, birthDate,groupId);
            Log.d("tag", newStudent.toString());
            createStudent(newStudent);

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
                    populateListView();
                    Log.d("tag", "listview updated");
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void createStudent(Student student) {
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if ( students == null || students.isEmpty()) return;
        Log.d("tag", String.valueOf(i));
        if (i == 0){
            students.sort(Comparator.comparing(Student::getName));
            populateListView();
        }
         else if (i==1){
            students.sort(Comparator.comparing(Student::getSurname));
            populateListView();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
