package com.homework.mp1p1h4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.homework.mp1p1h4.process.PhotoTransaction;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CAMERA = 100;
    static final int REQUEST_TAKE_PHOTO = 101;

    String currentPhotoPath;

    ImageView ivPhoto;
    EditText etName, etDescription;
    Button btnCamera, btnSave, btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        etName = (EditText) findViewById(R.id.etName);
        etDescription = (EditText) findViewById(R.id.etDescription);

        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnList = (Button) findViewById(R.id.btnList);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { grantPermissions(); }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor agrege un nombre", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etDescription.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor agrege una descripcion", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ivPhoto.getDrawable() == null) {
                    Toast.makeText(MainActivity.this, "Por favor agrege una fotografia", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put(PhotoTransaction.NAME_KEY, etName.getText().toString());
                values.put(PhotoTransaction.DESCRIPTION_KEY, etDescription.getText().toString());
                values.put(PhotoTransaction.PHOTO_KEY, currentPhotoPath);
                PhotoTransaction.insertPhoto(MainActivity.this, values);

                ivPhoto.setImageBitmap(null);
                etName.setText("");
                etDescription.setText("");
                currentPhotoPath = "";
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PhotoListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
            return;
        }

        Toast.makeText(getApplicationContext(), "Permisos denegados", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File photo = new File(currentPhotoPath);
            ivPhoto.setImageURI(Uri.fromFile(photo));
            scannImageFile();
        }
    }

    private void scannImageFile() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File img = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(img);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void grantPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA);
            return;
        }

        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) == null) {
            return;
        }

        File photo = null;
        try {
            photo = createImageFile();
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (photo != null) {
            Uri photoUri = FileProvider.getUriForFile(this, "com.homework.mp1p1h4.fileprovider", photo);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "p1h4" + File.separator);
        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {
                throw new IOException("Error al crear carpeta!");
            }
        }

        File img = File.createTempFile(fileName, ".jpg", storageDir);
        currentPhotoPath = img.getAbsolutePath();
        return img;
    }
}