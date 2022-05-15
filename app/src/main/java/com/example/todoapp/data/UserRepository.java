package com.example.todoapp.data;

import android.app.Application;
import android.os.AsyncTask;

import com.example.todoapp.model.UserEntity;
import com.example.todoapp.model.TodoRoomDatabase;

import java.util.List;


public class UserRepository {
    private UserDao mUserDao;

    public UserRepository(Application app) {
        TodoRoomDatabase database = TodoRoomDatabase.getDatabase(app);
        mUserDao = database.mUserDao();
    }

    public void insert(UserEntity userEntity) {
        mUserDao.insert(userEntity);
    }

    public void update(UserEntity userEntity) {
        new updateUserAsyncTask(mUserDao).execute(userEntity);
    }

    public void deleteById(UserEntity userEntity) {
        new deleteUserAsyncTask(mUserDao).execute(userEntity);
    }

    public UserEntity getUserById(int id) {
        return mUserDao.getUserById(id);
    }

    public List<UserEntity> getAllUsers() {
        return mUserDao.getAllUsers();
    }

    private static class updateUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private UserDao mUserDao;
        private updateUserAsyncTask(UserDao userDao){
            mUserDao=userDao;
        }

        @Override
        protected Void doInBackground(UserEntity... user) {
            mUserDao.update(user[0]);
            return null;
        }
    }

    private static class deleteUserAsyncTask extends AsyncTask<UserEntity, Void, Void> {
        private UserDao mUserDao;
        private deleteUserAsyncTask(UserDao userDao){
            mUserDao=userDao;
        }

        @Override
        protected Void doInBackground(UserEntity... user) {
            mUserDao.deleteById(user[0]);
            return null;
        }
    }
}
