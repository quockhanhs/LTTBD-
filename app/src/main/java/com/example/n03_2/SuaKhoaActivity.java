package com.example.n03_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class SuaKhoaActivity extends AppCompatActivity {

    private TruongDaiHocDB database;
    private Context context;
    private int maKhoa;
    private String tenKhoa;
    private ArrayList<Khoa> khoas;

    private EditText edtMaKhoa_SuaKhoa;
    private EditText edtTenKhoa_SuaKhoa;
    private TextView txvTenKhoa_SuaKhoa;

    private void findViews() {
        edtMaKhoa_SuaKhoa = (EditText) findViewById(R.id.edtMaKhoa);
        edtTenKhoa_SuaKhoa = (EditText) findViewById(R.id.edtTenKhoa);
        txvTenKhoa_SuaKhoa = (TextView) findViewById(R.id.txvTenKhoa_SuaKhoa);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_khoa);
        this.context = this;
        database = new TruongDaiHocDB(context);
        Intent intent = getIntent();
        this.maKhoa = intent.getIntExtra("maKhoa", 0);
        this.tenKhoa = intent.getStringExtra("tenKhoa");
        findViews();
        edtMaKhoa_SuaKhoa.setText(Integer.toString(this.maKhoa));
        edtTenKhoa_SuaKhoa.setText(this.tenKhoa);
        try {
            loadKhoas();
        }
        catch (Exception ex) {

        }
    }

    private void loadKhoas() {
        khoas = new ArrayList<>();
        String[] columnNames = {"MaKhoa", "TenKhoa"};
        Cursor cursor = null;
        try {
            database.openToRead();
            cursor = database.select("Khoa", columnNames, null);
        }
        catch (Exception ex) {
        }
        if (cursor==null)
            return;
        if (cursor.moveToFirst()) {
            do {
                Khoa khoa = new Khoa(cursor.getInt(cursor.getColumnIndex("MaKhoa")), cursor.getString(cursor.getColumnIndex("TenKhoa")));
                khoas.add(khoa);
            }
            while (cursor.moveToNext());
        }
        database.close();
    }

    public void btnBack_SuaKhoa_Clicked(View view) {
        finish();
    }

    public void btnUpdate_SuaKhoa_Clicked(View view) {
        if (edtTenKhoa_SuaKhoa.getText().toString().equals(""))
            txvTenKhoa_SuaKhoa.setText("Chưa nhập tên khoa");
        else {
            txvTenKhoa_SuaKhoa.setText("");
            for (int i = 0; i < khoas.size(); ++i) {
                if (!khoas.get(i).getMaKhoa().equals(this.maKhoa) && edtTenKhoa_SuaKhoa.getText().toString().equals(khoas.get(i).getTenKhoa()))
                    txvTenKhoa_SuaKhoa.setText("Trùng tên khoa");
            }
            if (txvTenKhoa_SuaKhoa.getText().toString().equals("")) {
                for (int i = 0; i < khoas.size(); ++i) {
                    if (khoas.get(i).getMaKhoa().equals(this.maKhoa)) {
                        khoas.get(i).setTenKhoa(edtTenKhoa_SuaKhoa.getText().toString());
                        break;
                    }
                }
                /*try {
                    FileOutputStream outputStream = openFileOutput("khoa.txt", MODE_PRIVATE);
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                    writer.write(Integer.toString(khoas.size()) + "\n");
                    for (int i = 0; i < khoas.size(); ++i)
                        writer.write(Integer.toString(khoas.get(i).getMaKhoa()) + "\t" + khoas.get(i).getTenKhoa() + "\n");
                    writer.close();
                    outputStream.close();
                    Intent intent= new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                catch (Exception ex) {

                }*/
                database.openToWrite();
                database.update("Khoa", new String[] {"TenKhoa"}, new String[] {edtTenKhoa_SuaKhoa.getText().toString()}, "MaKhoa=" + edtMaKhoa_SuaKhoa.getText().toString());
                database.close();
                Intent intent= new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}