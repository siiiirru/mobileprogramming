package com.example.everyonces_awards

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PDBhelper(context: Context) : SQLiteOpenHelper(context, "prizeDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE prize (pTitle TEXT PRIMARY KEY, pGiver TEXT, pReceiver TEXT,pContext TEXT, pDate TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS prize")
        onCreate(db)
    }
}