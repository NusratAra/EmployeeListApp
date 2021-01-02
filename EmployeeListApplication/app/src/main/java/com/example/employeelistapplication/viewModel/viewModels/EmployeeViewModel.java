package com.example.employeelistapplication.viewModel.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.employeelistapplication.model.models.Employee;
import com.example.employeelistapplication.viewModel.repositories.EmployeeRepository;

import java.util.List;

public class EmployeeViewModel extends AndroidViewModel {

    private EmployeeRepository repository;
    private LiveData<List<Employee>> allEmployees;

    public EmployeeViewModel(@NonNull Application application) {
        super(application);
        repository = new EmployeeRepository(application);
        allEmployees = repository.getAllEmployees();
    }

    public void insert(Employee employee){
        repository.insert(employee);
    }

    public void update(Employee employee){
        repository.update(employee);
    }

    public void delete(Employee employee){
        repository.delete(employee);
    }

    public void deleteAllEmployees(){
        repository.deleteAllEmployees();
    }

    public LiveData<List<Employee>> getAllEmployees(){
        return allEmployees;
    }
}
