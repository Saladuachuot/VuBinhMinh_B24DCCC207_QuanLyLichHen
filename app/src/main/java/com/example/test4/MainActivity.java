package com.example.test4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test4.adapter.AppointmentAdapter;
import com.example.test4.database.DatabaseHelper;
import com.example.test4.model.Appointment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalCount, tvReminderCount, tvEmpty;
    private RecyclerView rvAppointments;
    private Button btnAddAppointment;
    private DatabaseHelper dbHelper;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvReminderCount = findViewById(R.id.tvReminderCount);
        tvEmpty = findViewById(R.id.tvEmpty);
        rvAppointments = findViewById(R.id.rvAppointments);
        btnAddAppointment = findViewById(R.id.btnAddAppointment);

        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdapter(new ArrayList<>(), appointment -> {
            Intent intent = new Intent(MainActivity.this, AppointmentDetailActivity.class);
            intent.putExtra("APPOINTMENT_ID", appointment.getId());
            startActivity(intent);
        });
        rvAppointments.setAdapter(adapter);

        btnAddAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAppointmentActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        List<Appointment> appointments = dbHelper.getAllAppointments();
        adapter.updateData(appointments);

        tvTotalCount.setText(String.valueOf(dbHelper.countAll()));
        tvReminderCount.setText(String.valueOf(dbHelper.countReminders()));

        if (appointments.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvAppointments.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvAppointments.setVisibility(View.VISIBLE);
        }
    }
}