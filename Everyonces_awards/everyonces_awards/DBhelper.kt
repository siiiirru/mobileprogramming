package com.example.everyonces_awards

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBhelper (context: Context) : SQLiteOpenHelper(context, "awardsDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE awards(category TEXT PRIMARY KEY, fContext TEXT,sContext TEXT,tContext TEXT)") //contacts 테이블 생성
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS awards") //awards 테이블 초기화
        onCreate(db)
    }
}
