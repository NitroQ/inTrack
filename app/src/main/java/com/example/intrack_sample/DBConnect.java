package com.example.intrack_sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnect extends SQLiteOpenHelper {


    public DBConnect(Context context) {
        super(context, "Userdata.db", null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE UserLogin (user_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, fname TEXT NOT NULL, lname TEXT NOT NULL, username TEXT UNIQUE NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL)");
        DB.execSQL("CREATE TABLE UserRecord (user_id INTEGER NOT NULL, Time_In VARCHAR(6) NOT NULL, Time_Out VARCHAR(6) , Total INT, Record_Date DATE UNIQUE NOT NULL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP Table if exists UserLogin");
        DB.execSQL("DROP Table if exists UserRecord");
    }

    public Boolean register (String fname,String lname, String username, String email, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentVal = new ContentValues();

        contentVal.put("fname", fname);
        contentVal.put("lname", lname);
        contentVal.put("username", username);
        contentVal.put("email", email);
        contentVal.put("password", password);
        long results = DB.insert("UserLogin", null, contentVal);

        if(results == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor userLogin (String uname, String pass){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM UserLogin WHERE username = ? AND password = ?", new String[]{uname, pass});

        return cursor;

    }

    public Boolean findExistingDate (String id, String Record_Date){

        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM UserRecord WHERE user_id = ? AND Record_Date = ?", new String[]{id, Record_Date});
        if(cursor.getCount() == 0){
            return false;
        }else{
            return true;
        }
    }
    public Boolean findExistingTime (String id, String Record_Date){
        Boolean time = false;
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM UserRecord WHERE user_id = ? AND Record_Date = ?", new String[]{id, Record_Date});
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                if(cursor.getString(2) != null){
                    time = true;
                }
            }
        }else{
                time = false;
        }
        return time;
    }

    public Boolean TimeIn (String user_id, String Time_In, String Record_Date){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentVal = new ContentValues();

        contentVal.put("user_id", user_id);
        contentVal.put("Time_In", Time_In);
        contentVal.put("Record_Date", Record_Date);
        long results = DB.insert("UserRecord", null, contentVal);

        if(results == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor findTimeIn (String user_id,  String Record_Date){
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("SELECT * FROM UserRecord WHERE user_id = ? AND Record_Date = ?", new String[]{user_id, Record_Date});

        return cursor;
    }

    public Boolean TimeOut (String user_id, String Time_Out, String total,  String Record_Date){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentVal = new ContentValues();
        contentVal.put("Time_Out", Time_Out);
        contentVal.put("Total", total);

        Cursor cursor = DB.rawQuery("SELECT * FROM UserRecord WHERE user_id = ? AND Record_Date = ?", new String[]{user_id, Record_Date});

        if (cursor.getCount()>0){
            long result = DB.update("UserRecord",contentVal, "user_id = ? AND Record_Date = ?", new String[]{user_id, Record_Date});

            if(result == -1){
                return false;
            }else{
                return true;
            }

        }else{
            return false;
        }
    }

    public Cursor finduserData (String id){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM UserRecord WHERE user_id = ? ", new String[]{id});
        return cursor;
    }

}
