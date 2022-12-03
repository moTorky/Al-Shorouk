package com.example.elshrouk.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class StuContent {
public static abstract class StuEntry implements BaseColumns{
    public static final String TABLE_NAME = "students";

    public static final String TABLE_STU_ID = BaseColumns._ID;
    public static final String COLUMN_STU_GENDER = "GENDER";
    public static final String COLUMN_STU_NAME ="NAME";
    public static final String COLUMN_STU_BIRTH_DATE ="DATE";
    public static final String COLUMN_STU_PHONE_NUMBER="PHONE_NUMBER";
    public static final String COLUMN_STU_IMAGE_URI="IMAGE_URI";



    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    public static final String CONTENT_AUTHORITY = "com.example.elshrouk";
    public static final android.net.Uri BASE_CONTENT_URI = android.net.Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_STUDENTS = "students";
    public static final Uri CONTENT_URI = android.net.Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STUDENTS);




}
}
