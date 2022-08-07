package com.example.n03_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class TruongDaiHocDB {
    private static final String dbName = "TruongDaiHoc";
    private static final String createKhoaCommand = "CREATE TABLE Khoa(MaKhoa INTEGER PRIMARY KEY AUTOINCREMENT, TenKhoa TEXT NOT NULL);";
    private static final String createNganhCommand = "CREATE TABLE Nganh(MaNganh INTEGER PRIMARY KEY AUTOINCREMENT, TenNganh TEXT NOT NULL, MaKhoa INTEGER NOT NULL);";
    private Context context;
    private SQLiteDatabase database;
    private TruongDaiHocDBHelper helper;
    public static String getDbName() {
        return TruongDaiHocDB.dbName;
    }
    public static String getCreateKhoaCommand() {
        return TruongDaiHocDB.createKhoaCommand;
    }
    public static String getCreateNganhCommand() {
        return TruongDaiHocDB.createNganhCommand;
    }
    public TruongDaiHocDB(Context context) {
        this.context = context;
        helper = new TruongDaiHocDBHelper(context);
    }
    public TruongDaiHocDB openToWrite() {
        this.database = helper.getWritableDatabase();
        return this;
    }
    public TruongDaiHocDB openToRead() {
        this.database = helper.getReadableDatabase();
        return this;
    }
    public void close() {
        helper.close();
    }
    public long insert(String tableName, String[] columnNames, String[] columnValues) {
        ContentValues row = new ContentValues();
        for (int i = 0; i < columnNames.length; i++)
            row.put(columnNames[i], columnValues[i]);
        return database.insert(tableName, null, row);
    }
    public long update(String tableName, String[] columnNames, String[] columnValues, String condition) {
        ContentValues row = new ContentValues();
        for (int i = 0; i < columnNames.length; i++)
            row.put(columnNames[i], columnValues[i]);
        return database.update(tableName, row, condition, null);
    }
    public long delete(String tableName, String condition) {
        return database.delete(tableName, condition, null);
    }
    public Cursor select(String tableName, String[] columnNames, String condition) throws SQLException {
        Cursor cursor;
        if (condition != "")
            cursor = database.query(true, tableName, columnNames, condition, null, null, null,
                    null, null);
        else
            cursor = database.query(true, tableName, columnNames, null, null, null, null, null,
                    null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
}
