package com.example.emailapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ImageView
import java.util.*

class secondScreen : AppCompatActivity() {

    lateinit var tts : TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_screen)


        // This is for Text to speech in the second screen, that is (Compose email or Read Emails)
        tts = TextToSpeech(applicationContext,TextToSpeech.OnInitListener {
            if(it == TextToSpeech.SUCCESS){
                tts.language = Locale.US
                tts.setSpeechRate(0.8f)
                tts.speak("Login Successful, Click the upper half of the screen to Compose Email, and lower half of the screen to Read your mails", TextToSpeech.QUEUE_ADD,null)
            }
        })

        // Compose email Button
        val button = findViewById<ImageView>(R.id.composeBtn)
        button.setOnClickListener{
            val intent = Intent(this, emailBody::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
            finishAffinity()
    }
}