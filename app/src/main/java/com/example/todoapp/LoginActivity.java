package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.model.UserEntity;
import com.example.todoapp.util.OnSwipeTouchListener;
import com.example.todoapp.util.ShowToast;
import com.example.todoapp.viewModel.UserViewModel;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText name, password;
    Button login;
    List<UserEntity> userList;
    private UserViewModel userViewModel;
    TextView swipeForSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        login = findViewById(R.id.login_activity_login_btn);
        name = findViewById(R.id.login_activity_userName);
        password = findViewById(R.id.login_activity_userPassword);
        swipeForSignup = findViewById(R.id.signup_activity_swipe_btn);

        SharedPreferences preference = getApplicationContext().getSharedPreferences("todo_pref",  0);
        SharedPreferences.Editor editor = preference.edit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList = userViewModel.getAllUsers();

                for (int i = 0; i< userList.size(); i++) {

                    //if username and password matches the database.
                    if(userList.get(i).getName().equalsIgnoreCase(name.getText().toString())
                            && userList.get(i).getPassword().equals(password.getText().toString())) {

                        editor.putBoolean("authentication",true);
                        editor.putInt("user_id", userList.get(i).getUser_id());
                        editor.commit();

                    }
                }

                Boolean authentication = preference.getBoolean("authentication",false);

                if(authentication) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    name.setError("Invalid credentials provided");
                    ShowToast.displayToast(LoginActivity.this,"Invalid credentials provided");
                }
            }
        });

        //swipe for signup page
        swipeForSignup.setOnTouchListener(new OnSwipeTouchListener(LoginActivity.this){
            public void onSwipeLeft() {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}