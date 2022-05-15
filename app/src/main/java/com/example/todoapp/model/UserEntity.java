package com.example.todoapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class UserEntity {
    @PrimaryKey
    private int user_id;
    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @Ignore
    public UserEntity() {
    }

    public UserEntity(int user_id, @NonNull String name, @NonNull String password) {
        this.user_id = user_id;
        this.name = name;
        this.password= password;
    }
    @Ignore
    public UserEntity(@NonNull String name, @NonNull String password) {
        this.name = name;
        this.password= password;
    }


    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getPassword() {
        return password;
    }



}


