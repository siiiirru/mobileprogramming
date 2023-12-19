package com.example.everyonces_awards

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class Share: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.share)
        var getintent=intent
        var imagePath:String?=getintent.getStringExtra("filepath")
        var share: Button =findViewById(R.id.share)
        var gohome:Button=findViewById(R.id.gotohome)
        var imageView:ImageView=findViewById(R.id.imageView)

        // 이미지뷰에 비트맵 설정
        val bitmap = BitmapFactory.decodeFile(imagePath)
        imageView.setImageBitmap(bitmap)

        //이미지 공유
        share.setOnClickListener(){
            val imageUri = FileProvider.getUriForFile(
                this,
                "com.example.everyonces_awards.provider",
                File(imagePath)
            )

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/png"  // 이미지 타입에 따라 변경
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Share Image"))
        }

        gohome.setOnClickListener(){
            var intend = Intent(applicationContext,MainActivity::class.java)
            startActivity(intend)
        }

    }
}