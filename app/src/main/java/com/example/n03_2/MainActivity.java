package com.example.n03_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
    }

    public void btnQuanLyKhoa_Clicked(View view) {
        Intent intent = new Intent(context, QuanLyKhoaActivity.class);
        startActivity(intent);
    }

    public void btnQuanLyNganh_Clicked(View view) {
        Intent intent = new Intent(context, QuanLyNganhActivity.class);
        startActivity(intent);
    }
}