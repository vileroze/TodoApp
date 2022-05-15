package com.example.todoapp.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todoapp.data.TodoDao;
import com.example.todoapp.data.UserDao;

/**
 * database configuration and serves as the app's main access point to the persisted data.
 */
@Database(entities = {TodoEntity.class, UserEntity.class}, version = 1, exportSchema = false)
public abstract class TodoRoomDatabase extends RoomDatabase {
    /**
     * RoomDatabase must define an abstract method and return an
     * instance of each DAO class
     * @return TodoDao
     */
    public abstract TodoDao mTodoDao();

    public abstract UserDao mUserDao();

    public static TodoRoomDatabase INSTANCE;

    public static TodoRoomDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            synchronized (TodoRoomDatabase.class){
                if(INSTANCE ==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoRoomDatabase.class,
                            "todo.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }

    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TodoDao todoDao;
        private UserDao userDao;

        private PopulateDbAsyncTask(TodoRoomDatabase db) {
            todoDao = db.mTodoDao();
            userDao = db.mUserDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
