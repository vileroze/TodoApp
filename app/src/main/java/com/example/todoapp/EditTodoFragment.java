package com.example.todoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.todoapp.model.TodoEntity;
import com.example.todoapp.util.ShowToast;
import com.example.todoapp.viewModel.TodoViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditTodoFragment extends Fragment {
    View rootView;
    EditText title, description, date;
    RadioGroup priorityBtn;
    Button saveBtn, cancelBtn;
    CheckBox chComplete;
    Boolean error;

    private static final String TAG = "TodoTest";

    int todoId;

    public static final int CRITICAL_PRIORITY = 1;
    public static final int HIGH_PRIORITY = 2;
    public static final int NORMAL_PRIORITY = 3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_edit_todo, container, false);

        title = rootView.findViewById(R.id.edited_title);
        description = rootView.findViewById(R.id.edited_description);
        date = rootView.findViewById(R.id.edited_date);
        priorityBtn = rootView.findViewById(R.id.edited_priority);
        chComplete = rootView.findViewById(R.id.edited_completion_status);
        saveBtn = rootView.findViewById(R.id.edit_fragment_btn_save);
        cancelBtn = rootView.findViewById(R.id.edit_fragment_btn_cancel);

        //loading the updated value of each field if updated
        loadUpdateData();

        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                    DisplayTestDate();
                return false;

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTodo();
            }
        });

        //when the user cancels the edit
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertCancel();
            }
        });
        return rootView;
    }

    void DisplayTestDate() {
        Calendar calendar = Calendar.getInstance();
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);
        int cMonth = calendar.get(Calendar.MONTH);
        int cYear = calendar.get(Calendar.YEAR);
        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(year + "-" + month + "-" + dayOfMonth);
            }
        }, cYear, cMonth, cDay);
        pickerDialog.show();
    }

    //if the user wants to exit without saving the edit or without editing
    void ShowAlertCancel(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setMessage(getString(R.string.alert_cancel))
                .setTitle(getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialog.show();
    }

    //set the updated values
    void loadUpdateData() {
        todoId = getActivity().getIntent().getIntExtra("TodoId", -1);
        TodoViewModel viewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        if (todoId != -1) {
            saveBtn.setText("Update");
            TodoEntity todo = viewModel.getTodoById(todoId);
            title.setText(todo.getTitle());
            description.setText(todo.getDescription());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            date.setText(format.format( todo.getTodoDate()));
            switch (todo.getPriority()){
                case 1:
                    priorityBtn.check(R.id.edited_critical_priority);
                    break;
                case 2:
                    priorityBtn.check(R.id.edited_high_priority);
                    break;
                case 3:
                    priorityBtn.check(R.id.edited_normal_priority);
                    break;
            }
            chComplete.setChecked(todo.isCompleted());
        }
    }

    void SaveTodo(){
        error = false;
        TodoEntity todoEntity = new TodoEntity();
        Date todoDate = new Date();
        int checkedPriority = -1;
        int priority = 0;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            todoDate = format.parse(date.getText().toString());
        }catch (ParseException ex){
            ex.printStackTrace();
            date.setError("Invalid date, check again");
        }
        checkedPriority = priorityBtn.getCheckedRadioButtonId();

        switch (checkedPriority){
            case R.id.edited_critical_priority:
                priority = CRITICAL_PRIORITY;
                break;
            case R.id.edited_high_priority:
                priority=HIGH_PRIORITY;
                break;
            case R.id.edited_normal_priority:
                priority=NORMAL_PRIORITY;
                break;
        }

        SharedPreferences preferences= getContext().getSharedPreferences("todo_pref",0);
        int user_id  = preferences.getInt("user_id",0);

        if(title.getText().toString().trim().equals("") || description.getText().toString().trim().equals("")
           || date.getText().toString().trim().equals("") || checkedPriority == -1) {
            ShowToast.displayToast(getContext(),"Non of the fields can be left empty");
            error = true;
        }
        else {

            todoEntity.setTitle(title.getText().toString());
            todoEntity.setDescription(description.getText().toString());
            todoEntity.setTodoDate(todoDate);
            todoEntity.setPriority(priority);
            todoEntity.setCompleted(chComplete.isChecked());
            todoEntity.setUser_id(user_id);
        }

        TodoViewModel viewModel  = new ViewModelProvider(this).get(TodoViewModel.class);

        if(!error) {
            if(todoId != -1){
                todoEntity.setId(todoId);
                viewModel.update(todoEntity);
            } else viewModel.insert(todoEntity);

            ShowToast.displayToast(getActivity(),"Todo Saved");
            Intent intent= new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }
    }
}