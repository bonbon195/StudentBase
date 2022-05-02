package ru.bonbon.sdudentdatabase.dialog;

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

import ru.bonbon.sdudentdatabase.R;
import ru.bonbon.sdudentdatabase.entity.Student;

public class AddStudentDialogFragment extends DialogFragment {

    private EditText edName;
    private EditText edSurname;
    private EditText edPatronymic;
    private EditText edBirthdate;
    AddStudentDialogFragment.AddStudentDialogListener listener;
    private Student currentStudent;

    public AddStudentDialogFragment(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    public AddStudentDialogFragment() {
    }

    public interface AddStudentDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddStudentDialogFragment.AddStudentDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() +
                    " must implement AddStudentDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_student_add, null);
        edName = view.findViewById(R.id.student_name_et);
        edSurname = view.findViewById(R.id.student_surname_et);
        edPatronymic = view.findViewById(R.id.student_patronymic_et);
        edBirthdate = view.findViewById(R.id.student_birth_date_et);
        if (currentStudent != null){
            Log.d("tag", "not null " + currentStudent.toString());
            edName.setText(currentStudent.getName(), TextView.BufferType.EDITABLE);
            edSurname.setText(currentStudent.getSurname(), TextView.BufferType.EDITABLE);
            edPatronymic.setText(currentStudent.getPatronymic(), TextView.BufferType.EDITABLE);
            edBirthdate.setText(currentStudent.getBirthDate(), TextView.BufferType.EDITABLE);
        }

        builder.setView(view)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDialogPositiveClick(AddStudentDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDialogNegativeClick(AddStudentDialogFragment.this);
                    }
                });
        return builder.create();
    }
    public void nullifyCurrentStudent(){
        this.currentStudent = null;
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    public EditText getEdName() {
        return edName;
    }

    public void setEdName(EditText edName) {
        this.edName = edName;
    }

    public EditText getEdSurname() {
        return edSurname;
    }

    public void setEdSurname(EditText edSurname) {
        this.edSurname = edSurname;
    }

    public EditText getEdPatronymic() {
        return edPatronymic;
    }

    public void setEdPatronymic(EditText edPatronymic) {
        this.edPatronymic = edPatronymic;
    }

    public EditText getEdBirthdate() {
        return edBirthdate;
    }

    public void setEdBirthdate(EditText edBirthdate) {
        this.edBirthdate = edBirthdate;
    }
}