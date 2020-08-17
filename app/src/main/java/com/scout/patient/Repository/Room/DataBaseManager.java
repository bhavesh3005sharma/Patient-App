package com.scout.patient.Repository.Room;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "my_Database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME="searching_data";
    private static final String COLUMN_TITLE ="title";
    private static final String COLUMN_URL="url";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_TYPE = "type" ; // Type 1 : Hospital ,Type 2 : Doctor ,Type 3 : HistoryData

    public DataBaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table "+TABLE_NAME+"("+COLUMN_TITLE+" TEXT, "+COLUMN_URL+" TEXT,"+COLUMN_ID+" TEXT,"+COLUMN_TYPE+" TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String userId ,String title, String url, String type){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_URL,url);
        cv.put(COLUMN_ID,userId);
        cv.put(COLUMN_TYPE,type);

        long result = sqLiteDatabase.insert(TABLE_NAME,null,cv);

        if(result==-1)
            return false;
        else
            return true;

        // result==1 means task is successful.
    }

    public Integer deleteData(String title){
        SQLiteDatabase db=this.getWritableDatabase();
        String[] args={title,"3"};
        return db.delete(TABLE_NAME,"title=? AND "+COLUMN_TYPE+"=?",args);
    }

    public int cleanSuggestionHistory() {
        SQLiteDatabase db=this.getWritableDatabase();
        String[] args={"3"};
        return db.delete(TABLE_NAME,COLUMN_TYPE+"=?",args);
    }

    public int clearHospitalsList() {
        SQLiteDatabase db=this.getWritableDatabase();
        String[] args={"1"};
        return db.delete(TABLE_NAME,COLUMN_TYPE+"=?",args);
    }

    public int clearDoctorsList() {
        SQLiteDatabase db=this.getWritableDatabase();
        String[] args={"2"};
        return db.delete(TABLE_NAME,COLUMN_TYPE+"=?",args);
    }

    public Cursor getAllHospitalsData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM searching_data WHERE "+COLUMN_TYPE+" = 1",null);
    }

    public Cursor getAllDoctorsData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM searching_data WHERE "+COLUMN_TYPE+" = 2",null);
    }

    public Cursor getAllPreviousData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM searching_data WHERE "+COLUMN_TYPE+" = 3",null);
    }
}
