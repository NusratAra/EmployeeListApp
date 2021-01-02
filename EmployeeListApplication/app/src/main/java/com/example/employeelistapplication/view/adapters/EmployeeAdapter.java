package com.example.employeelistapplication.view.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.employeelistapplication.R;
import com.example.employeelistapplication.model.models.Employee;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends ListAdapter<Employee, EmployeeAdapter.EmployeeHolder> {
    private static final String TAG= EmployeeAdapter.class.getSimpleName();
    private OnItemClickListener listener;

    public EmployeeAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Employee> DIFF_CALLBACK = new DiffUtil.ItemCallback<Employee>() {
        @Override
        public boolean areItemsTheSame(@NonNull Employee oldItem, @NonNull Employee newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Employee oldItem, @NonNull Employee newItem) {

            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getAge() == newItem.getAge() &&
                    oldItem.getGender().equals(newItem.getGender()) &&
                    oldItem.getPhoto().equals(newItem.getPhoto());
        }
    };

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.individual_employee_layout, parent, false);
        return new EmployeeHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EmployeeHolder holder, int position) {

        Employee currentEmployee = getItem(position);
        holder.employeeName.setText(currentEmployee.getName());
        holder.employeeAge.setText(holder.itemView.getContext().getString(R.string.age_tag) +" "+ currentEmployee.getAge());
        holder.employeeGender.setText(holder.itemView.getContext().getString(R.string.gender_tag) +" "+ currentEmployee.getGender());

        if(currentEmployee.getPhoto() != null){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.override(512);
            Glide.with(holder.itemView.getContext()).asDrawable()
                    .load(currentEmployee.getPhoto())
                    .apply(requestOptions)
                    .placeholder(holder.itemView.getContext().getDrawable(R.drawable.man))
                    .into(holder.employeeImage);

        }

    }

    public Employee getEmployeeAt(int position){
        return getItem(position);
    }

    class EmployeeHolder extends RecyclerView.ViewHolder{

        private TextView employeeName;
        private TextView employeeAge;
        private TextView employeeGender;
        private CircleImageView employeeImage;

        public EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.employee_name);
            employeeAge = itemView.findViewById(R.id.employee_age);
            employeeGender = itemView.findViewById(R.id.employee_gender);
            employeeImage = itemView.findViewById(R.id.profile_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.OnItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Employee employee);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
