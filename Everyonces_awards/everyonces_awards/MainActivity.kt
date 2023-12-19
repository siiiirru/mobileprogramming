package com.example.everyonces_awards

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {
    var soundintent:Intent?=null
    lateinit var butsong:ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var makeawards:Button=findViewById(R.id.makeawds)
        var makeprize:Button=findViewById(R.id.makeprize)

        //노래끄기
        butsong=findViewById(R.id.sound)
        butsong.setOnCheckedChangeListener(){buttonView, isChecked ->
            if(isChecked){
                if (soundintent == null)
                    soundintent=Intent(this,MusicService::class.java)
                startService(soundintent)
            }
            else if(soundintent != null){
                stopService(soundintent)
                soundintent = null}
        }

        makeawards.setOnClickListener(){
            var intend1 = Intent(applicationContext,Makeawards::class.java)
            startActivity(intend1)
        }
        makeprize.setOnClickListener(){
            var intend2 = Intent(applicationContext,Makeprize::class.java)
            startActivity(intend2)
        }


    }
}

class MusicService:android.app.Service(){
    lateinit var mp:MediaPlayer
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!::mp.isInitialized || !mp.isPlaying) {
            mp = MediaPlayer.create(this, R.raw.standardjazzbars)
            mp.isLooping = true
            mp.start()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mp.stop()
        super.onDestroy()
    }
}