package ru.bonbon.sdudentdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import ru.bonbon.sdudentdatabase.adapter.GroupAdapter;
import ru.bonbon.sdudentdatabase.dialog.AddFacultyDialogFragment;
import ru.bonbon.sdudentdatabase.dialog.AddGroupDialogFragment;
import ru.bonbon.sdudentdatabase.entity.Faculty;
import ru.bonbon.sdudentdatabase.entity.Group;
import ru.bonbon.sdudentdatabase.service.FacultyService;
import ru.bonbon.sdudentdatabase.service.GroupService;

public class GroupsActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AddGroupDialogFragment.AddGroupDialogListener{
    final String URL = "https://students-db1.herokuapp.com/";
    private ListView listView;
    private List<Group> groups;
    private FloatingActionButton addGroup;
    private Group selectedGroup;
    private GroupAdapter groupAdapter;
    private AddGroupDialogFragment dialogFragment;
    private int facultyId;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    GroupService groupService = retrofit.create(GroupService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        init();
    }

    private void init(){
        addGroup = findViewById(R.id.add_group);
        addGroup.setOnClickListener(this);
        facultyId = getIntent().getIntExtra("facultyId", 0);
        Log.d("tag", String.valueOf(facultyId));
        getAllGroups();
    }

    private void populateListView() {
        listView = findViewById(R.id.groups);
        groupAdapter = new GroupAdapter(this, R.layout.item_group, groups);
        listView.setAdapter(groupAdapter);
        listView.setOnItemClickListener(this);
        this.registerForContextMenu(listView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_group:
                addGroupDialog();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(GroupsActivity.this, StudentsActivity.class);
        Group group = (Group) adapterView.getItemAtPosition(i);
        intent.putExtra("groupId", group.getId());
        startActivity(intent);
    }

    private void addGroupDialog() {
        dialogFragment = new AddGroupDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "addGroup");
    }

    private void updateGroupDialog(Group group) {
        dialogFragment = new AddGroupDialogFragment(group);
        dialogFragment.show(getSupportFragmentManager(), "updateGroup");
    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        Group currentGroup = dialogFragment.getCurrentGroup();
        String name = dialogFragment.getEditText().getText().toString();
        boolean canAdd = true;
        if (!name.equals("")) {
            for (Group group : groups) {
                if (group.getName().equalsIgnoreCase(name)) {
                    canAdd = false;
                }
            }
            if (canAdd) {
                if (currentGroup == null) {
                    Group newGroup = new Group(name, facultyId);
                    Log.d("tag", newGroup.toString());
                    sendGroup(newGroup);
                } else {
                    currentGroup.setName(name);
                    updateGroup(currentGroup);
                }
            }else{
                Toast.makeText(getApplicationContext(),
                        "Группа с таким именем уже существует",
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
        selectedGroup = groupAdapter.getItem(info.position);
        Log.d("tag", selectedGroup.toString());
        menu.setHeaderTitle(selectedGroup.getName());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu_item:
                deleteGroup(selectedGroup);
                break;
            case R.id.edit_menu_item:
                updateGroupDialog(selectedGroup);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void getAllGroups() {
        Call<List<Group>> groupCall = groupService.getByFacultyId(facultyId);
        groupCall.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                Log.d("tag", "response " + response.body().toString());
                groups = (response.body());
                for (Group group : groups) {
                    Log.d("tag", "getall " + group.toString());
                }
                if (groupAdapter == null) {
                    populateListView();
                } else {
                    groupAdapter.clear();
                    groupAdapter.addAll(groups);
                    groupAdapter.notifyDataSetChanged();
                    Log.d("tag", "listview updated");
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendGroup(Group group) {
        Call<Group> groupCall = groupService.create(group);
        groupCall.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                getAllGroups();
                Group group1 = response.body();
                Log.d("tag", group1.toString());
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deleteGroup(Group group){
        Call<Void> groupCall = groupService.delete(group.getId());
        groupCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (groups.contains(group)) groups.remove(group);
                getAllGroups();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void updateGroup(Group group){
        Call<Void> groupCall = groupService.update(group);
        groupCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                getAllGroups();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
