package ru.bonbon.studentbase.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ru.bonbon.studentbase.R;
import ru.bonbon.studentbase.entity.Group;

public class AddGroupDialogFragment extends DialogFragment {

    private EditText editText;
    AddGroupDialogListener listener;
    private Group currentGroup;

    public AddGroupDialogFragment(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    public AddGroupDialogFragment() {
    }

    public interface AddGroupDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddGroupDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() +
                    " must implement AddGroupDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_group_add, null);
        editText = view.findViewById(R.id.group_name_et);
        if (currentGroup != null){
            Log.d("tag", "not null " + currentGroup.toString());
            editText.setText(currentGroup.getName(), TextView.BufferType.EDITABLE);
        }

        builder.setView(view)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDialogPositiveClick(AddGroupDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDialogNegativeClick(AddGroupDialogFragment.this);
                    }
                });
        return builder.create();
    }
    public void nullifyCurrentGroup(){
        this.currentGroup = null;
    }
    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    public EditText getEditText() {
        return editText;
    }
}
