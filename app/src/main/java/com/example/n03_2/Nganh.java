package com.example.n03_2;

import java.util.ArrayList;

public class Nganh {
    private Integer maNganh;
    private String tenNganh;
    private Khoa khoa;
    public Nganh(Integer maNganh, String tenNganh, Integer maKhoa, ArrayList<Khoa> khoas) {
        this.maNganh = maNganh;
        this.tenNganh = tenNganh;
        Khoa khoa = new Khoa(maKhoa, khoas);
        this.khoa = khoa;
    }
    public Integer getMaNganh() {
        return this.maNganh;
    }
    public String getTenNganh() {
        return this.tenNganh;
    }
    public Khoa getKhoa(){
        return this.khoa;
    }

    public void setTenNganh(String tenNganh) {
        this.tenNganh = tenNganh;
    }

    public void setKhoa(Khoa khoa) {
        this.khoa = khoa;
    }
}
