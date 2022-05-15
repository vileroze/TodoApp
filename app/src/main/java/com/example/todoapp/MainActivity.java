package com.example.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.model.UserEntity;
import com.example.todoapp.util.ShowToast;
import com.example.todoapp.viewModel.TodoViewModel;
import com.example.todoapp.viewModel.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

/**
 * First screen after a successful login.
 * Hosts the ListTodoFragment, also contains a button for adding a new todoITEM
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "UserList";
    private TodoViewModel todoViewModel;
    private UserViewModel userViewModel;

    FragmentManager fragmentManager;
    Fragment fragment;
    SharedPreferences preferences;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        preferences = getApplicationContext().getSharedPreferences("todo_pref", 0);
        //to know which user last logged in
        int user_id = preferences.getInt("user_id",0);
        UserEntity userEntity = userViewModel.getUserById(user_id);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //makes toolBar behave as actionBar (useful for displaying menu items)
        setSupportActionBar(toolbar);

        //display userName
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(userEntity.getName().toUpperCase(Locale.ROOT));
        toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        toolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //set the icon
        //getSupportActionBar().setIcon(R.drawable.ic_account);

        //open window to create new todoItem
        floatingActionButton = findViewById(R.id.btn_activity_main_floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        //replace the fragment with the todoList
        fragmentManager = getSupportFragmentManager();
        fragment = new ListTodoFragment();
        fragmentManager.beginTransaction()
                    .replace(R.id.list_todo_container, fragment)
                    .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get the stored user id
        int userId = preferences.getInt("user_id",-1);

        switch (item.getItemId()){

            case R.id.delete_completed:
                if(userId != -1) {
                    todoViewModel.deleteAllCompleted(userId, true);
                }
                else {
                    ShowToast.displayToast(getApplicationContext(),"Item not deleted, try again later");
                }
                break;

            case R.id.delete_all:
                todoViewModel.deleteAll(userId);
                ShowToast.displayToast(getApplicationContext(),"All items deleted");
                break;

            case R.id.logout:
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent intent= new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}