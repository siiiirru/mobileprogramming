package com.example.contacts

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    lateinit var mydbhelper:dbHelper //새로 정의할 클래스
    lateinit var edtname:EditText
    lateinit var edtnum:EditText
    lateinit var edtlistname:EditText
    lateinit var edtlistnum:EditText
    lateinit var initbtn:Button
    lateinit var insbtn:Button
    lateinit var searchbtn:Button
    lateinit var mysqlDB:SQLiteDatabase

    class dbHelper(context: Context) : SQLiteOpenHelper(context, "telDB", null, 1) { //telDB는 데이터베이스 이름
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE contacts(gName CHAR(20) PRIMARY KEY, gphone CHAR(20))") //contacts 테이블 생성
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS contacts") //contacts 테이블 초기화
            onCreate(db)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtname=findViewById(R.id.editTextText)
        edtnum=findViewById(R.id.editTextPhone)
        edtlistname=findViewById(R.id.editTextTextMultiLine)
        edtlistnum=findViewById(R.id.editTextTextMultiLine2)
        initbtn=findViewById(R.id.button)
        insbtn=findViewById(R.id.button2)
        searchbtn=findViewById(R.id.button3)

        mydbhelper= dbHelper(this); //DBMS생성

        initbtn.setOnClickListener(){
            mysqlDB=mydbhelper.writableDatabase //내용을 쓸 수 있는 데이터베이스인 mysqlDB를 생성.
            mydbhelper.onUpgrade(mysqlDB,1,2)
            mysqlDB.close() //데이터를 변경시켜주는 작업을 멈춤
        }

        insbtn.setOnClickListener(){
            mysqlDB=mydbhelper.writableDatabase
            mysqlDB.execSQL("INSERT INTO contacts VALUES ('"+edtname.text.toString()+"','"+edtnum.text.toString()+"');")
            mysqlDB.close()
        }

        searchbtn.setOnClickListener(){
            mysqlDB=mydbhelper.readableDatabase
            var cursor: Cursor
            cursor=mysqlDB.rawQuery("SELECT * FROM contacts;",null) //커서에 SELECT 결과과 저장됨.
            var strname:String= "name \r\n"+"--------------"+"\r\n"
            var strnum:String="phone number \r\n"+"--------------"+"\r\n"
            while(cursor.moveToNext()){
                strname+=cursor.getString(0)+"\r\n"
                strnum+=cursor.getString(1)+"\r\n"
            }
            edtlistname.setText(strname)
            edtlistnum.setText(strnum)
            cursor.close()
            mysqlDB.close()
        }

    }
}