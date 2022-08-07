package com.example.n03_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ThemNganhActivity extends AppCompatActivity {

    private TruongDaiHocDB database;
    private Context context;
    private ArrayList<Khoa> khoas;
    private ArrayList<Nganh> nganhs;

    private Spinner spnKhoa;
    private EditText edtMaNganh;
    private EditText edtTenNganh;
    private TextView txvMaNganh;
    private TextView txvTenNganh;

    private void findViews() {
        edtMaNganh = findViewById(R.id.edtMaNganh);
        edtTenNganh = findViewById(R.id.edtTenNganh);
        txvMaNganh = findViewById(R.id.txvMaNganh);
        txvTenNganh = findViewById(R.id.txvTenNganh);
        spnKhoa = findViewById(R.id.spnKhoa);
    }

    private void loadKhoas() {
        khoas = new ArrayList<>();
        try {
            database.openToRead();
            Cursor cursor = database.select("Khoa", new String[]{"MaKhoa", "TenKhoa"}, null);
            if (cursor!=null) {
                cursor.moveToFirst();
                do {
                    Integer maKhoa = cursor.getInt(0);
                    String tenKhoa = cursor.getString(1);
                    Khoa khoa = new Khoa(maKhoa, tenKhoa);
                    khoas.add(khoa);
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception ex) {

        }
    }

    private void loadNganhs() {
        nganhs = new ArrayList<>();
        try {
            FileInputStream fileInputStream = openFileInput("nganh.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            int nganhNumber = Integer.parseInt(reader.readLine().toString());
            String[] parts;
            Nganh nganh;
            for (int i = 0; i < nganhNumber; ++i) {
                parts = reader.readLine().split("\t");
                nganh = new Nganh(Integer.parseInt(parts[0]), parts[1], Integer.parseInt(parts[2]), khoas);
                nganhs.add(nganh);
            }
            reader.close();
            inputStreamReader.close();
            fileInputStream.close();
        }
        catch (Exception ex) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nganh);
        this.context = this;
        database = new TruongDaiHocDB(context);
        loadKhoas();
        loadNganhs();
        findViews();
        loadSpnKhoa();
    }

    private void loadSpnKhoa() {
        ArrayAdapter<Khoa> adapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, khoas) {
            public Object getItem(int position) {
                return khoas.get(position).getMaKhoa().toString() + " - " + khoas.get(position).getTenKhoa();
            }
        };
        spnKhoa.setAdapter(adapter);
    }

    public void btnBack_Clicked(View view) {
        finish();
    }

    public void btnInsert_Clicked(View view) {
        if (edtMaNganh.getText().toString().equals(""))
            txvMaNganh.setText("Chưa nhập mã ngành");
        else {
            txvMaNganh.setText("");
            for (int i = 0; i < nganhs.size(); ++i)
                if (nganhs.get(i).getMaNganh().equals(Integer.parseInt(edtMaNganh.getText().toString()))) {
                    txvMaNganh.setText("Trùng mã ngành");
                    break;
                }
        }
        if (edtTenNganh.getText().toString().equals(""))
            txvTenNganh.setText("Chưa nhập tên ngành");
        else {
            txvTenNganh.setText("");
            for (int i = 0; i < nganhs.size(); ++i)
                if (nganhs.get(i).getTenNganh().equals(edtTenNganh.getText().toString())) {
                    txvTenNganh.setText("Trùng tên ngành");
                    break;
                }
        }
        if (!txvMaNganh.getText().toString().equals("") || !txvTenNganh.getText().toString().equals(""))
            return;
        Nganh nganh = new Nganh(Integer.parseInt(edtMaNganh.getText().toString()), edtTenNganh.getText().toString(), Integer.parseInt(spnKhoa.getSelectedItem().toString().split(" - ")[0]), khoas);
        nganhs.add(nganh);
        try {
            FileOutputStream outputStream = openFileOutput("nganh.txt", MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(Integer.toString(nganhs.size()) + "\n");
            for (int i = 0; i < nganhs.size(); ++i)
                writer.write(Integer.toString(nganhs.get(i).getMaNganh()) + "\t" + nganhs.get(i).getTenNganh() + "\t" + nganhs.get(i).getKhoa().getMaKhoa() + "\n");
            writer.close();
            outputStream.close();
            Intent intent= new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        catch (Exception ex) {

        }
    }
}