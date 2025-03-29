package com.example.cs4084_finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "lolzBankDB";

    private static final int DB_VERSION = 2;

    private static final String JOKES_TABLE = "mySavedJokes";

    private static final String MEMES_TABLE = "mySavedMemes";

    private static final String ID_COL = "id";

    private static final String DATE_ADDED = "dateAdded";

    private static final String JOKE_COL=  "joke";

    private static final String MEME_COL = "meme";

    //creating constructor for dbHandler
    public DBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createJokesTable = "CREATE TABLE " + JOKES_TABLE + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_ADDED + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + JOKE_COL + " TEXT)";

        String createMemesTable = "CREATE TABLE " + MEMES_TABLE + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_ADDED + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + MEME_COL + " TEXT)";

        db.execSQL(createJokesTable);
        db.execSQL(createMemesTable);
    }

    public void addNewJoke(String joke) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();

        value.put(JOKE_COL, joke);

        db.insert(JOKES_TABLE, null, value);

        db.close();
    }

    public void addNewMeme(String memeURL) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();

        value.put(MEME_COL, memeURL);

        db.insert(MEMES_TABLE, null, value);

        db.close();
    }

    public void deleteJokeByID(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(JOKES_TABLE, "id=?", new String[]{id});
    }

    public void deleteJoke(String joke){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(JOKES_TABLE, "joke=?", new String[]{joke});
    }

    public void deleteMemeByID(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MEMES_TABLE, "id=?", new String[]{id});
    }

    public void deleteMeme(String memeURL){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(JOKES_TABLE, "meme=?", new String[]{memeURL});
    }

    //method called to check if db exists already
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ JOKES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ MEMES_TABLE);
        onCreate(db);
    }

    public ArrayList<String> readJokes(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorJokes = db.rawQuery("SELECT * FROM "+ JOKES_TABLE, null);

        try{
            ArrayList<String> jokesList = new ArrayList<>();

            if(cursorJokes.moveToFirst()) {
                do {
                    int jokeIndex = cursorJokes.getColumnIndex(JOKE_COL);
                    if(jokeIndex != 1){
                        jokesList.add(cursorJokes.getString(jokeIndex));
                    }

                }while (cursorJokes.moveToNext());
            }
            cursorJokes.close();
            db.close();
            return jokesList;

        }catch(SQLException e){
            db.close();
            return null;
        }

    }

    public ArrayList<String> readMemes(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorJokes = db.rawQuery("SELECT * FROM "+ MEMES_TABLE, null);

        try{
            ArrayList<String> jokesList = new ArrayList<>();

            if(cursorJokes.moveToFirst()) {
                do {
                    int memeIndex = cursorJokes.getColumnIndex(MEME_COL);
                    if(memeIndex != 1){
                        jokesList.add(cursorJokes.getString(memeIndex));
                    }

                }while (cursorJokes.moveToNext());
            }
            cursorJokes.close();
            db.close();
            return jokesList;

        }catch(SQLException e){
            db.close();
            return null;
        }

    }
}
