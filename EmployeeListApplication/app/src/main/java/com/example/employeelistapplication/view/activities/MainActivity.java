package com.example.employeelistapplication.view.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.employeelistapplication.view.adapters.EmployeeAdapter;
import com.example.employeelistapplication.viewModel.viewModels.EmployeeViewModel;
import com.example.employeelistapplication.R;
import com.example.employeelistapplication.model.models.Employee;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private EmployeeViewModel employeeViewModel;
    private FloatingActionButton addEmployeeButton;
    private EmployeeAdapter adapter;
    private RecyclerView.ViewHolder viewHolderRecycler;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new EmployeeAdapter();
        recyclerView.setAdapter(adapter);

        employeeViewModel = ViewModelProviders.of(this).get(EmployeeViewModel.class);
        employeeViewModel.getAllEmployees().observe(this, new Observer<List<Employee>>() {
            @Override
            public void onChanged(List<Employee> employees) {
                adapter.submitList(employees);
            }
        });

        addEmployeeButton = findViewById(R.id.add_button);
        addEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddEmployeeActivity.class);
                addEmployeeActivityResultLauncher.launch(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewHolderRecycler = viewHolder;
                alertView(getString(R.string.delete_warning_single));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new EmployeeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Employee employee) {
                Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
                intent.putExtra(AddEmployeeActivity.EXTRA_ID, employee.getId());
                intent.putExtra(AddEmployeeActivity.EXTRA_NAME, employee.getName());
                intent.putExtra(AddEmployeeActivity.EXTRA_AGE, employee.getAge());
                intent.putExtra(AddEmployeeActivity.EXTRA_GENDER, employee.getGender());
                intent.putExtra(AddEmployeeActivity.EXTRA_PROFILE_PIC, employee.getPhoto());
                addEmployeeActivityResultLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> addEmployeeActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        int id = data.getIntExtra(AddEmployeeActivity.EXTRA_ID, -1);
                        if (id == -1) {
                            addEmployeeViewModelCall(data);
                        } else {
                            updateEmployeeViewModelCall(data);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Not Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void addEmployeeViewModelCall(Intent data) {

        String name = data.getStringExtra(AddEmployeeActivity.EXTRA_NAME);
        int age = Integer.parseInt(data.getStringExtra(AddEmployeeActivity.EXTRA_AGE));
        String gender = data.getStringExtra(AddEmployeeActivity.EXTRA_GENDER);
        String photo = data.getStringExtra(AddEmployeeActivity.EXTRA_PROFILE_PIC);

        Employee employee = new Employee(name, age, gender, photo);
        employeeViewModel.insert(employee);
        Toast.makeText(this, "Employee info is added", Toast.LENGTH_SHORT).show();

    }

    private void updateEmployeeViewModelCall(Intent data) {

        int id = data.getIntExtra(AddEmployeeActivity.EXTRA_ID, -1);
        String name = data.getStringExtra(AddEmployeeActivity.EXTRA_NAME);
        int age = Integer.parseInt(data.getStringExtra(AddEmployeeActivity.EXTRA_AGE));
        String gender = data.getStringExtra(AddEmployeeActivity.EXTRA_GENDER);
        String photo = data.getStringExtra(AddEmployeeActivity.EXTRA_PROFILE_PIC);

        Employee employee = new Employee(name, age, gender, photo);
        employee.setId(id);
        employeeViewModel.update(employee);
        Toast.makeText(this, "Employee info is updated", Toast.LENGTH_SHORT).show();

    }


    private void alertView(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Delete!")
                .setIcon(R.drawable.ic_warning)
                .setMessage(message)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                        adapter.notifyItemChanged(viewHolderRecycler.getAdapterPosition());
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        employeeViewModel.delete(adapter.getEmployeeAt(viewHolderRecycler.getAdapterPosition()));
                        Toast.makeText(getApplicationContext(), "Employee info is deleted!", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void alertView(String message, String title) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(title)
                .setIcon(R.drawable.ic_warning)
                .setMessage(message)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        employeeViewModel.deleteAllEmployees();
                        Toast.makeText(getApplicationContext(), "All employees info is deleted!", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_item:
                alertView(getString(R.string.delete_warning_all), getString(R.string.delete_all_employees));
                return true;
            case R.id.export_list:
                writeRecordsToFile(employeeViewModel.getAllEmployees().getValue());
                return true;
            case R.id.import_list:
                readRecordsFromFile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void writeRecordsToFile(List<Employee> employees){
        File exportDir = new File(Environment.getExternalStorageDirectory() + "/EmployeeData", "");
        String fileName = "EmployeeData";
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, fileName + ".json");

        if(file.exists()){
            file.delete();
        }

        List<Employee> employeeList = employeeViewModel.getAllEmployees().getValue();
        Gson gson = new Gson();
        String jsonArray = gson.toJson(employeeList);


        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonArray.getBytes());
            fos.close();
            Toast.makeText(this, "Exported employee list", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readRecordsFromFile(){
        File exportDir = new File(Environment.getExternalStorageDirectory() + "/EmployeeData", "");
        String fileName = "EmployeeData";


        File file = new File(exportDir, fileName + ".json");
        if (file.exists()) {
            //TODO Toast = no file, add file.
        }
        try {
            String myData = "";
            FileInputStream fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();

            Type listType = new TypeToken<List<Employee>>() {}.getType();

            List<Employee> employees = new Gson().fromJson(myData, listType);

            insertEmployeesData(employees);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertEmployeesData(List<Employee> employees) {
        for(int i = 0; i<employees.size(); i++){
            employeeViewModel.insert(employees.get(i));
        }
        Toast.makeText(this, "Imported employee list", Toast.LENGTH_SHORT).show();
    }
}
