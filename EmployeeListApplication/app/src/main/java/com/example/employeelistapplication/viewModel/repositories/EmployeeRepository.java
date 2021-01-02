package com.example.employeelistapplication.viewModel.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.employeelistapplication.model.models.Employee;
import com.example.employeelistapplication.model.database.daos.EmployeeDao;
import com.example.employeelistapplication.model.database.EmployeeDatabase;

import java.util.List;

public class EmployeeRepository {
    private EmployeeDao employeeDao;
    private LiveData<List<Employee>> allEmployees;

    public EmployeeRepository(Application application){
        EmployeeDatabase database = EmployeeDatabase.getInstance(application);
        employeeDao = database.employeeDao();
        allEmployees = employeeDao.getAllEmployees();
    }

    public void insert(Employee employee){
        new InsertEmployeeAsyncTask(employeeDao).execute(employee);
    }

    public void update(Employee employee){
        new UpdateEmployeeAsyncTask(employeeDao).execute(employee);
    }

    public void delete(Employee employee){
        new DeleteEmployeeAsyncTask(employeeDao).execute(employee);
    }

    public void deleteAllEmployees(){
        new DeleteAllEmployeesAsyncTask(employeeDao).execute();
    }

    public LiveData<List<Employee>> getAllEmployees(){
        return allEmployees;
    }

    private static class InsertEmployeeAsyncTask extends AsyncTask<Employee, Void, Void>{

        private EmployeeDao employeeDao;
        private InsertEmployeeAsyncTask(EmployeeDao employeeDao){
            this.employeeDao = employeeDao;
        }

        @Override
        protected Void doInBackground(Employee... employees) {
            employeeDao.insert(employees[0]);
            return null;
        }
    }

    private static class UpdateEmployeeAsyncTask extends AsyncTask<Employee, Void, Void>{

        private EmployeeDao employeeDao;
        private UpdateEmployeeAsyncTask(EmployeeDao employeeDao){
            this.employeeDao = employeeDao;
        }

        @Override
        protected Void doInBackground(Employee... employees) {
            employeeDao.update(employees[0]);
            return null;
        }
    }

    private static class DeleteEmployeeAsyncTask extends AsyncTask<Employee, Void, Void>{

        private EmployeeDao employeeDao;
        private DeleteEmployeeAsyncTask(EmployeeDao employeeDao){
            this.employeeDao = employeeDao;
        }

        @Override
        protected Void doInBackground(Employee... employees) {
            employeeDao.delete(employees[0]);
            return null;
        }
    }

    private static class DeleteAllEmployeesAsyncTask extends AsyncTask<Void, Void, Void>{

        private EmployeeDao employeeDao;
        private DeleteAllEmployeesAsyncTask(EmployeeDao employeeDao){
            this.employeeDao = employeeDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            employeeDao.deleteAllEmployees();
            return null;
        }
    }
}
