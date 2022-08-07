package com.example.n03_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class SuaNganhActivity extends AppCompatActivity {

    private Context context;
    private ArrayList<Khoa> khoas;
    private ArrayList<Nganh> nganhs;

    private EditText edtMaNganh;
    private EditText edtTenNganh;
    private Spinner spnKhoa;
    private TextView txvTenNganh;

    private void findViews() {
        edtMaNganh = findViewById(R.id.edtMaNganh);
        edtTenNganh = findViewById(R.id.edtTenNganh);
        spnKhoa = findViewById(R.id.spnKhoa);
        txvTenNganh = findViewById(R.id.txvTenNganh);
    }

    private void loadKhoas() {
        khoas = new ArrayList<>();
        try {
            FileInputStream fileInputStream = openFileInput("khoa.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            int khoaNumber = Integer.parseInt(reader.readLine().toString());
            String[] parts;
            Khoa khoa;
            for (int i = 0; i < khoaNumber; ++i) {
                parts = reader.readLine().split("\t");
                khoa = new Khoa(Integer.parseInt(parts[0]), parts[1]);
                khoas.add(khoa);
            }
            Toast.makeText(context, Integer.toString(khoas.size()), Toast.LENGTH_SHORT).show();
            reader.close();
            inputStreamReader.close();
            fileInputStream.close();
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

    private void loadSpnKhoa() {
        ArrayAdapter<Khoa> adapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, khoas) {
            public Object getItem(int position) {
                return khoas.get(position).getMaKhoa().toString() + " - " + khoas.get(position).getTenKhoa();
            }
        };
        spnKhoa.setAdapter(adapter);
    }

    private void loadForm() {
        Intent intent = getIntent();
        Integer maNganh = intent.getIntExtra("maNganh", 0);
        String tenNganh = intent.getStringExtra("tenNganh");
        Integer maKhoa = intent.getIntExtra("maKhoa", 0);
        edtMaNganh.setText(maNganh.toString());
        edtTenNganh.setText(tenNganh);
        for (int i = 0; i < khoas.size(); ++i)
            if (khoas.get(i).getMaKhoa().equals(maKhoa)) {
                spnKhoa.setSelection(i);
                break;
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_nganh);
        this.context = this;
        findViews();
        loadKhoas();
        loadNganhs();
        loadSpnKhoa();
        loadForm();
    }

    public void btnBack_Clicked(View view) {
        finish();
    }

    public void btnSuaNganh_Clicked(View view) {
        if (edtTenNganh.getText().toString().equals(""))
            txvTenNganh.setText("Chưa nhập tên ngành");
        else {
            txvTenNganh.setText("");
            for (int i = 0; i < nganhs.size(); ++i)
                if (nganhs.get(i).getTenNganh().equals(edtTenNganh.getText().toString()))
                    if (!nganhs.get(i).getMaNganh().toString().equals(edtMaNganh.getText().toString())) {
                        txvTenNganh.setText("Trùng tên ngành");
                        break;
                    }
        }
        if (txvTenNganh.getText().toString().equals("")) {
            for (int i = 0; i < nganhs.size(); ++i)
               if (nganhs.get(i).getMaNganh().toString().equals(edtMaNganh.getText().toString())) {
                   nganhs.get(i).setTenNganh(edtTenNganh.getText().toString());
                   Khoa khoa = new Khoa(Integer.parseInt(spnKhoa.getSelectedItem().toString().split(" - ")[0]),khoas);
                   nganhs.get(i).setKhoa(khoa);
                   break;
               }
        }
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