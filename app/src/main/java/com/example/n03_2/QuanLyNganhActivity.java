package com.example.n03_2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class QuanLyNganhActivity extends AppCompatActivity {

    private Context context;
    private ArrayList<Khoa> khoas;
    private ArrayList<Nganh> nganhs;

    private ListView lsvNganh;

    private void findViews() {
        lsvNganh = findViewById(R.id.lsvNganh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_nganh);
        this.context = this;
        findViews();
        loadKhoas();
        loadNganhs();
        loadLsvNganh();
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

    private void loadLsvNganh() {
        ArrayAdapter<Nganh> adapter = new ArrayAdapter<Nganh>(this.context,R.layout.listitem_nganh, nganhs) {
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    Nganh nganh = nganhs.get(position);
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    convertView = layoutInflater.inflate(R.layout.listitem_nganh, null);
                    TextView txvMaNganh = convertView.findViewById(R.id.txvMaNganh);
                    txvMaNganh.setText(nganh.getMaNganh().toString());
                    TextView txvTenNganh = (TextView) convertView.findViewById(R.id.txvTenNganh);
                    txvTenNganh.setText(nganh.getTenNganh());
                    TextView txvKhoa = (TextView) convertView.findViewById(R.id.txvKhoa);
                    txvKhoa.setText(nganh.getKhoa().getTenKhoa());
                    Button btnRemoveNganh = (Button) convertView.findViewById(R.id.btnRemoveNganh);
                    btnRemoveNganh.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            nganhs.remove(position);
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
                                loadNganhs();
                                loadLsvNganh();
                            }
                            catch (Exception ex) {

                            }
                        }
                    });
                    Button btnEditNganh = (Button) convertView.findViewById(R.id.btnEditNganh);
                    btnEditNganh.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Nganh nganh = nganhs.get(position);
                            Intent intent = new Intent(context, SuaNganhActivity.class);
                            intent.putExtra("maNganh",nganh.getMaNganh());
                            intent.putExtra("tenNganh", nganh.getTenNganh());
                            intent.putExtra("maKhoa", nganh.getKhoa().getMaKhoa());
                            syncLauncher.launch(intent);
                        }
                    });
                }
                return convertView;
            }
        };
        lsvNganh.setAdapter(adapter);
    }

    private ActivityResultLauncher<Intent> syncLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        loadNganhs();
                        loadLsvNganh();
                    }
                }
            });

    public void btnAddNganh_Clicked(View view) {
        Intent intent = new Intent(context, ThemNganhActivity.class);
        syncLauncher.launch(intent);
    }
}