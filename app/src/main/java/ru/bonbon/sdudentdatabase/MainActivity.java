package ru.bonbon.sdudentdatabase;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.bonbon.sdudentdatabase.entity.Faculty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AddFacultyDialogFragment.AddFacultyDialogListener {
    private ListView listView;
    private List<Faculty> faculties;
    private FloatingActionButton addFaculty;
    private AddFacultyDialogFragment dialogFragment;
    private Faculty selectedFaculty;
    private FacultyAdapter facultyAdapter;
    final String URL = "https://students-db1.herokuapp.com/";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    FacultyService facultyService = retrofit.create(FacultyService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        init();
    }

    void init() {
        addFaculty = findViewById(R.id.add_faculty);
        addFaculty.setOnClickListener(this);
        getAllFaculties();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_faculty:
                addFacultyDialog();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(MainActivity.this, GroupsActivity.class);
        startActivity(intent);
    }

    private void addFacultyDialog() {
        dialogFragment = new AddFacultyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "addFaculty");
    }

    private void updateFacultyDialog(Faculty faculty) {
        dialogFragment = new AddFacultyDialogFragment(faculty);
        dialogFragment.show(getSupportFragmentManager(), "addFaculty");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Faculty currentFaculty = dialogFragment.getCurrentFaculty();
        String name = dialogFragment.getEditText().getText().toString();
        if (!name.equals("")) {
            for (Faculty faculty : faculties) {
                if (faculty.getName().equals(name)) {
                    Toast.makeText(getApplicationContext(),
                            "Факультет с таким именем уже существует",
                            Toast.LENGTH_SHORT).show();
                }else{
                    if (currentFaculty == null){
                        Faculty newFaculty = new Faculty(name);
                        Log.d("tag", newFaculty.toString());
                        sendFaculty(newFaculty);
                    }else{
                        currentFaculty.setName(name);
                        updateFaculty(currentFaculty);
                    }
                }
            }
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
        selectedFaculty = facultyAdapter.getItem(info.position);
        Log.d("tag", selectedFaculty.toString());
        menu.setHeaderTitle(selectedFaculty.getName());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu_item:
                deleteFaculty(selectedFaculty);
                break;
            case R.id.edit_menu_item:
                updateFacultyDialog(selectedFaculty);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void populateListView() {
        listView = findViewById(R.id.faculties);
        facultyAdapter = new FacultyAdapter(this, R.layout.item_faculty,
                faculties);
        listView.setAdapter(facultyAdapter);
        listView.setOnItemClickListener(this);
        this.registerForContextMenu(listView);
    }

    private void getAllFaculties() {
        Call<List<Faculty>> facultyCall = facultyService.getAll();
        facultyCall.enqueue(new Callback<List<Faculty>>() {
            @Override
            public void onResponse(Call<List<Faculty>> call, Response<List<Faculty>> response) {
                Log.d("tag", "response " + response.body().toString());
                faculties = (response.body());
                for (Faculty faculty : faculties) {
                    Log.d("tag", "getall " + faculty.toString());
                }
                if (facultyAdapter == null) {
                    populateListView();
                } else {
                    facultyAdapter.clear();
                    facultyAdapter.addAll(faculties);
                    facultyAdapter.notifyDataSetChanged();
                    Log.d("tag", "listview updated");
                }
            }

            @Override
            public void onFailure(Call<List<Faculty>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendFaculty(Faculty faculty) {
        Call<Faculty> facultyCall = facultyService.create(faculty);
        facultyCall.enqueue(new Callback<Faculty>() {
            @Override
            public void onResponse(Call<Faculty> call, Response<Faculty> response) {
                Faculty faculty = response.body();
                Log.d("tag", "added faculty: " + faculty.toString());
                getAllFaculties();
            }

            @Override
            public void onFailure(Call<Faculty> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deleteFaculty(Faculty faculty){
        Call<Void> facultyCall = facultyService.delete(faculty.getId());
        facultyCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (faculties.contains(faculty)) faculties.remove(faculty);
                getAllFaculties();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void updateFaculty(Faculty faculty){
        Call<Void> facultyCall = facultyService.update(faculty);
        facultyCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                getAllFaculties();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

