package com.example.elshrouk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.elshrouk.data.StuContent.StuEntry;
import com.example.elshrouk.data.StuDPHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String tableName;
    ListView allStu;
    StuCursorAdapter mAdapter;
    FloatingActionButton ftb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cataogry);
        Intent mIntent = getIntent();
        if(mIntent != null) {
            tableName = mIntent.getStringExtra(MainActivity.INTENT_KEY);
        }
        ftb =findViewById(R.id.fab);
        ftb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CatalogActivity.this, EditorActivity.class);
                i.putExtra("EditStu",true);
                startActivity(i);
            }
        });

        allStu=findViewById(R.id.all_stu_list_view);

        View emptyView = findViewById(R.id.empty_view);
        allStu.setEmptyView(emptyView);

        mAdapter = new StuCursorAdapter(this,null);
        allStu.setAdapter(mAdapter);

            allStu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(CatalogActivity.this, EditorActivity.class);
                i.putExtra("position",id);
                Log.v("CatalogActivity", "select item id: "+id);
                i.setData(Uri.withAppendedPath(StuEntry.CONTENT_URI,String.valueOf(id)));
                startActivity(i);
            }
        });

        getSupportLoaderManager().initLoader(0,null, this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_all_entries:
                deleteAllStu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertStu() {
        StuDPHelper stuDPHelper = new StuDPHelper(this);
        SQLiteDatabase db = stuDPHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StuEntry.COLUMN_STU_NAME, "mo torky");
        values.put(StuEntry.COLUMN_STU_BIRTH_DATE,"17/12");
        values.put(StuEntry.COLUMN_STU_GENDER, 1);
        values.put(StuEntry.COLUMN_STU_PHONE_NUMBER, "+2001120075620");
        long id = db.insert(StuEntry.TABLE_NAME,null,values);

        Log.v("CatalogActivity","new Stu in :"+id);
    }

    private void deleteAllStu() {
        getContentResolver().delete(StuEntry.CONTENT_URI,
                null,
                null);
        Log.v("CatalogActivity","delete all stu fom students table");

    }
 // this code 'll work in main thread so we 'll use loader to work in background thread

//    @Override
//    protected void onStart() {
//        super.onStart();
//        getDatabaseInfo();
//    }
//
//    private void getDatabaseInfo() {
//        String []projection={
//                StuEntry._ID,
//                StuEntry.COLUMN_STU_NAME,
//                StuEntry.COLUMN_STU_BIRTH_DATE,
//                StuEntry.COLUMN_STU_GENDER
//        };
//        Cursor cursor = getContentResolver().query(StuEntry.CONTENT_URI
//                ,projection
//                ,null
//                ,null,
//                null,
//                null);
//
//
//        mAdapter = new StuCursorAdapter(this,cursor);
//        allStu.setAdapter(mAdapter);
//
//        allStu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String name = (String) parent.getSelectedItem();
//                Intent i = new Intent(CatalogActivity.this, EditorActivity.class);
//                i.putExtra("position",id);
//                Log.v("CatalogActivity", "select item id: "+id);
//                startActivity(i);
//            }
//        });
//        Log.v("Catalog ","query done");
//    }

    //return all stu in the table
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String []projection={
                StuEntry._ID,
                StuEntry.COLUMN_STU_NAME,
                StuEntry.COLUMN_STU_BIRTH_DATE,
                StuEntry.COLUMN_STU_GENDER
        };
        return new CursorLoader(getApplicationContext(),
                StuEntry.CONTENT_URI
                ,projection
                ,null
                ,null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset( Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
