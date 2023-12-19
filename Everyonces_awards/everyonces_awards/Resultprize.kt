package com.example.everyonces_awards

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class Resultprize : AppCompatActivity() {
    lateinit var prizeView: PrizeResultView
    lateinit var mydbhelper: PDBhelper
    lateinit var mysqlDB:SQLiteDatabase
    var textList=mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prizeView = PrizeResultView(this)
        setContentView(prizeView)

        mydbhelper= PDBhelper(this)
        mysqlDB=mydbhelper.readableDatabase
        var cursor:Cursor
        cursor=mysqlDB.rawQuery("SELECT * FROM prize;",null)
        if(cursor.moveToFirst()){
            textList= mutableListOf(cursor.getString(0),cursor.getString(1),
                cursor.getString(2),cursor.getString(3),
                cursor.getString(4))
        }
        else Toast.makeText(this,"가져온 값이 없습니다.",Toast.LENGTH_SHORT).show()
        cursor.close()
        mysqlDB.close()

        prizeView.settextList(textList)

    }

    override fun onResume() {
        super.onResume()

        prizeView.post {
            val filePath = prizeView.saveBitmapToFile(prizeView)
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

class PrizeResultView(context: Context) : View(context) {
    private val backgroundImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.prizeresult)

    val font: Typeface? = ResourcesCompat.getFont(context, R.font.chosungs)
    private var textList=mutableListOf<String>()

    fun settextList(textList:MutableList<String>){
        this.textList=textList
        invalidate()
    }

    private val smallPaint: Paint = Paint().apply {
        textSize = 50f
        color = 0xFF000000.toInt() // 텍스트 색상
        typeface=font
    }
    private val bigPaint: Paint = Paint().apply {
        textSize = 70f
        color = 0xFF000000.toInt() // 텍스트 색상
        typeface=font
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
        val fileName = "prize_image.png"
        val directory = context.filesDir // 내부 저장소의 파일 디렉터리

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

        canvas.drawBitmap(backgroundImage, null, Rect(0, 0, width, height), null)
        if (textList.isNotEmpty()) {
            val text = textList!![3] // 내용 텍스트
            val textPaint = TextPaint(bigPaint) // bigPaint의 설정을 복사하여 TextPaint 초기화

            // 텍스트를 그릴 위치 및 영역 설정
            val x = 180f
            val y = 900f
            val width = 800

            // StaticLayout을 직접 생성
            val staticLayout =
                StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

            // 텍스트를 그릴 영역 지정
            val textRect =
                Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + staticLayout.height).toInt())

            // 캔버스에 텍스트 그리기
            drawTextOnCanvas(staticLayout, canvas, textRect)
            canvas.drawText(textList!![0], 200f, 660f, smallPaint) //상제목
            canvas.drawText(textList!![1], 400f, 1630f, smallPaint) //주는이
            canvas.drawText(textList!![2], 600f, 800f, bigPaint) //받는이
            textList[4] = formatDate(textList[4])
            canvas.drawText(textList!![4], 370f, 1500f, smallPaint) //날짜
        }

        // 캔버스에 텍스트 그리기 함수
    }
    private fun drawTextOnCanvas(layout: StaticLayout, canvas: Canvas, rect: Rect) {
            for (i in 0 until layout.lineCount) {
                val lineStart = layout.getLineStart(i)
                val lineEnd = layout.getLineEnd(i)
                val line = layout.text.subSequence(lineStart, lineEnd)
                canvas.drawText(line.toString(), rect.left.toFloat(), rect.top + layout.getLineBaseline(i).toFloat(), bigPaint)
            }
    }
    private fun formatDate(dateString: String): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())

            val date = inputFormat.parse(dateString)
            return outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dateString // 변환에 실패하면 그대로 반환
    }
}