package com.homework.mp1p1h4.process;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

public class PhotoTransaction {
    public static String DB_NAME = "mp1";
    public static String TABLE = "p1h4_photo";

    public static String ID_KEY = "id";
    public static String NAME_KEY = "name";
    public static String DESCRIPTION_KEY = "description";
    public static String PHOTO_KEY = "photo";

    public static String getCreateTableSQL() {
        return "CREATE TABLE " + TABLE + "(" +
                ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                NAME_KEY + " TEXT NOT NULL, " +
                DESCRIPTION_KEY + " TEXT NOT NULL, " +
                PHOTO_KEY + " TEXT NOT NULL)";
    }

    public static String getDropTableSQL() {
        return "DROP TABLE " + TABLE;
    }

    public static void insertPhoto(Context ctx, ContentValues values) {
        try {
            Connection cnn = new Connection(ctx, PhotoTransaction.DB_NAME, null, 1);
            SQLiteDatabase db = cnn.getWritableDatabase();
            db.insert(PhotoTransaction.TABLE, null, values);
            db.close();
            Toast.makeText(ctx, "Photo registrada correctament", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(ctx, "Ha ocurrido un error!", Toast.LENGTH_LONG).show();
        }
    }

    public static ArrayList<Photo> getPhotoArray(Context ctx) {
        ArrayList<Photo> list = new ArrayList<>();

        try {
            Connection cnn = new Connection(ctx, DB_NAME, null, 1);
            SQLiteDatabase db = cnn.getReadableDatabase();
            String[] columns = {
                    ID_KEY,
                    NAME_KEY,
                    DESCRIPTION_KEY,
                    PHOTO_KEY
            };
            Cursor c = db.query(TABLE, columns, null, null, null, null, null);

            while (c.moveToNext()) {
                Photo p = new Photo(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
                list.add(p);
            }
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return list;
    }

    public static ArrayList<String> getPhotoList(Context ctx) {
        ArrayList<String> list = new ArrayList<>();

        ArrayList<Photo> photos = getPhotoArray(ctx);

        for (int i=0; i<photos.size(); i++) {
            list.add(photos.get(i).getId().toString() + " - " + photos.get(i).getName() + ": " + photos.get(i).getDescription());
        }

        return list;
    }

    public static String getPhotoPath(Context ctx, String id) {
        String path = null;

        try {
            Connection cnn = new Connection(ctx, DB_NAME, null, 1);
            SQLiteDatabase db = cnn.getReadableDatabase();
            Cursor c = db.query(TABLE, new String[] {PHOTO_KEY}, ID_KEY + "=?", new String[] {id}, null, null, null);

            if (c.getCount() > 0) {
                c.moveToFirst();
                path = c.getString(0);
            }
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return path;
    }

    public static boolean deletePhoto(Context ctx, String id) {
        try {
            Connection cnn = new Connection(ctx, DB_NAME, null, 1);
            SQLiteDatabase db = cnn.getWritableDatabase();
            db.delete(TABLE, ID_KEY + "=?", new String[] {id});
            Toast.makeText(ctx, "Photo eliminada!!", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception ex) {
            Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
