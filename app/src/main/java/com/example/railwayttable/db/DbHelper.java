package com.example.railwayttable.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "startowa";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STACJA = "stacjaPocz";


    public static final String TABLE_NAME_K = "koncowa";
    public static final String COLUMN_ID_K = "id";
    public static final String COLUMN_DESTINATION = "stacjaPocz";
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STARTOWA_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_STACJA + " TEXT"
                        + ")";
        String CREATE_KONCOWA_TABLE =
                "CREATE TABLE " + TABLE_NAME_K + "("
                        + COLUMN_ID_K + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_DESTINATION + " TEXT"
                        + ")";
        db.execSQL(CREATE_STARTOWA_TABLE);
        db.execSQL(CREATE_KONCOWA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS startowa");
        db.execSQL("DROP TABLE IF EXISTS koncowa");
        onCreate(db);
    }

    public boolean addStartowa(StartStationModel startStationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Values = new ContentValues();
        Values.put(COLUMN_STACJA, startStationModel.getStacjaPocz());
        long insert = db.insert(TABLE_NAME, null, Values);
        return insert != -1;
    }

    public boolean addKoncowa(DestinationModel destinationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Values = new ContentValues();
        Values.put(COLUMN_DESTINATION, destinationModel.getStacjaKon());
        long insert = db.insert(TABLE_NAME_K, null, Values);
        return insert != -1;
    }

    public StartStationModel getModelByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = " SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            int column_id = cursor.getInt(0);
            String stacjaP = cursor.getString(1);

            return new StartStationModel(column_id, stacjaP);
        }
        cursor.close();
        db.close();
        return null;
    }

    public boolean deleteStartStationByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=" + id, null) > 0;
    }

    public DestinationModel getDestModelByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = " SELECT * FROM " + TABLE_NAME_K + " WHERE " + COLUMN_ID_K + " = " + id;
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            int column_id = cursor.getInt(0);
            String destination = cursor.getString(1);

            return new DestinationModel(column_id, destination);
        }
        cursor.close();
        db.close();
        return null;
    }

    public boolean deleteDestinationByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_K, COLUMN_ID_K + "=" + id, null) > 0;
    }

}
