package ru.bonbon.sdudentdatabase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ru.bonbon.sdudentdatabase.R;
import ru.bonbon.sdudentdatabase.entity.Group;

public class GroupAdapter extends ArrayAdapter<Group> {
    List<Group> list;
    TextView groupName;

    public GroupAdapter(@NonNull Context context, int resource, @NonNull List<Group> objects) {
        super(context, resource, objects);
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Group group = getItem(position);
        if (convertView ==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_group, null);
        }
        groupName = convertView.findViewById(R.id.group_name);
        groupName.setText(group.getName());
        return convertView;
    }
}
