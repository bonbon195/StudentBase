package ru.bonbon.sdudentdatabase;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
        AdapterView.OnItemClickListener, AddFacultyDialogFragment.AddFacultyDialogListener{
    ListView listView;
    List<Faculty> faculties;
    FloatingActionButton addFaculty;
    AddFacultyDialogFragment dialogFragment;
//    FacultyService facultyService;
    static final String URL = "https://students-db1.herokuapp.com/";
    static Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    static FacultyService facultyService = retrofit.create(FacultyService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        init();
    }
    void init(){
        addFaculty = findViewById(R.id.add_faculty);
        addFaculty.setOnClickListener(this);
        listView = findViewById(R.id.faculties);
        try {
            faculties = new GetAllAsyncTask().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FacultyAdapter facultyAdapter = new FacultyAdapter(this, R.layout.item_faculty, faculties);
        listView.setAdapter(facultyAdapter);
        listView.setOnItemClickListener(this);
        this.registerForContextMenu(listView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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

    private void addFacultyDialog(){
        dialogFragment = new AddFacultyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "addFaculty");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String name = dialogFragment.getEditText().getText().toString();
        if (!name.equals("")) {
            for (Faculty faculty : faculties) {
                if (faculty.getName().equals(name)) {
                    Toast.makeText(getApplicationContext(),
                            "Факультет с таким именем уже существует",
                            Toast.LENGTH_SHORT).show();
                }
            }
            Faculty faculty = new Faculty(name);
            Log.d("tag", faculty.toString());
//            sendFaculty(faculty);
            new SendAsyncTask().execute(faculty);
            try {
                List<Faculty> facultyList = new GetAllAsyncTask().execute().get();
                faculties.add(facultyList.get(facultyList.size() - 1));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        switch (v.getId()){
//            case R.id.:
//                break;
//        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.: ;
//                break;
//        }

        return super.onContextItemSelected(item);
    }

    class GetAllAsyncTask extends AsyncTask<Void, Void, List<Faculty>>{

        @Override
        protected List<Faculty> doInBackground(Void... voids) {
            Call<List<Faculty>> facultyCall = facultyService.getAll();
            try {
                Response<List<Faculty>> response = facultyCall.execute();
                List<Faculty> facultyList = response.body();
                Log.d("tag", response.body().toString());
                return facultyList;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Faculty> facultyList) {
            for (Faculty faculty: facultyList){
                Log.d("tag", faculty.toString());
            }
            super.onPostExecute(facultyList);
        }
    }

    class SendAsyncTask extends AsyncTask<Faculty, Void, Void>{

        @Override
        protected Void doInBackground(Faculty... faculties) {
            Call<Faculty> facultyCall = facultyService.create(faculties[0]);
            facultyCall.enqueue(new Callback<Faculty>() {
                @Override
                public void onResponse(Call<Faculty> call, Response<Faculty> response) {
                    Faculty faculty = response.body();
                    Log.d("tag", "what" + faculty.toString());
                }

                @Override
                public void onFailure(Call<Faculty> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            return null;
        }
    }

}