package com.example.elshrouk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.elshrouk.CatalogActivity;
import com.example.elshrouk.data.StuContent.StuEntry;
public class StuDPHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE=
    "CREATE TABLE "+ StuEntry.TABLE_NAME+"("+
            StuEntry.TABLE_STU_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            StuEntry.COLUMN_STU_NAME+" TEXT NOT NULL, "+
            StuEntry.COLUMN_STU_GENDER+" INTEGER NOT NULL, "+
            StuEntry.COLUMN_STU_BIRTH_DATE+" TEXT , "+
            StuEntry.COLUMN_STU_PHONE_NUMBER+" TEXT NOT NULL, "+
            StuEntry.COLUMN_STU_IMAGE_URI+" TEXT NOT NULL "+")";

    private static final int  DATABASE_VERSION =1;
    public static final String DATABASE_NAME ="classes.db";

    public StuDPHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+StuEntry.TABLE_NAME);
        onCreate(db);
    }
}
