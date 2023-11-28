package com.example.storage_memojang

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    var filename:String="memoTest"
    lateinit var edtext:EditText
    var number:Int=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtext=findViewById(R.id.memosheet)
        var readbtn: Button =findViewById(R.id.read)
        var writebtn: Button =findViewById(R.id.write)
        readbtn.setOnClickListener() { //메모장에 쓰기
            try {
                var filename2 = filename + number + ".txt"
                var inputfile: FileInputStream = openFileInput(filename2)
                var buffer = ByteArray(inputfile.available())
                inputfile.read(buffer)
                edtext.setText(buffer.decodeToString()) //버퍼에 저장한걸 스트링으로 변환.
                inputfile.close()
            }catch (e: FileNotFoundException) {
                // 파일이 존재하지 않을 때 처리
                Toast.makeText(this, "파일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(this, "파일 읽기 오류", Toast.LENGTH_SHORT).show()
            }
        }

        writebtn.setOnClickListener(){
            try {
                var filename2 = filename + number + ".txt"
                var outputfile: FileOutputStream = openFileOutput(filename2, Context.MODE_PRIVATE)
                outputfile.write(edtext.text.toString().toByteArray())
                outputfile.close()
                number++
            }catch (e:Exception) {
                Toast.makeText(this, "파일 쓰기 오류", Toast.LENGTH_SHORT).show()
            }
        }
    }
}