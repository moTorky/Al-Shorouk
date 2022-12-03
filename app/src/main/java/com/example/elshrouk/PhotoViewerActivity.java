package com.example.elshrouk;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


public class PhotoViewerActivity extends AppCompatActivity {
    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        mImageView = findViewById(R.id.imageView);
        String url = getIntent().getStringExtra("image");
        // you can show image from url using library like glide or picasso
        if(getIntent().hasExtra("image"))
            Log.v("PhotoViewerActivity","uri : "+url);
        mImageView.setImageURI(Uri.parse(url));
    }

    @Override
    public void onBackPressed() {
        PhotoViewerActivity.this.finish();
    }
}
