package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.model.TodoEntity;
import com.example.todoapp.model.UserEntity;
import com.example.todoapp.util.ShowToast;
import com.example.todoapp.viewModel.TodoViewModel;
import com.example.todoapp.viewModel.UserViewModel;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TodoViewModel todoViewModel;
    UserViewModel userViewModel;
    Integer user_id;
    TextView name, old_pass, new_pass;
    Button submit, delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        name = findViewById(R.id.profile_activity_user_name);
        old_pass = findViewById(R.id.profile_activity_oldPassword);
        new_pass = findViewById(R.id.profile_activity_newPassword);
        submit = findViewById(R.id.profile_activity_submit_btn);
        delete = findViewById(R.id.profile_activity_delete_btn);

        SharedPreferences preferences=getApplicationContext().getSharedPreferences("todo_pref",0);
        user_id = preferences.getInt("user_id",-1);

        //get user ID
        UserEntity userEntity = userViewModel.getUserById(user_id);

        name.setText(userEntity.getName());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(old_pass.getText().toString().trim().toString().equals("") ||
                        new_pass.getText().toString().trim().toString().equals("")) {
                    ShowToast.displayToast(ProfileActivity.this,"All the fields need to be filled");
                }
                else {
                    if(old_pass.getText().toString().equals(userEntity.getPassword())) {
                        userEntity.setPassword(new_pass.getText().toString());
                        userViewModel.update(userEntity);
                        ShowToast.displayToast(ProfileActivity.this,"Password updated");
                    }
                    else old_pass.setError("Incorrect Password");
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
                alertDialog.setMessage(getString(R.string.alert_delete))
                        .setTitle(getString(R.string.app_name))
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

                            //deletes the user todos and sends the user back to login page
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //deletes the todos associated with this userID
                                List<TodoEntity> todoEntity = todoViewModel.getAll();
                                for(int i = 0; i< todoEntity.size(); i++) {
                                    if(todoEntity.get(i).getUser_id() == user_id) {
                                        todoViewModel.deleteById(todoEntity.get(i));
                                    }
                                }
                                userViewModel.deleteById(userEntity);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
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
        });
    }
}