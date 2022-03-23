package ru.bonbon.sdudentdatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FacultyAdapter extends ArrayAdapter<Faculty> {
    List<Faculty> list;

    public FacultyAdapter(@NonNull Context context, int resource, @NonNull List<Faculty> objects) {
        super(context, resource, objects);
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Faculty faculty = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_faculty, null);
        }
        TextView facultyName = convertView.findViewById(R.id.faculty_name);
        facultyName.setText(faculty.getName());
        return convertView;
    }
}
