package com.example.n03_2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class QuanLyKhoaActivity extends AppCompatActivity {

    private TruongDaiHocDB database;
    private TextView txvTitle;
    private ListView lsvKhoa;

    private Context context;
    private ArrayList<Khoa> khoas;

    private void findViews() {
        lsvKhoa = (ListView) findViewById(R.id.lsvNganh);
        txvTitle = (TextView) findViewById(R.id.txvTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_khoa);
        this.context = this;
        findViews();
        database = new TruongDaiHocDB(this.context);
        try {
            loadKhoas();
            loadLsvKhoa();
        } catch (Exception e) {
            txvTitle.setText(e.toString());
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
        /*FileInputStream fileInputStream = openFileInput("khoa.txt");
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
        Toast.makeText(context, Integer.toString(khoas.size()),Toast.LENGTH_SHORT).show();
        reader.close();
        inputStreamReader.close();
        fileInputStream.close();*/
    }

    private void loadLsvKhoa() {
        ArrayAdapter<Khoa> adapter = new ArrayAdapter<Khoa>(this.context,R.layout.listitem_khoa, khoas) {
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    Khoa khoa = khoas.get(position);
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    convertView = layoutInflater.inflate(R.layout.listitem_khoa, null);
                    TextView txvMaKhoa = convertView.findViewById(R.id.txvMaNganh);
                    txvMaKhoa.setText(khoa.getMaKhoa().toString());
                    TextView txvTenKhoa = (TextView) convertView.findViewById(R.id.txvTenNganh);
                    txvTenKhoa.setText(khoa.getTenKhoa());
                    Button btnRemoveKhoa = (Button) convertView.findViewById(R.id.btnRemoveKhoa);
                    btnRemoveKhoa.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            /*khoas.remove(position);
                            try {
                                FileOutputStream outputStream = openFileOutput("khoa.txt", MODE_PRIVATE);
                                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                                writer.write(Integer.toString(khoas.size()) + "\n");
                                for (int i = 0; i < khoas.size(); ++i)
                                    writer.write(Integer.toString(khoas.get(i).getMaKhoa()) + "\t" + khoas.get(i).getTenKhoa() + "\n");
                                writer.close();
                                outputStream.close();
                                loadKhoas();

                            }
                            catch (Exception ex) {

                            }*/
                            Integer maKhoa = khoas.get(position).getMaKhoa();
                            database.openToWrite();
                            database.delete("Khoa","MaKhoa="+maKhoa.toString());
                            database.close();
                            loadKhoas();
                            loadLsvKhoa();
                        }
                    });
                    Button btnEditKhoa = (Button) convertView.findViewById(R.id.btnEditKhoa);
                    btnEditKhoa.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Khoa khoa = khoas.get(position);
                            Intent intent = new Intent(context, SuaKhoaActivity.class);
                            intent.putExtra("maKhoa", khoa.getMaKhoa());
                            intent.putExtra("tenKhoa", khoa.getTenKhoa());
                            startThemKhoaActivityLauncher.launch(intent);
                        }
                    });
                }
                return convertView;
            }
        };
        lsvKhoa.setAdapter(adapter);
    }

    private ActivityResultLauncher<Intent> startThemKhoaActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            loadKhoas();
                        }
                        catch (Exception ex) {

                        }
                        loadLsvKhoa();
                    }
                }
            });

    public void btnAddKhoa_Clicked(View view) {
        Intent intent = new Intent(this.context, ThemKhoaActivity.class);
        startThemKhoaActivityLauncher.launch(intent);
    }
}