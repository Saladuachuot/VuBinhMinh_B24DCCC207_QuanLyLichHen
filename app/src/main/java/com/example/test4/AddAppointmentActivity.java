package com.example.test4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.test4.database.DatabaseHelper;
import com.example.test4.model.Appointment;

import java.util.Calendar;
import java.util.Locale;

public class AddAppointmentActivity extends AppCompatActivity {

    private EditText edtTitle, edtLocation;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private SwitchCompat switchReminder;
    private Button btnSave;
    private ImageButton btnBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        dbHelper = new DatabaseHelper(this);
        edtTitle = findViewById(R.id.edtTitle);
        edtLocation = findViewById(R.id.edtLocation);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        switchReminder = findViewById(R.id.switchReminderAdd);
        btnSave = findViewById(R.id.btnSaveAppointment);
        btnBack = findViewById(R.id.btnBackAdd);

        timePicker.setIs24HourView(true);

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveAppointment());
    }

    private void saveAppointment() {
        String title = edtTitle.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();

        if (title.isEmpty()) {
            edtTitle.setError("Vui lòng nhập tên lịch hẹn");
            edtTitle.requestFocus();
            return;
        }
        if (location.isEmpty()) {
            edtLocation.setError("Vui lòng nhập địa điểm");
            edtLocation.requestFocus();
            return;
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth(); // 0-11
        int year = datePicker.getYear();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Kiểm tra thời gian phải sau thời điểm hiện tại
        Calendar selectedCal = Calendar.getInstance();
        selectedCal.set(year, month, day, hour, minute, 0);

        Calendar now = Calendar.getInstance();
        if (!selectedCal.after(now)) {
            Toast.makeText(this, "Thời điểm hẹn phải sau thời điểm hiện tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu dạng yyyy-MM-dd và HH:mm
        String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
        boolean isReminder = switchReminder.isChecked();

        Appointment newApp = new Appointment(title, location, formattedDate, formattedTime, isReminder);
        long id = dbHelper.insertAppointment(newApp);

        if (id > 0) {
            Toast.makeText(this, "Đã lưu lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi lưu lịch hẹn", Toast.LENGTH_SHORT).show();
        }
    }
}