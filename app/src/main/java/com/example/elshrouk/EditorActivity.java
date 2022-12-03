package com.example.elshrouk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.elshrouk.data.StuContent;
import com.example.elshrouk.data.StuDPHelper;
import com.example.elshrouk.data.StuProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditorActivity extends AppCompatActivity {
    private StuDPHelper mDPHelper ;
    private SQLiteDatabase db;
    private EditText mNameEditText;
    private EditText mBirthDateEditText;
    private EditText mPhoneNumber;
    private Spinner myGenderSpinner;
    ImageView stuImageView;
    Button takeImageBut;
    private long idCurrentStu;
    private  boolean EditStu = false;
    private Uri mUri;
    static final int REQUEST_IMAGE_CAPTURE = 7;
    File photoFile = null;
    Uri photoURI = null;

    private int mGender = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mDPHelper =new StuDPHelper(this);
        db = mDPHelper.getWritableDatabase();

        mNameEditText =findViewById(R.id.edit_Stu_name);
        mBirthDateEditText=findViewById(R.id.edit_Stu_Birth_Date);
        myGenderSpinner=findViewById(R.id.spinner_gender);
        mPhoneNumber = findViewById(R.id.edit_Stu_phone);
        stuImageView = findViewById(R.id.stu_Image);
        takeImageBut = findViewById(R.id.add_Image);

        Intent i =getIntent();
        if(i!=null){
            EditStu =i.getBooleanExtra("EditStu",false);
            idCurrentStu = i.getLongExtra("position",-1);
            Log.v("EditorActivity", "select item id: "+ idCurrentStu);

            mUri= i.getData();
            if(mUri != null){
                setTitle(R.string.update_stu);
            }
            else {
                setTitle(R.string.Editor_Activity_Name);
            }
        }
        if(idCurrentStu != -1)
            showStuData();
        setUpSpinner();

        // on click takeImage button take image by camera
        takeImageBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        //show image in new activity
        stuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorActivity.this,PhotoViewerActivity.class);
                intent.putExtra("image", photoURI.toString());
                startActivity(intent);
            }
        });
    }

    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir =// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        //currentPhotoPath = "file:" + image.getAbsolutePath();
        Log.v("Editor activity", "absolute path for image : " + currentPhotoPath);
        return image;
    }

    private void dispatchTakePictureIntent() {
        //Intent to open camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // inashalize the File where the photo should go with null
            photoFile = null;
            photoURI = null;
            try {
                photoFile = createImageFile(); // used to make temp file and return it
                //get uri for image file
                photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", createImageFile());
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("Editor activity", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.v("Editor activity", "Uri image : " + photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //get the image form current URI and show it in the imageView
            stuImageView.setImageURI(photoURI);
            Log.v("Editor activity", "URI of photo  : " + photoURI);
        }
    }

    private void showStuData() {
        Uri mUri = Uri.withAppendedPath(StuContent.StuEntry.CONTENT_URI,String.valueOf(idCurrentStu));
        Log.v("EditorActivity", "select item uri: "+mUri);
        Log.v("EditorActivity", "select item uri match: "+ StuProvider.sUriMatcher.match(mUri));
        Cursor cursor = getContentResolver().query(mUri,null,null,null,null);
        if(cursor!=null) {
            cursor.moveToFirst();
            mNameEditText.setText(cursor.getString(cursor.getColumnIndex(StuContent.StuEntry.COLUMN_STU_NAME)));
            mBirthDateEditText.setText(cursor.getString(cursor.getColumnIndex(StuContent.StuEntry.COLUMN_STU_BIRTH_DATE)));
            mPhoneNumber.setText(cursor.getString(cursor.getColumnIndex(StuContent.StuEntry.COLUMN_STU_PHONE_NUMBER)));
            stuImageView.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(StuContent.StuEntry.COLUMN_STU_IMAGE_URI))));
        }
    }

    private void upDateStu() {
        ContentValues values =new ContentValues();
        values.put(StuContent.StuEntry.COLUMN_STU_NAME, mNameEditText.getText().toString().trim());
        values.put(StuContent.StuEntry.COLUMN_STU_BIRTH_DATE,mBirthDateEditText.getText().toString().trim());
        values.put(StuContent.StuEntry.COLUMN_STU_GENDER, mGender);
        values.put(StuContent.StuEntry.COLUMN_STU_PHONE_NUMBER,mPhoneNumber.getText().toString().trim());
        if( null == photoURI || photoURI.toString().length()==0){
            Uri mUri = Uri.withAppendedPath(StuContent.StuEntry.CONTENT_URI,String.valueOf(idCurrentStu));
            Log.v("EditorActivity", "select item uri: "+mUri);
            Log.v("EditorActivity", "select item uri match: "+ StuProvider.sUriMatcher.match(mUri));
            Cursor cursor = getContentResolver().query(mUri,null,null,null,null);
            if(cursor!=null) {
                cursor.moveToFirst();
                Log.v("EditorActivity", "image URI: "+ cursor.getString(cursor.getColumnIndex(StuContent.StuEntry.COLUMN_STU_IMAGE_URI)));
                values.put(StuContent.StuEntry.COLUMN_STU_IMAGE_URI, cursor.getString(cursor.getColumnIndex(StuContent.StuEntry.COLUMN_STU_IMAGE_URI)));
            }
        }else {
            values.put(StuContent.StuEntry.COLUMN_STU_IMAGE_URI, photoURI.toString());
        }

        Uri mUri = Uri.withAppendedPath(StuContent.StuEntry.CONTENT_URI,String.valueOf(idCurrentStu));
        long mId = getContentResolver().update(mUri,
                values,
                StuContent.StuEntry.TABLE_STU_ID+"=?",
                new String[]{String.valueOf(idCurrentStu)});
        Log.v("EditorActivity", "stu updated at id = "+mId);
    }

    private void insertSru() {
        ContentValues values =new ContentValues();
        values.put(StuContent.StuEntry.COLUMN_STU_NAME, mNameEditText.getText().toString().trim());
        values.put(StuContent.StuEntry.COLUMN_STU_BIRTH_DATE,mBirthDateEditText.getText().toString().trim());
        values.put(StuContent.StuEntry.COLUMN_STU_GENDER, mGender);
        values.put(StuContent.StuEntry.COLUMN_STU_PHONE_NUMBER,mPhoneNumber.getText().toString().trim());
        values.put(StuContent.StuEntry.COLUMN_STU_IMAGE_URI,photoURI.toString());

        Uri uri = getContentResolver().insert(StuContent.StuEntry.CONTENT_URI,values);

        //long mId =  Integer.valueOf(uri.getPath().substring(11));

        long mId =  Integer.valueOf(String.valueOf(ContentUris.parseId(uri)));
        Log.v("EditorActivity", "new stu inserted at id = "+mId);

        if(mId == -1){
            Toast.makeText(this, "error with saving the new student",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "student saved with row id: "+mId,Toast.LENGTH_LONG).show();
        }
    }

    private void setUpSpinner(){
        ArrayAdapter genderSpinnerAdapter =ArrayAdapter.createFromResource(this,R.array.array_gender_option,android.R.layout.simple_dropdown_item_1line);
        myGenderSpinner.setAdapter(genderSpinnerAdapter);
        myGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection =(String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if(selection.equals("Male"))
                        mGender= StuContent.StuEntry.GENDER_MALE;
                    else if(selection.equals("Female"))
                        mGender= StuContent.StuEntry.GENDER_FEMALE;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { mGender=1; }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                if(idCurrentStu !=-1){
                    upDateStu();
                }else {
                    insertSru();
                }
                finish();
                return true;
            case R.id.action_delete:
                clearEditText();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearEditText() {
        //clear all editText in the activity
        mNameEditText.setText("");
        mBirthDateEditText.setText("");
        mPhoneNumber.setText("");
        Log.v("EditorActivity", "select item uri match: "+ mUri);

        if(!EditStu) {
            delImage(); //delete the image then delete the current student
            getContentResolver().delete(mUri, null, null);
            Toast.makeText(getApplicationContext(),"Student at id: "+idCurrentStu+" was deleted",Toast.LENGTH_LONG).show();
        }

    }
    private void delImage(){
        //get cursor for current student then get the uri image for current student
        Cursor cursor = getContentResolver().query(mUri, null, null, null, null);
        cursor.moveToFirst();
        Uri imageURI = Uri.parse(cursor.getString(cursor.getColumnIndex(StuContent.StuEntry.COLUMN_STU_IMAGE_URI)));
        Log.v("EditorActivity", "select item image uri : "+ imageURI);
        cursor.close();
        //  TO-DO chake if the file is deleted
        File fdelete = new File(String.valueOf(imageURI));
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.v("Editor activity","file Deleted :" +imageURI);
            } else {
                Log.v("Editor activity","file not Deleted :"+imageURI);
            }
        }
    }

}
