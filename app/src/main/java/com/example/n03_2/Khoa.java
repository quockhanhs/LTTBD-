package com.example.n03_2;

import java.util.ArrayList;

public class Khoa {
    private Integer maKhoa;
    private String tenKhoa;
    public Khoa(Integer maKhoa, String tenKhoa) {
        this.maKhoa = maKhoa;
        this.tenKhoa = tenKhoa;
    }
    public Khoa(Integer maKhoa, ArrayList<Khoa> khoas) {
        this.maKhoa = maKhoa;
        for (int i = 0; i < khoas.size(); ++i)
            if (khoas.get(i).getMaKhoa().equals(this.maKhoa))
                this.tenKhoa = khoas.get(i).getTenKhoa();
    }

    public Integer getMaKhoa() {
        return this.maKhoa;
    }

    public String getTenKhoa() {
        return this.tenKhoa;
    }
    public void setTenKhoa(String tenKhoa) {
        this.tenKhoa = tenKhoa;
    }
}
