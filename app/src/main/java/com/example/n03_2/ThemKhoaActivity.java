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

public class ThemKhoaActivity extends AppCompatActivity {

    private Context context;
    private ArrayList<Khoa> khoas;
    private TruongDaiHocDB database;

    private EditText edtMaKhoa_ThemKhoa;
    private EditText edtTenKhoa_ThemKhoa;
    private TextView txvMaKhoa_ThemKhoa;
    private TextView txvTenKhoa_ThemKhoa;

    private void findViews() {
        edtMaKhoa_ThemKhoa = (EditText) findViewById(R.id.edtMaKhoa);
        edtTenKhoa_ThemKhoa = (EditText) findViewById(R.id.edtTenKhoa);
        txvMaKhoa_ThemKhoa = (TextView) findViewById(R.id.txvMaKhoa_ThemKhoa);
        txvTenKhoa_ThemKhoa = (TextView) findViewById(R.id.txvTenKhoa_ThemKhoa);
    }

    private void loadKhoas() throws IOException, NumberFormatException {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_khoa);
        this.context = this;
        findViews();
        database = new TruongDaiHocDB(this.context);
        try {
            loadKhoas();
        }
        catch (Exception ex) {

        }
    }

    public void btnBack_ThemKhoa_Clicked(View view) {
        finish();
    }

    public void btnInsert_ThemKhoa_Clicked(View view) {
        if (edtTenKhoa_ThemKhoa.getText().toString().equals(""))
            txvTenKhoa_ThemKhoa.setText("Chưa nhập tên khoa");
        else {
            txvTenKhoa_ThemKhoa.setText("");
            for (int i = 0; i < khoas.size(); ++i)
                if (khoas.get(i).getTenKhoa().equals(edtTenKhoa_ThemKhoa.getText().toString()))
                    txvMaKhoa_ThemKhoa.setText("Tên khoa bị trùng");
        }
        if (txvMaKhoa_ThemKhoa.getText().toString().equals("") && txvTenKhoa_ThemKhoa.getText().toString().equals("")) {
            String[] columnNames = {"TenKhoa"};
            String[] columnValues = {edtTenKhoa_ThemKhoa.getText().toString()};
            database.openToWrite();
            database.insert("Khoa",columnNames, columnValues);
            database.close();
            Intent intent= new Intent();
            setResult(RESULT_OK, intent);
            finish();
            /*khoas.add(khoa);
            try {
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
        }
    }
}