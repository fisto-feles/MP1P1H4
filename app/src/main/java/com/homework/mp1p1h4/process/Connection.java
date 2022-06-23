package com.homework.mp1p1h4.process;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.concurrent.TransferQueue;

public class Connection extends SQLiteOpenHelper {
    public Connection(@Nullable Context ctx, @Nullable String dbName, @Nullable SQLiteDatabase.CursorFactory cursor, int version) {
        super(ctx, dbName, cursor, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PhotoTransaction.getCreateTableSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PhotoTransaction.getDropTableSQL());
        onCreate(db);
    }
}
