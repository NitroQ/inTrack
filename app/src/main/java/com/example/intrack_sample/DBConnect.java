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
//        DB.execSQL("CREATE TABLE UserRecord (user_id INTEGER NOT NULL, Time_In DATE, Time_Out DATE, Record_Date DATE NOT NULL)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP Table if exists UserLogin");
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

//    public Boolean updateUserdata (String name, String contact){
//        SQLiteDatabase DB = this.getWritableDatabase();
//        ContentValues contentVal = new ContentValues();
//        contentVal.put("contact", contact);
//
//        Cursor cursor = DB.rawQuery("SELECT * FROM UserLogin WHERE name = ?", new String[]{name});
//
//        if (cursor.getCount()>0){
//            long result = DB.update("UserLogin",contentVal, "name=?", new String[]{name});
//
//            if(result == -1){
//                return false;
//            }else{
//                return true;
//            }
//
//        }else{
//            return false;
//        }
//
//    }
//
//    public Boolean deletedata (String name){
//        SQLiteDatabase DB = this.getWritableDatabase();
//        ContentValues contentVal = new ContentValues();
//
//        Cursor cursor = DB.rawQuery("SELECT * FROM UserLogin WHERE name = ?", new String[]{name});
//
//        if (cursor.getCount()>0){
//            long result = DB.delete("UserLogin","name=?", new String[]{name});
//
//            if(result == -1){
//                return false;
//            }else{
//                return true;
//            }
//
//        }else{
//            return false;
//        }
//
//    }

    public Cursor userLogin (String uname, String pass){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM UserLogin WHERE username = ? AND password = ?", new String[]{uname, pass});

        return cursor;

    }
}
