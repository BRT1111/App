package com.example.myfirstapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.MainActivity;
import com.example.myfirstapp.R;
import com.example.myfirstapp.model.LoginResponse;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import util.StringUtils;

import java.io.IOException;

public class LoginActivity extends BaseActivity {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            showToast("请输入账号");
            return;
        }
        if (StringUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }

        // 创建请求体（根据接口参数调整）
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        // 创建请求（替换为实际接口 URL）
        Request request = new Request.Builder()
                .url("http://192.168.31.32:8080/renren-fast/sys/login")
                .post(formBody)
                .build();

        // 发送异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> showToast("网络请求失败，请检查网络连接"));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            Gson gson = new Gson();
                            LoginResponse loginResponse = gson.fromJson(responseData, LoginResponse.class);

                            if (loginResponse.getCode() == 200) {
                                showToast("登录成功");
                                saveToken(loginResponse.getToken());
                                navigateToMainActivity();
                            } else {
                                showToast("登录失败：" + loginResponse.getMsg());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast("解析响应失败");
                        }
                    });
                } else {
                    runOnUiThread(() -> showToast("服务器错误，状态码：" + response.code()));
                }
            }
        });
    }

    // 保存 Token 到本地
    private void saveToken(String token) {
        getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .edit()
                .putString("token", token)
                .apply();
    }

    // 跳转到主页
    private void navigateToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    // 自定义 Toast
    public void showToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);
        TextView toastMessage = layout.findViewById(R.id.toast_message);
        toastMessage.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

        new Handler().postDelayed(toast::cancel, 5000);
    }
}