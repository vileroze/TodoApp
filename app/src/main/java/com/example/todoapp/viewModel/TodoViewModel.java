package com.example.todoapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todoapp.data.TodoRepository;
import com.example.todoapp.model.TodoEntity;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {
    private TodoRepository repository;
    private LiveData<List<TodoEntity>> allTodos;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
        allTodos = repository.getAllTodoList();
    }

    //used to display the live data meaning modifications to the list will be notified to the owner
    public LiveData<List<TodoEntity>> getAllTodos() {return allTodos;}

    //adding data using the form
    public void insert(TodoEntity todo) { repository.insert(todo);}

    //updating data using the form
    public void update(TodoEntity todo) {repository.update(todo);}


//    public void updateIsCompleted(int id, boolean is_completed) { repository.updateIsCompleted(id, is_completed);}

    public void deleteById(TodoEntity todoEntity){
        repository.deleteById(todoEntity);
    }

    //when the user selects delete all from the menu
    public void deleteAll(int id) {repository.deleteAll(id);}

    //used to delete all the todos when the user account is deleted
    public List<TodoEntity> getAll() {return repository.getAll();}

    //used to select particular todos when updating
    public TodoEntity getTodoById(int id){
        return repository.getTodoById(id);
    }

    //when the user selects "delete all" from the menu
    public void deleteAllCompleted(int id, boolean is_completed) { repository.deleteAllCompleted(id, is_completed); }

}
