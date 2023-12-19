package com.example.everyonces_awards

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Inputawards : AppCompatActivity(){
    lateinit var category:TextView
    lateinit var mydbhelper:DBhelper
    lateinit var mysqlDB: SQLiteDatabase
    lateinit var finish:Button
    lateinit var first:EditText
    lateinit var second:EditText
    lateinit var third:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inputawards)
        first=findViewById(R.id.firstinput)
        second=findViewById(R.id.secondinput)
        third=findViewById(R.id.thirdinput)
        category=findViewById(R.id.category)
        //카테고리명 인텐트 받아오기
        var getintent=intent
        var cateString:String?=getintent.getStringExtra("category")
        category.setText(cateString)//받은 카테고리명을 제목에 출력.
        finish=findViewById(R.id.done)

        var goback:Button=findViewById(R.id.goback)
        goback.setOnClickListener(){
            var intend = Intent(applicationContext,Makeawards::class.java)
            startActivity(intend)
        }

        //이미 입력된 내용이 있으면 버튼을 수정하기로 변경
        mydbhelper= DBhelper(this)
        mysqlDB=mydbhelper.readableDatabase
        var cursor: Cursor
        cursor=mysqlDB.rawQuery("SELECT * FROM awards WHERE category = '${cateString}';",null)
        if (cursor.moveToFirst()) {
            finish.setText("수정하기")
            first.setText(cursor.getString(1))
            second.setText(cursor.getString(2))
            third.setText(cursor.getString(3))
        }
        cursor.close()
        mysqlDB.close()

        finish.setOnClickListener(){
            mysqlDB=mydbhelper.writableDatabase
            val firstValue = first.text.toString()
            val secondValue = second.text.toString()
            val thirdValue = third.text.toString()

            //빠진게 있다면 경고알람
            if(first.text.isEmpty()||second.text.isEmpty()||third.text.isEmpty())
                Toast.makeText(this,"모든 항목을 채워주세요.",Toast.LENGTH_SHORT).show()
            //버튼이 수정하기면 데이터베이스 수정
            else if(finish.text=="수정하기"){
            val values = ContentValues()
            values.put("fContext", firstValue)
            values.put("sContext", secondValue)
            values.put("tContext", thirdValue)
            mysqlDB.update(
                "awards",
                values,      // 업데이트할 데이터
                "category=?",  // WHERE 조건
                arrayOf(cateString)  // 조건 값
            )
                mysqlDB.close()
                Toast.makeText(this,"수정되었습니다.",Toast.LENGTH_SHORT).show()
                var intend = Intent(applicationContext,Makeawards::class.java)
                startActivity(intend) //makeawards화면으로
            }
            //원래 아무 것도 없었다면 입력된 내용을 데이터베이스에 저장.
            else {
                mysqlDB.execSQL("INSERT INTO awards VALUES ('"+cateString+"','"+firstValue+"','"+secondValue+"','"+thirdValue+"');")
                mysqlDB.close()
                Toast.makeText(this,"입력되었습니다.",Toast.LENGTH_SHORT).show()
                var intend = Intent(applicationContext,Makeawards::class.java)
                startActivity(intend) //makeawards화면으로
            }
        }
    }

}