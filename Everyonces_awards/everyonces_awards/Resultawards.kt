package com.example.everyonces_awards

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

class Resultawards : AppCompatActivity() {
    lateinit var mydbhelper:DBhelper
    lateinit var mysqlDB: SQLiteDatabase
    var textList = mutableListOf<List<String>>()
    val PREFS_NAME = "MyPrefsFile"
    val KEY_USER_NAME = "user_name"
    lateinit var awardsView: AwardsResultView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            awardsView = AwardsResultView(this)
            setContentView(awardsView)

            mydbhelper= DBhelper(this)
            mysqlDB=mydbhelper.readableDatabase

            //username 그리게하기
            val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            var username= sharedPreferences.getString(KEY_USER_NAME, "")
            awardsView.setName(username!!)

            var cursor: Cursor
            cursor=mysqlDB.rawQuery("SELECT * FROM awards;",null)
            while(cursor.moveToNext()){
                val row = listOf(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3))
                textList.add(row)
            }
            mysqlDB.close()
            cursor.close()
            awardsView.setData(textList)

            Log.d("Resultawards", "awardsView width: ${awardsView.width}, height: ${awardsView.height}")
        }
    override fun onResume() {
        super.onResume()

        awardsView.post {
            val filePath = awardsView.saveBitmapToFile(awardsView)
            if (filePath != null) {
                Log.d("ImageSave", "Image saved to: $filePath")
                Toast.makeText(this, "5초 뒤 공유페이지로 이동합니다.", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    val intent = Intent(applicationContext, Share::class.java)
                    intent.putExtra("filepath", filePath)
                    startActivity(intent)
                }, 5000)
            } else {
                Log.e("ImageSave", "Failed to save image.")
            }
        }
    }
}

class AwardsResultView(context: Context) : View(context) {

        private val backgroundImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.awardsresult1)// awardsresult 이미지를 Bitmap으로 로드
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val yearString = year.toString()+"년"
        val font: Typeface? = ResourcesCompat.getFont(context, R.font.peace)
        // 데이터베이스에서 읽어온 텍스트
        private var textList=mutableListOf<List<String>>()
        private var username: String?=null

     fun setName(username:String){
        this.username=username
        invalidate()
    }
    fun setData(textList: MutableList<List<String>>) {
        this.textList = textList
        // View를 다시 그리도록 요청
        invalidate()
    }
    private val namePaint: Paint = Paint().apply {
        textSize = 80f
        color = 0xFFFFFFFF.toInt() // 텍스트 색상
        typeface=font
    }
    private val yearPaint: Paint = Paint().apply {
        textSize = 80f
        color = 0xFFFFE400.toInt() // 텍스트 색상
        typeface=font
    }
    // 텍스트를 입힐 Paint 설정
    private val textPaint: Paint = Paint().apply {
        textSize = 30f
        color = 0xFF000000.toInt() // 텍스트 색상
    }

    //뷰로부터 비트맵 만들기
    fun getBitmapFromView(view:View): Bitmap {
        Log.d("AwardsResultView", "getBitmapFromView - width: $width, height: $height")
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    //만든 비트맵 내부저장소에 저장하기
    fun saveBitmapToFile(view:View): String? {
        val awardsBitmap = getBitmapFromView(view)
        val fileName = "awards_image.png"
        val directory = context.filesDir // 내부저장소의 파일 디렉토리

        val file = File(directory, fileName)

        try {
            val fileOutputStream = FileOutputStream(file)
            awardsBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // awardsresult 이미지를 그림
        canvas.drawBitmap(backgroundImage, null, Rect(0, 0, width, height), null)

        //이름 그리기
        username?.let {
            canvas.drawText(it, 350f, 110f, namePaint)
        }

        //년도 그리기
        yearString?.let{
            canvas.drawText(it,340f,197f,yearPaint)
        }

        // 데이터베이스에서 읽어온 텍스트를 이미지 위에 그림
        var yOffset = 265f // 세로 시작 위치

            // 값들 그리기
        for (rowIndex in textList!!.indices) {
            val row = textList!![rowIndex]

            //row의 인덱스가 textList의 1/2 미만이면
            if (rowIndex < textList!!.size / 2) {
                for (textIndex in row.indices) {
                    val text = row[textIndex]

                    if (textIndex == 0) { // 어워즈 부문
                        canvas.drawText(text, 50f, yOffset, textPaint)
                        yOffset += 75f
                    } else {
                        canvas.drawText(text, 120f, yOffset, textPaint)
                        yOffset += 60f // 값들 사이의 간격
                    }
                }
                yOffset += 25f
            }
            // row의 인덱스가 textList의 1/2 이상이면
            else {
                if(rowIndex==6)yOffset=350f
                for (textIndex in row.indices) {
                    val text = row[textIndex]

                    if (textIndex == 0) { // 어워즈 부문
                        canvas.drawText(text, 700f, yOffset, textPaint)
                        yOffset += 70f
                    } else {
                        canvas.drawText(text, 630f, yOffset, textPaint)
                        yOffset += 60f // 값들 사이의 간격
                    }
                }
                yOffset += 30f
            }
        }
        Log.d("AwardsResultView", "onDraw - width: $width, height: $height")
    }
}
