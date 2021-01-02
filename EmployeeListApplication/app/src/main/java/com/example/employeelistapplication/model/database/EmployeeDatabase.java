package com.example.employeelistapplication.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.employeelistapplication.model.database.daos.EmployeeDao;
import com.example.employeelistapplication.model.models.Employee;

@Database(entities = {Employee.class}, version = 1)
public abstract class EmployeeDatabase extends RoomDatabase {

    private static EmployeeDatabase instance;

    public abstract EmployeeDao employeeDao();

    public static synchronized EmployeeDatabase getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    EmployeeDatabase.class, "employee_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }


    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private EmployeeDao employeeDao;

        private PopulateDbAsyncTask(EmployeeDatabase db){
            employeeDao = db.employeeDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
