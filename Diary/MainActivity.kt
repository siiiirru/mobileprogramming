package com.example.diary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    lateinit var edtext:EditText
    lateinit var readbtn:Button
    lateinit var savebtn:Button
    var number:Int=1
    var readnum:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtext=findViewById(R.id.editTextText)
        readbtn=findViewById(R.id.read)
        savebtn=findViewById(R.id.save)
        var state:String=Environment.getExternalStorageState() //sd카드 장착정보 가져오기
        if(state.equals(Environment.MEDIA_MOUNTED)==false)
            Toast.makeText(this, "sd카드가 장착되어있지 않습니다.", Toast.LENGTH_SHORT).show()

    readbtn.setOnClickListener(){
        try{
        var filename=getExternalFilesDir(null)?.absolutePath //sd카드 경로
        filename+="/DiaryPark"+readnum+".txt"
        var inputstream:InputStream=FileInputStream(filename)
        var buffer=ByteArray(inputstream.available())
        inputstream.read(buffer)
        edtext.setText(buffer.decodeToString())
            inputstream.close()
            readnum++
        }catch (e:Exception){
            if (readnum>=number)
                Toast.makeText(this,"모두 읽었습니다.",Toast.LENGTH_SHORT).show()
            else Toast.makeText(this,"읽기오류",Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
        savebtn.setOnClickListener(){
            try{
                var filename=getExternalFilesDir(null)?.absolutePath
                filename+="/DiaryPark"+number+".txt"
                var outputstream: OutputStream = FileOutputStream(filename)
                outputstream.write(edtext.text.toString().toByteArray())
                outputstream.close()
                number++
            }catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(this,"쓰기오류",Toast.LENGTH_SHORT).show()
            }
        }

    }
}