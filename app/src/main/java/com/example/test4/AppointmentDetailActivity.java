package com.example.test4;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.test4.database.DatabaseHelper;
import com.example.test4.model.Appointment;

public class AppointmentDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvLocation, tvDateTime, tvReminderStatus;
    private SwitchCompat switchReminder;
    private Button btnBack, btnDelete;
    private ImageButton btnBackTop;
    private DatabaseHelper dbHelper;
    private int appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        dbHelper = new DatabaseHelper(this);
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvLocation = findViewById(R.id.tvDetailLocation);
        tvDateTime = findViewById(R.id.tvDetailDateTime);
        tvReminderStatus = findViewById(R.id.tvReminderStatus);
        switchReminder = findViewById(R.id.switchReminderDetail);
        btnBack = findViewById(R.id.btnBackDetail);
        btnDelete = findViewById(R.id.btnDeleteAppointment);
        btnBackTop = findViewById(R.id.btnBackTop);

        appointmentId = getIntent().getIntExtra("APPOINTMENT_ID", -1);
        if (appointmentId == -1) {
            finish();
            return;
        }

        loadDetail();

        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dbHelper.updateReminder(appointmentId, isChecked);
            tvReminderStatus.setText(isChecked ? "Đang bật" : "Đang tắt");
        });

        btnBack.setOnClickListener(v -> finish());
        btnBackTop.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa lịch hẹn này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        dbHelper.deleteAppointment(appointmentId);
                        Toast.makeText(this, "Đã xóa lịch hẹn", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void loadDetail() {
        Appointment app = dbHelper.getAppointmentById(appointmentId);
        if (app != null) {
            tvTitle.setText(app.getTitle());
            tvLocation.setText(app.getLocation());
            tvDateTime.setText(app.getFormattedDate() + "  •  " + app.getTime());
            switchReminder.setChecked(app.isReminder());
            tvReminderStatus.setText(app.isReminder() ? "Đang bật" : "Đang tắt");
        }
    }
}