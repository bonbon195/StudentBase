package ru.bonbon.sdudentdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import ru.bonbon.sdudentdatabase.adapter.FacultyAdapter;
import ru.bonbon.sdudentdatabase.dialog.AddFacultyDialogFragment;
import ru.bonbon.sdudentdatabase.entity.Faculty;
import ru.bonbon.sdudentdatabase.service.FacultyService;

public class FacultyActivity extends AppCompatActivity implements View.OnClickListener,
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

        init();
    }

    void init() {
        setContentView(R.layout.activity_faculty);
        addFaculty = findViewById(R.id.add_faculty);
        addFaculty.setOnClickListener(this);
        getAllFaculties();
//        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
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
        Intent intent = new Intent(FacultyActivity.this, GroupsActivity.class);
        Faculty faculty = (Faculty) adapterView.getItemAtPosition(i);
        Log.d("tag", String.valueOf(faculty.getId()));
        intent.putExtra("facultyId", faculty.getId());
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
        boolean canAdd = true;
        if (!name.equals("")) {
            for (Faculty faculty : faculties) {
                if (faculty.getName().equalsIgnoreCase(name)) {
                    canAdd = false;
                }
            }
            if (canAdd) {
                if (currentFaculty == null) {
                    Faculty newFaculty = new Faculty(name);
                    Log.d("tag", newFaculty.toString());
                    sendFaculty(newFaculty);
                } else {
                    currentFaculty.setName(name);
                    updateFaculty(currentFaculty);
                }
            }else{
                Toast.makeText(getApplicationContext(),
                        "Факультет с таким именем уже существует",
                        Toast.LENGTH_SHORT).show();
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
        Call<Void> facultyCall = facultyService.create(faculty);
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

