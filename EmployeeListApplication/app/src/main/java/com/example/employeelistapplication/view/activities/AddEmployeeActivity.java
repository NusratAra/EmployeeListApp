package com.example.employeelistapplication.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.employeelistapplication.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;

public class AddEmployeeActivity extends AppCompatActivity {
    public static final String TAG = AddEmployeeActivity.class.getSimpleName();

    public static final String EXTRA_ID = "com.example.employeelistapplication.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.employeelistapplication.EXTRA_NAME";
    public static final String EXTRA_AGE = "com.example.employeelistapplication.EXTRA_AGE";
    public static final String EXTRA_GENDER = "com.example.employeelistapplication.EXTRA_GENDER";
    public static final String EXTRA_PROFILE_PIC = "com.example.employeelistapplication.EXTRA_PROFILE_PIC";

    private FrameLayout employeePofileImageLayout;
    private EditText employeeName;
    private EditText employeeAge;
    private Spinner employeeGender;
    private FancyButton saveButton;
    private CircleImageView employeePofileImage;
    private String inputImageData = "";
    private final String[] genderArray = new String[]{
            "Gender",
            "Male",
            "Female",
            "Others"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);



        employeePofileImageLayout = findViewById(R.id.profile_image_layout);
        employeePofileImage = findViewById(R.id.profile_image);
        employeeName = findViewById(R.id.employee_name);
        employeeAge = findViewById(R.id.employee_age);
        saveButton = findViewById(R.id.save);
        initSpinner();
        getIntentValues();

        employeePofileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageData();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEmployeeInfo();
            }
        });

    }

    private void setImageData() {

        ImagePicker.Companion.with(this)    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri selectedImage = Uri.fromFile(ImagePicker.Companion.getFile(data));

            inputImageData = ImagePicker.Companion.getFile(data).getAbsolutePath();

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.override(512);
            Glide.with(this).asDrawable()
                    .load(selectedImage)
                    .apply(requestOptions)
                    .placeholder(getDrawable(R.drawable.man))
                    .into(employeePofileImage);

        }
    }

    private void getIntentValues() {

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle(R.string.edit_employee);
            employeeName.setText(intent.getStringExtra(EXTRA_NAME));
            employeeAge.setText(String.valueOf(intent.getIntExtra(EXTRA_AGE, 1)));
            inputImageData = intent.getStringExtra(EXTRA_PROFILE_PIC);

            for(int i=0; i<genderArray.length; i++){
                if(genderArray[i].equals(intent.getStringExtra(EXTRA_GENDER))){
                    employeeGender.setSelection(i);
                }
            }

            Glide.with(this).asDrawable()
                    .load(inputImageData)
                    .placeholder(getDrawable(R.drawable.man))
                    .into(employeePofileImage);


        } else {
            setTitle(R.string.add_employee);
        }
    }

    private void saveEmployeeInfo() {
        String name = employeeName.getText().toString();
        String age = String.valueOf(employeeAge.getText().toString());
        String gender = "";
        if(!employeeGender.getSelectedItem().toString().equals("Gender")){
             gender = employeeGender.getSelectedItem().toString();

        }

        if(name.trim().isEmpty() || age.trim().isEmpty() ||
                gender.equalsIgnoreCase("")){
            Toast.makeText(this, "Please insert name, age, gender", Toast.LENGTH_SHORT).show();
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_NAME, name);
            data.putExtra(EXTRA_AGE, age);
            data.putExtra(EXTRA_GENDER, gender);
            if(inputImageData == null){
                data.putExtra(EXTRA_PROFILE_PIC, "");
            } else {
                data.putExtra(EXTRA_PROFILE_PIC, inputImageData);
            }

            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            if(id != -1){
                data.putExtra(EXTRA_ID, id);
            }
            setResult(RESULT_OK, data);
            finish();
        }



    }

    private void initSpinner() {

        employeeGender =  findViewById(R.id.employee_gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, genderArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeGender.setAdapter(adapter);

        employeeGender.setSelection(0, false);
        employeeGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}