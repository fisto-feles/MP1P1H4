package com.homework.mp1p1h4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class PhotoViewActivity extends AppCompatActivity {

    ImageView ivPhotoView;
    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        ivPhotoView = (ImageView) findViewById(R.id.ivPhotoView);
        photoPath = getIntent().getExtras().getString("photoPath");
        Bitmap bm = BitmapFactory.decodeFile(photoPath);
        ivPhotoView.setImageBitmap(bm);
    }
}