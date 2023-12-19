package com.example.everyonces_awards

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class Makeprize:AppCompatActivity() {
    lateinit var butmake:Button
    lateinit var pTitle:EditText
    lateinit var pGiver:EditText
    lateinit var pReceiver:EditText
    lateinit var pContext:EditText
    lateinit var pDate:EditText
    lateinit var mydbhelper:PDBhelper
    lateinit var mysqlDB:SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.makeprize)
        pTitle=findViewById(R.id.ptitle)
        pGiver=findViewById(R.id.pgiver)
        pReceiver=findViewById(R.id.preceiver)
        pContext=findViewById(R.id.pcontext)
        pDate=findViewById(R.id.pdate)
        butmake=findViewById(R.id.make)

        //홈버튼
        var gohome: Button =findViewById(R.id.gohome2)
        gohome.setOnClickListener(){
            var intend = Intent(applicationContext,MainActivity::class.java)
            startActivity(intend)
        }

        //원래 입력값 표시
        mydbhelper= PDBhelper(this)
        mysqlDB=mydbhelper.readableDatabase
        var cursor: Cursor
        cursor=mysqlDB.rawQuery("SELECT * FROM prize;",null)
        if(cursor.moveToFirst()){
            pTitle.setText(cursor.getString(0))
            pGiver.setText(cursor.getString(1))
            pReceiver.setText(cursor.getString(2))
            pContext.setText(cursor.getString(3))
            pDate.setText(cursor.getString(4))
        }
        cursor.close()
        mysqlDB.close()

        //상장 만들기 버튼
        butmake.setOnClickListener(){
            if(pTitle.text.isEmpty()||pGiver.text.isEmpty()||pReceiver.text.isEmpty()||pContext.text.isEmpty()||pDate.text.isEmpty())
                Toast.makeText(this,"모든 항목에 입력하지 않았습니다.",Toast.LENGTH_SHORT).show()
            else {
                var v1=pTitle.text.toString()
                var v2=pGiver.text.toString()
                var v3=pReceiver.text.toString()
                var v4=pContext.text.toString()
                var v5=pDate.text.toString()
                //이미 입력값 있으면
                if(checkExist()==true)
                {
                    mysqlDB=mydbhelper.writableDatabase
                    val values = ContentValues()
                    values.put("pTitle", v1)
                    values.put("pGiver", v2)
                    values.put("pReceiver", v3)
                    values.put("pContext",v4)
                    values.put("pDate",v5)
                    mysqlDB.update("prize", values,null,null)
                    mysqlDB.close()
                }
                else{
                    mysqlDB=mydbhelper.writableDatabase
                    mysqlDB.execSQL("INSERT INTO prize VALUES ('"+v1+"','"+v2+"','"+v3+"','"+v4+"','"+v5+"');")
                    mysqlDB.close()
                }
                Toast.makeText(this,"입력되었습니다.",Toast.LENGTH_SHORT).show()
                var intend = Intent(applicationContext,Resultprize::class.java)
                startActivity(intend) //makeawards화면으로
            }

        }

    }

    //입력된 값이 있는지 체크
    fun checkExist():Boolean{
        mydbhelper= PDBhelper(this)
        mysqlDB=mydbhelper.readableDatabase
        var cursor:Cursor
        cursor=mysqlDB.rawQuery("SELECT * FROM prize;",null)
        if(cursor.moveToFirst()){
            cursor.close()
            mysqlDB.close()
            return true
        }
        cursor.close()
        mysqlDB.close()
        return false
    }

}