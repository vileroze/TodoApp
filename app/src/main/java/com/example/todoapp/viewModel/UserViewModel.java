package com.example.todoapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.todoapp.data.UserRepository;
import com.example.todoapp.model.UserEntity;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    UserRepository repository;
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void insert(UserEntity user) {
        repository.insert(user);
    }

    public void update(UserEntity user) {repository.update(user);}

    public void deleteById(UserEntity user) {repository.deleteById(user);}

    public UserEntity getUserById(int id) {
        return repository.getUserById(id);
    }

    public List<UserEntity> getAllUsers() {
       return repository.getAllUsers();
    }

}
