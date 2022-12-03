package com.example.elshrouk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.PreferenceChangeEvent;


public class MainActivity extends AppCompatActivity {
public static final String INTENT_KEY = "tableName";
public static final String createNewTable = "createNewTable";
    ListView allClasses;
    EditText className;
    Button createBtn;
    String tableName ;
    ArrayList<String> tablesName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        className = findViewById(R.id.class_name);
        createBtn =findViewById(R.id.create);

        tablesName = new ArrayList<>();
        tablesName.add("students");
        //name of current table
        //in the new version i 'll add feature for add more than one table in the app
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableName = className.getText().toString();
                tablesName.add(tableName);

                Intent i =new Intent(MainActivity.this,CatalogActivity.class);
                i.putExtra(INTENT_KEY, tableName);
                i.putExtra(createNewTable,true);
                startActivity(i);
            }
        });

        ArrayAdapter adapter;
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, (List) tablesName);

        allClasses = findViewById(R.id.classes);
        allClasses.setAdapter(adapter);

        //this code 'll work in the new version
        allClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i =new Intent(MainActivity.this,CatalogActivity.class);
                i.putExtra(createNewTable,false);
                startActivity(i);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        className.setVisibility(View.GONE);
        className.setText("");
        createBtn.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_class:
                className.setVisibility(View.VISIBLE);
                createBtn.setVisibility(View.VISIBLE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
