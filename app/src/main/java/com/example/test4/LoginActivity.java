package com.example.test4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test4.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private TextView tvError;
    private Button btnLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvError = findViewById(R.id.tvError);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String user = edtUsername.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                tvError.setText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu");
                tvError.setVisibility(View.VISIBLE);
                return;
            }

            if (dbHelper.checkLogin(user, pass)) {
                tvError.setVisibility(View.GONE);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                tvError.setText("Sai tên đăng nhập hoặc mật khẩu");
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }
}