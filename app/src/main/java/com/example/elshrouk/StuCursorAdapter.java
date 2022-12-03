package com.example.elshrouk;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.example.elshrouk.data.StuContent;

public class StuCursorAdapter extends CursorAdapter {
    Cursor mCursor;
    TextView stu_name;
    public StuCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.stu,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        stu_name = view.findViewById(R.id.stu_name);
        stu_name.setText(cursor.getString(cursor.getColumnIndex(StuContent.StuEntry.COLUMN_STU_NAME)));
    }
}
