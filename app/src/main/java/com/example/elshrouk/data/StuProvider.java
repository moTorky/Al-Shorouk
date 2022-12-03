package com.example.elshrouk.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.elshrouk.data.StuContent.StuEntry;
public class StuProvider extends ContentProvider {

    public static final int STUDENTS = 100;
    public static final int STUDENTS_ID = 101;

    public static final UriMatcher sUriMatcher =new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(StuEntry.CONTENT_AUTHORITY,StuEntry.PATH_STUDENTS,STUDENTS);
        sUriMatcher.addURI(StuEntry.CONTENT_AUTHORITY,StuEntry.PATH_STUDENTS+ "/#",STUDENTS_ID);
    }

    private StuDPHelper mDpHelper ;

    @Override
    public boolean onCreate() {
        mDpHelper =new StuDPHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri,  String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db =mDpHelper.getReadableDatabase();
         Cursor cursor;
         int match= sUriMatcher.match(uri);
         switch (match){
             case STUDENTS:
                 cursor = db.query(StuEntry.TABLE_NAME, projection, selection, selectionArgs ,null, null,sortOrder);
                 break;
             case STUDENTS_ID:
                 selection=StuEntry._ID+ "=?";
                 selectionArgs =new String[] {String.valueOf(ContentUris.parseId(uri))};
                 cursor =db.query(StuEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                 break;
             default:
                 throw new IllegalArgumentException("cant query unknown uri " +uri);
         }

         cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        long id =-1;
        // Check that the name is not null
        String name = values.getAsString(StuEntry.COLUMN_STU_NAME);
        if (name == null || name.length()==0) {
            return Uri.withAppendedPath(StuEntry.CONTENT_URI,"/"+id);
        }
        String phone = values.getAsString(StuEntry.COLUMN_STU_PHONE_NUMBER);
        if (phone == null || phone.length()==0) {
            return Uri.withAppendedPath(StuEntry.CONTENT_URI,"/"+id);
        }
        String date = values.getAsString(StuEntry.COLUMN_STU_BIRTH_DATE);
        if (date == null || date.length()==0) {
            return Uri.withAppendedPath(StuEntry.CONTENT_URI,"/"+id);
        }
        String imageURI = values.getAsString(StuEntry.COLUMN_STU_IMAGE_URI);
        if (imageURI == null || imageURI.length()==0) {
            return Uri.withAppendedPath(StuEntry.CONTENT_URI,"/"+id);
        }
        // Insert the new pet with the given values
         id = db.insert(StuEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e("StuProvider", "Failed to insert row for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return Uri.withAppendedPath(StuEntry.CONTENT_URI,"/"+id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int out;
        switch (match){
            case STUDENTS:
              out=  db.delete(StuEntry.TABLE_NAME,selection, selectionArgs);
              break;
            case STUDENTS_ID:
                selection=StuEntry._ID+ "=?";
                selectionArgs =new String[] {String.valueOf(ContentUris.parseId(uri))};
                out =  db.delete(StuEntry.TABLE_NAME,selection, selectionArgs);
                break;
            default:
                throw  new IllegalArgumentException("cant delete unknown uri " +uri);
        }

        if (out != 0)
        getContext().getContentResolver().notifyChange(uri,null);
        return out;
    }

    @Override
    public int update(Uri uri, ContentValues values,  String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match) {
            case STUDENTS_ID:
                selection = StuEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int rowsUpdated = updateStu(uri, values, selection, selectionArgs);
            // If 1 or more rows were updated, then notify all listeners that the data at the
            // given URI has changed
            if (rowsUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsUpdated;
            default:
                throw new IllegalArgumentException("cant update unknown uri " + uri);
        }
    }
    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateStu(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(StuEntry.COLUMN_STU_NAME)) {
            String name = values.getAsString(StuEntry.COLUMN_STU_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(StuEntry.COLUMN_STU_GENDER)) {
            Integer gender = values.getAsInteger(StuEntry.COLUMN_STU_GENDER);
            if (gender == null) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        if (values.containsKey(StuEntry.COLUMN_STU_PHONE_NUMBER)) {
            String phone = values.getAsString(StuEntry.COLUMN_STU_PHONE_NUMBER);
            if (phone == null) {
                throw new IllegalArgumentException("stu requires a phone");
            }
        }

        if (values.containsKey(StuEntry.COLUMN_STU_PHONE_NUMBER)) {
            String phone = values.getAsString(StuEntry.COLUMN_STU_PHONE_NUMBER);
            phone=phone.trim();
            if (phone.length() != 11) {
                Toast.makeText(getContext(),"stu phone not equal 11 daget",Toast.LENGTH_LONG).show();
                throw new IllegalArgumentException("stu phone not equal 11 deagit");
            }
        }
        if (values.containsKey(StuEntry.COLUMN_STU_BIRTH_DATE)) {
            String date = values.getAsString(StuEntry.COLUMN_STU_BIRTH_DATE);
            if (date == null) {
                throw new IllegalArgumentException("stu requires a date");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase db = mDpHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return db.update(StuEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
// TODO keep going in your live
