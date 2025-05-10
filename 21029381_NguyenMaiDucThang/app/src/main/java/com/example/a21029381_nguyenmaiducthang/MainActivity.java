package com.example.a21029381_nguyenmaiducthang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText etUser, etPass;
    CheckBox checkBox;
    Button btnLogin;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        apiService = ApiClient.getApiService();
        restorePreferences();
        setupLogin();
    }

    private void initViews() {
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        checkBox = findViewById(R.id.CheckBox1);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupLogin() {
        btnLogin.setOnClickListener(view -> {
            String username = etUser.getText().toString().trim();
            String password = etPass.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng không để trống ô nào!", Toast.LENGTH_SHORT).show();
                return;
            }

            loginCustomer(username, password);
        });
    }

    private void loginCustomer(String username, String password) {
        Customer customer = new Customer(username, password);
        Call<Customer> call = apiService.loginCustomer(customer);

        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    savePreferences(username, password);
                    goToProductPage();
                } else {
                    Toast.makeText(MainActivity.this, "Thông tin không đúng. Đang đăng ký mới...", Toast.LENGTH_SHORT).show();
                    registerCustomer(username, password);
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerCustomer(String username, String password) {
        Customer newCustomer = new Customer(username, password);
        Call<Customer> call = apiService.registerCustomer(newCustomer);

        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    loginCustomer(username, password); // Tự động đăng nhập lại
                } else {
                    Toast.makeText(MainActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối khi đăng ký: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePreferences(String username, String password) {
        SharedPreferences prefs = getSharedPreferences("Save", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user1", username);
        editor.putString("pass1", password);
        editor.putBoolean("checked", checkBox.isChecked());
        editor.apply();
    }

    private void restorePreferences() {
        SharedPreferences prefs = getSharedPreferences("Save", MODE_PRIVATE);
        boolean checked = prefs.getBoolean("checked", false);
        if (checked) {
            etUser.setText(prefs.getString("user1", ""));
            etPass.setText(prefs.getString("pass1", ""));
        }
        checkBox.setChecked(checked);
    }

    private void goToProductPage() {
        String inputName = etUser.getText().toString();
        Intent intent = new Intent(MainActivity.this, ProductPage.class);
        intent.putExtra("Tên", inputName);
        startActivity(intent);
        finish();
    }
}
