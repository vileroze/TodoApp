package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.todoapp.model.UserEntity;
import com.example.todoapp.util.OnSwipeTouchListener;
import com.example.todoapp.util.ShowToast;
import com.example.todoapp.viewModel.UserViewModel;

import java.util.List;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "UserTest";
    EditText name, password, confirmPassword;
    Button signup;
    List<UserEntity> userList;
    private UserViewModel userViewModel;
    Boolean error = false;
//    ShowToast showToast;
    TextView swipeForLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        signup = findViewById(R.id.login_activity_btn_login);
        name = findViewById(R.id.login_activity_et_name);
        password = findViewById(R.id.login_activity_et_password);
        confirmPassword = findViewById(R.id.signup_activity_et_confirm_pass);
        swipeForLogin = findViewById(R.id.login_activity_swipe_btn);

        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                error = false;
                userList = userViewModel.getAllUsers();
                UserEntity userEntity = new UserEntity();
                userEntity.setUser_id(userList.size() + 1);
                userEntity.setName(name.getText().toString());
                userEntity.setPassword(password.getText().toString());

                if(password.getText().toString().trim().equals("") ||
                        confirmPassword.getText().toString().trim().equals("")) {
                    error = true;
                    ShowToast.displayToast(SignupActivity.this,"Password cannot be empty");
                }

                //validation for confirm password and password match
                if(!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    error = true;
                    password.setError("Passwords do not match");
                }

                //validation for unique username
                for (int i = 0; i< userList.size(); i++) {
                    if(name.getText().toString().equalsIgnoreCase(userList.get(i).getName())) {
                        Log.d(TAG, userList.get(i).getName());
                        name.setError("Credentials already exist");
                        error = true;
                        break;
                    }
                }

                if(!error) {
                    userViewModel.insert(userEntity);
                    ShowToast.displayToast(SignupActivity.this,"Registered successful");
                    error = false;
                }
                else {
                    ShowToast.displayToast(SignupActivity.this,"Account not created");
                }
            }
        });

        //swipe for login
        swipeForLogin.setOnTouchListener(new OnSwipeTouchListener(SignupActivity.this){
            public void onSwipeRight() {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });
    }
}