package com.example.todoapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapp.model.UserEntity;

import java.util.List;
@Dao
public interface UserDao {

    @Insert
    void insert(UserEntity user);

    //fetch particular user
    @Query("SELECT * FROM user_table WHERE user_id=:id")
    UserEntity getUserById(int id);

    //delete by id
    @Delete
    void deleteById(UserEntity userEntity);

    //fetch all users
    @Query("SELECT * FROM user_table")
      List<UserEntity> getAllUsers();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(UserEntity... user);
}
