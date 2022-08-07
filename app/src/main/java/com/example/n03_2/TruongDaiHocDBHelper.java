package com.example.n03_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TruongDaiHocDBHelper extends SQLiteOpenHelper {
    public TruongDaiHocDBHelper(Context context) {
        super(context,TruongDaiHocDB.getDbName(),null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TruongDaiHocDB.getCreateKhoaCommand());
            db.execSQL(TruongDaiHocDB.getCreateNganhCommand());
        }
        catch (Exception ex) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(TruongDaiHocDB.getCreateKhoaCommand());
            db.execSQL(TruongDaiHocDB.getCreateNganhCommand());
        }
        catch (Exception ex) {

        }
    }
}
