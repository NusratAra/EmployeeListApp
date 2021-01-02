package com.example.employeelistapplication.model.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.employeelistapplication.model.models.Employee;

import java.util.List;

@Dao
public interface EmployeeDao {

    @Insert
    void insert(Employee employee);

    @Update
    void update(Employee employee);

    @Delete
    void delete(Employee employee);

    @Query("DELETE FROM employee_table")
    void deleteAllEmployees();

    @Query("SELECT * FROM employee_table ORDER BY id DESC")
    LiveData<List<Employee>> getAllEmployees();
}
