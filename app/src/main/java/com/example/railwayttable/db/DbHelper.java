package com.example.railwayttable.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION =7;

    public static final String TABLE_NAME = "startowa";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STACJA = "stacjaPocz";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";


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
                        + COLUMN_STACJA + " TEXT,"
                        +   COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0" +")";
        String CREATE_KONCOWA_TABLE =
                "CREATE TABLE " + TABLE_NAME_K + "("
                        + COLUMN_ID_K + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_DESTINATION + " TEXT,"
                        +  COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0" + ")";
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
        Values.put(COLUMN_IS_FAVORITE, 1);
        long insert = db.insert(TABLE_NAME, null, Values);
        return insert != -1;
    }

    public boolean updateStartowa(StartStationModel startStationModel, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, isFavorite ? 1 : 0);

        String selection = COLUMN_STACJA + " = ?";
        String[] selectionArgs = {startStationModel.getStacjaPocz()};

        int rowsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);

        db.close();

        return rowsUpdated > 0;
    }
    public boolean updateKoncowa(DestinationModel destinationModel, boolean isFavorite) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, isFavorite ? 1 : 0);

        String selection = COLUMN_DESTINATION + " = ?";
        String[] selectionArgs = {destinationModel.getStacjaKon()};

        int rowsUpdated = db.update(TABLE_NAME_K, values, selection, selectionArgs);

        db.close();

        return rowsUpdated > 0;
    }
    public boolean addKoncowa(DestinationModel destinationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues Values = new ContentValues();
        Values.put(COLUMN_DESTINATION, destinationModel.getStacjaKon());
        Values.put(COLUMN_IS_FAVORITE, 1);
        long insert = db.insert(TABLE_NAME_K, null, Values);
        return insert != -1;
    }

    @SuppressLint("Range")
    public boolean isFavorite(StartStationModel startStationModel) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_IS_FAVORITE};
        String selection = COLUMN_STACJA + " = ?";
        String[] selectionArgs = {startStationModel.getStacjaPocz()};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean isFavorite = false;

        if (cursor.moveToFirst()) {
            isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1;
        }

        cursor.close();
        db.close();

        return isFavorite;
    }
    @SuppressLint("Range")
    public boolean isFavorite(DestinationModel destinationModel) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_IS_FAVORITE};
        String selection = COLUMN_DESTINATION + " = ?";
        String[] selectionArgs = {destinationModel.getStacjaKon()};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean isFavorite = false;

        if (cursor.moveToFirst()) {
            isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1;
        }

        cursor.close();
        db.close();

        return isFavorite;
    }

    @SuppressLint("Range")
    public boolean isFavoriteByStationName(String stationName) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isFavorite = false;

        try {
            String[] columns = {COLUMN_IS_FAVORITE};
            String selection = COLUMN_STACJA + " = ?";
            String[] selectionArgs = {stationName};

            Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1;
            }

            cursor.close();
        } finally {
            db.close();
        }

        return isFavorite;
    }
    @SuppressLint("Range")
    public boolean isFavoriteDestinationStationName(String stationName) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isFavorite = false;

        try {
            String[] columns = {COLUMN_IS_FAVORITE};
            String selection = COLUMN_DESTINATION + " = ?";
            String[] selectionArgs = {stationName};

            Cursor cursor = db.query(TABLE_NAME_K, columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1;
            }

            cursor.close();
        } finally {
            db.close();
        }

        return isFavorite;
    }
    public StartStationModel getModelByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString =  "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?";
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

    public boolean deleteStartStation(StartStationModel startStationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME,
                COLUMN_ID + " = ? AND " + COLUMN_IS_FAVORITE + " = 1",
                new String[]{String.valueOf(startStationModel.getId())});
        return rowsAffected > 0;
    }

    public DestinationModel getDestModelByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = " SELECT * FROM " + TABLE_NAME_K + " WHERE " + COLUMN_ID_K + " = ?";
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

    public List<StartStationModel> getFavoriteStartStations() {
        List<StartStationModel> favoriteStartStations = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_IS_FAVORITE + " = 1";
        Cursor cursor = db.rawQuery(queryString, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    @SuppressLint("Range") String stacjaP = cursor.getString(cursor.getColumnIndex(COLUMN_STACJA));
                    @SuppressLint("Range") boolean isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1;

                    StartStationModel startStationModel = new StartStationModel(id, stacjaP);
                    startStationModel.setFavorite(isFavorite);

                    favoriteStartStations.add(startStationModel);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return favoriteStartStations;
    }

    public List<DestinationModel> getDestinationStations() {
        List<DestinationModel> favoriteEndStations = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TABLE_NAME_K + " WHERE " + COLUMN_IS_FAVORITE + " = 1";
        Cursor cursor = db.rawQuery(queryString, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    @SuppressLint("Range") String stacjaP = cursor.getString(cursor.getColumnIndex(COLUMN_STACJA));
                    @SuppressLint("Range") boolean isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1;

                    DestinationModel destinationModel = new DestinationModel(id, stacjaP);
                    destinationModel.setFavorite(isFavorite );

                    favoriteEndStations.add(destinationModel);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
            db.close();
        }

        return favoriteEndStations;

    }

    public List<StartStationModel> getAllStartStations() {
        List<StartStationModel> startStationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String stacjaPocz = cursor.getString(cursor.getColumnIndex(COLUMN_STACJA));
                @SuppressLint("Range") boolean isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1;

                StartStationModel startStation = new StartStationModel(id, stacjaPocz);
                startStation.setFavorite(isFavorite);

                startStationList.add(startStation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return startStationList;
    }


    public List<DestinationModel> getAllDestinationStations() {
        List<DestinationModel> destinationStationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_K;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_K));
                @SuppressLint("Range") String stacjaKon = cursor.getString(cursor.getColumnIndex(COLUMN_DESTINATION));
                @SuppressLint("Range") boolean isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FAVORITE)) == 1;

                DestinationModel destinationStation = new DestinationModel(id, stacjaKon);
                destinationStation.setFavorite(isFavorite);

                destinationStationList.add(destinationStation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return destinationStationList;
    }


    public boolean deleteDestination(DestinationModel destinationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME_K,
                COLUMN_ID_K + " = ? AND " + COLUMN_IS_FAVORITE + " = 1",
                new String[]{String.valueOf(destinationModel.getId())});
        return rowsAffected > 0;
    }

}
