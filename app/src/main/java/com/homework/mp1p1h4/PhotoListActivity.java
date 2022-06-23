package com.homework.mp1p1h4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.homework.mp1p1h4.process.PhotoTransaction;

import java.io.File;

public class PhotoListActivity extends AppCompatActivity {

    ListView lvPhotoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        lvPhotoList = (ListView) findViewById(R.id.lvPhotoList);

        refreshList();

        lvPhotoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String photoId = lvPhotoList.getItemAtPosition(position).toString();
                photoId = photoId.substring(0, photoId.indexOf("-")).trim();

                String photoPath = PhotoTransaction.getPhotoPath(PhotoListActivity.this, photoId);

                if (photoPath == null) return;

                if (!new File(photoPath).exists()) {
                    showDeleteDialog(photoId);
                    return;
                }

                Intent i = new Intent(getApplicationContext(), PhotoViewActivity.class);
                i.putExtra("photoPath", photoPath);
                startActivity(i);
            }
        });
    }

    private void showDeleteDialog(String id) {
        AlertDialog dialog = new AlertDialog.Builder(PhotoListActivity.this)
                .setTitle("IMAGEN ELIMINADA")
                .setMessage("La imagen que desea ver ya no existe, desea eliminarla de la lista?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PhotoTransaction.deletePhoto(PhotoListActivity.this ,id);
                        refreshList();
                    }
                })
                .setNegativeButton("No", null).create();
        dialog.show();
    }

    private void refreshList() {
        ArrayAdapter adapter = new ArrayAdapter(PhotoListActivity.this, android.R.layout.simple_list_item_1, PhotoTransaction.getPhotoList(PhotoListActivity.this));
        lvPhotoList.setAdapter(adapter);
    }
}