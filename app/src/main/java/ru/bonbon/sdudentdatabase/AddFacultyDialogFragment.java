package ru.bonbon.sdudentdatabase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddFacultyDialogFragment extends DialogFragment {

    private EditText editText;
    AddFacultyDialogListener listener;

    public interface AddFacultyDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddFacultyDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() +
                    " must implement AddFacultyDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_faculty_add, null);
        editText = view.findViewById(R.id.faculty_name_et);
        builder.setView(view)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onDialogPositiveClick(AddFacultyDialogFragment.this);
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDialogNegativeClick(AddFacultyDialogFragment.this);
                    }
                });
        return builder.create();
    }

    public EditText getEditText() {
        return editText;
    }
}
