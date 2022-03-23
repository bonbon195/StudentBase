package ru.bonbon.sdudentdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    ListView listView;
    List<Faculty> faculties;
    FloatingActionButton addFaculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        init();
    }
    void init(){
        addFaculty = findViewById(R.id.add_faculty);
        addFaculty.setOnClickListener(this);
        faculties = new ArrayList<>();
        faculties.add(new Faculty("Информационные системы и программирование"));
        faculties.add(new Faculty("Компьютерные системы и комплексы"));
        faculties.add(new Faculty("Сетевое и системное администрирование"));
        listView = findViewById(R.id.faculties);
        FacultyAdapter facultyAdapter = new FacultyAdapter(this, R.layout.item_faculty, faculties);
        listView.setAdapter(facultyAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(MainActivity.this, GroupsActivity.class);
        startActivity(intent);
    }
}