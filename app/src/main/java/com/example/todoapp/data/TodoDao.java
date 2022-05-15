package com.example.todoapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapp.model.TodoEntity;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    void insert(TodoEntity todo);

    //delete todos according to user id
    @Query("DELETE FROM todo_table WHERE user_id = :id")
    void deleteAll(int id);

    //delete all completed todos
    @Query("DELETE FROM todo_table WHERE user_id=:id AND is_completed = :is_completed")
    void deleteAllCompleted(int id, boolean is_completed);

    //delete a certain item by id
    @Delete
    void deleteById(TodoEntity todo);

    //fetch a item by id
    @Query("SELECT * FROM todo_table WHERE id=:id")
    TodoEntity getTodoById(int id);

    //trying to insert the same todoItem with the same id will result in replacement of the old item
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(TodoEntity... todo);

    //
//    @Query("UPDATE todo_table SET is_completed = :is_completed WHERE id = :id")
//    void updateIsComplete(int id, boolean is_completed);

    //live data of todos for display purposes
    @Query("SELECT * FROM todo_table ORDER BY todo_date, priority desc")
    LiveData<List<TodoEntity>> getAllTodos();

    //list of todos in order for deletion purposes
    @Query("SELECT * FROM todo_table ORDER BY todo_date, priority desc")
    List<TodoEntity> getAll();


}
