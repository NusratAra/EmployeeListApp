package com.example.employeelistapplication.model.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "employee_table")
public class Employee {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private int age;
    private String gender;

    private String photo;

    public Employee(String name, int age, String gender, String photo) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.photo = photo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoto() {
        return photo;
    }
}
