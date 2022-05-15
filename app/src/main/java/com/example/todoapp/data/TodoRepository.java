package com.example.todoapp.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import com.example.todoapp.model.TodoEntity;
import com.example.todoapp.model.TodoRoomDatabase;

public class TodoRepository {
    private TodoDao todoDao;
    private LiveData<List<TodoEntity>> allTodos;

    public TodoRepository(Application application){
        TodoRoomDatabase database = TodoRoomDatabase.getDatabase(application);
        todoDao = database.mTodoDao();
        allTodos = todoDao.getAllTodos();
    }

//    public TodoDao getmTodoDAO() {
//        return mTodoDAO;
//    }

//    public void setmTodoDAO(TodoDao mTodoDAO) {
//        this.mTodoDAO = mTodoDAO;
//    }

    public LiveData<List<TodoEntity>> getAllTodoList() {
        return allTodos;
    }

    public void setAllTodoList(LiveData<List<TodoEntity>> allTodoList) {
        this.allTodos = allTodoList;
    }

    public void insert(TodoEntity todoEntity){
        new insertTodoAsyncTask(todoDao).execute(todoEntity);
    }

    public void update(TodoEntity todoEntity) {
        new updateTodoAsyncTask(todoDao).execute(todoEntity);
    }

//    public void updateIsCompleted(int id, boolean is_completed) {mTodoDAO.updateIsComplete(id, is_completed);}

    public void deleteById(TodoEntity todoEntity){
        new deleteByIdTodoAsync(todoDao).execute(todoEntity);
    }

    public void deleteAll(int id) { todoDao.deleteAll(id);}

    public List<TodoEntity> getAll() { return  todoDao.getAll();}

    public void deleteAllCompleted(int id, boolean is_completed) {
        todoDao.deleteAllCompleted(id, is_completed);
    }

    public TodoEntity getTodoById(int id){
        return todoDao.getTodoById(id);
    }

    private static class insertTodoAsyncTask extends AsyncTask<TodoEntity, Void, Void> {
        private TodoDao mTodoDao;
        private insertTodoAsyncTask(TodoDao todoDAO){
            mTodoDao=todoDAO;
        }

        @Override
        protected Void doInBackground(TodoEntity... todoEntities) {
            mTodoDao.insert(todoEntities[0]);
            return null;
        }
    }

    private static class updateTodoAsyncTask extends AsyncTask<TodoEntity, Void, Void> {
        private TodoDao mTodoDao;
        private updateTodoAsyncTask(TodoDao todoDAO){
            mTodoDao=todoDAO;
        }

        @Override
        protected Void doInBackground(TodoEntity... todoEntities) {
            mTodoDao.update(todoEntities[0]);
            return null;
        }
    }


    private static class deleteByIdTodoAsync extends AsyncTask<TodoEntity, Void, Void>{
        private TodoDao mTodoDao;
        private deleteByIdTodoAsync(TodoDao todoDAO){
            mTodoDao=todoDAO;
        }

        @Override
        protected Void doInBackground(TodoEntity... todoEntities) {
            mTodoDao.deleteById(todoEntities[0]);
            return null;
        }
    }


}
