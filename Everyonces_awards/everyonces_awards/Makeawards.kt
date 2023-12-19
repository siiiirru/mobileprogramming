package com.example.everyonces_awards

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Makeawards : AppCompatActivity(){
    var butcount:Int=12
    lateinit var buttons: Array<Button>
    lateinit var buttonsid:IntArray
    lateinit var imageViews:Array<ImageView>
    lateinit var imageViewid:IntArray
    lateinit var mydbhelper:DBhelper
    lateinit var mysqlDB: SQLiteDatabase
    lateinit var goResult:Button
    lateinit var butname:Button
    lateinit var name:EditText
    val PREFS_NAME = "MyPrefsFile"
    val KEY_USER_NAME = "user_name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.makeawards)
        //위젯 초기화
        buttonsid=intArrayOf(R.id.song, R.id.movie, R.id.drama, R.id.book, R.id.friends,R.id.fashion,R.id.food,R.id.travle,R.id.smile,R.id.sad,R.id.angry,R.id.good)
        imageViewid=intArrayOf(R.id.songok, R.id.movieok, R.id.dramaok, R.id.bookok, R.id.friendsok,R.id.fashionok,R.id.foodok,R.id.travleok,R.id.smileok,R.id.sadok,R.id.angryok,R.id.goodok)
        buttons = Array(butcount) { findViewById<View>(buttonsid[it]) as Button }
        imageViews = Array(butcount) { findViewById<View>(imageViewid[it]) as ImageView }
        butname=findViewById(R.id.butname)
        name=findViewById(R.id.name)

        //홈으로 가는 버튼
        var gohome:Button=findViewById(R.id.gohome)
        gohome.setOnClickListener(){
            var intend = Intent(applicationContext,MainActivity::class.java)
            startActivity(intend)
        }

        //이름저장
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var storedUserName = sharedPreferences.getString(KEY_USER_NAME, "")
        name.setText(storedUserName)

        butname.setOnClickListener(){
            if(name.text.isEmpty())Toast.makeText(this,"항목이 비어있습니다.",Toast.LENGTH_SHORT).show()
            else{
                val enteredUserName = name.text.toString()
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(KEY_USER_NAME, enteredUserName)
                editor.apply()
                Toast.makeText(this,"저장되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }


        //데이터베이스를 확인해 입력완료 된 항목인지 체크해 ok사인들이 표시되게 한다.
        mydbhelper= DBhelper(this);
        checkOk(imageViews,buttons)


        //모두 입력되면 결과보기 창으로 이동한다.
        goResult=findViewById(R.id.button2)
        checkDone()

        //초기화 버튼
        var butreset:Button=findViewById(R.id.reset)
        butreset.setOnClickListener(){
            var dlg=AlertDialog.Builder(this)
            dlg.setTitle("어워즈 전체 초기화")
            dlg.setMessage("전체 항목을 초기화 하시겠습니까?")
            dlg.setPositiveButton("네"){ dialog,which ->
                mysqlDB=mydbhelper.writableDatabase //내용을 쓸 수 있는 데이터베이스인 mysqlDB를 생성.
                mydbhelper.onUpgrade(mysqlDB,1,2)
                mysqlDB.close()
                val intent = intent
                finish()
                startActivity(intent)
            }
            dlg.setNegativeButton("아니요"){dialog,which->
            }
            dlg.show()
        }
    }

    //버튼을 누르면 inputawards로 버튼의 텍스트를 인텐트로 전달한다.
    fun goinput(e:View){
        if(e is Button){
            var buttext:String =e.text.toString()
            var intend3=Intent(applicationContext,Inputawards::class.java)
            intend3.putExtra("category",buttext)
            startActivity(intend3)
        }
    }

    //입력 완료 항목여부 체크
    fun checkOk(imgv:Array<ImageView>,but:Array<Button>){
        mysqlDB=mydbhelper.readableDatabase
        var cursor: Cursor
        for (i in 0 until butcount) {
            cursor=mysqlDB.rawQuery("SELECT COUNT(*) FROM awards WHERE category = '${but[i].text}';",null)
            var count:Int=0
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
            if (count==1) imgv[i].visibility= View.VISIBLE
            cursor.close()
        }
        mysqlDB.close()
    }

    //모두 입력됐는지 체크
    fun checkDone(){
        mysqlDB=mydbhelper.readableDatabase
        var cursor: Cursor
        cursor=mysqlDB.rawQuery("SELECT COUNT(*) FROM awards;",null) //커서에 SELECT 결과과 저장됨.
        var count:Int=0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        if (count==butcount) goResult.setOnClickListener(){
            var intend2 = Intent(applicationContext,Resultawards::class.java)
            startActivity(intend2)
        }
        else goResult.setOnClickListener(){
            Toast.makeText(this,"모두 입력되지 않았습니다.",Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        mysqlDB.close()
    }
}