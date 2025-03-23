package com.example.myfirstapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public Context mContext;
    @Override
    protected void onChildTitleChanged(Activity childActivity, CharSequence title) {
        super.onChildTitleChanged(childActivity, title);
        mContext = this;

    }
    public void showToast(String msg) {
        Toast.makeText(mContext, "msg", Toast.LENGTH_SHORT).show();

    }
public void navigateTo(Class cls) {
    Intent intent = new Intent(mContext, cls);
    startActivity(intent);
}
}
