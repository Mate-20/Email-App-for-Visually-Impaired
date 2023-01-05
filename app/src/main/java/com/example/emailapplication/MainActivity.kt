package com.example.emailapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var tts: TextToSpeech
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                tts.setSpeechRate(0.8f)
                tts.speak(
                    " TO speak your email, tap on bottom right of your screen",
                    TextToSpeech.QUEUE_ADD, null
                )
            }
        })
        micBtn0.setOnClickListener {
            speechrecog()
        }
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                val value = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            // If ID and PASS are correct, we are logging in
            if (count == 0) {
                userEmail.setText(
                    value?.get(0).toString().replace("\\s".toRegex(), "").lowercase()
                )
                tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                    if (it == TextToSpeech.SUCCESS) {
                        tts.language = Locale.US
                        tts.setSpeechRate(0.8f)
                        tts.speak(
                            " Your email is: " + userEmail.text.toString() + " To confirm it, tap on bottom left of the screen or bottom right to speak again",
                            TextToSpeech.QUEUE_ADD, null
                        )
                    }
                })
                buttonConfirm.setOnClickListener {
                    count++
                    tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                        if (it == TextToSpeech.SUCCESS) {
                            tts.language = Locale.US
                            tts.setSpeechRate(0.8f)
                            tts.speak(
                                " You have confirmed your email, tap on bottom right to enter the password",
                                TextToSpeech.QUEUE_ADD,
                                null
                            )
                        }
                    })
                }
            } else if (count == 1) {
                userPassword.setText(value?.get(0).toString())
                tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                    if (it == TextToSpeech.SUCCESS) {
                        tts.language = Locale.US
                        tts.setSpeechRate(0.8f)
                        tts.speak(
                            " Your Password is. " + userPassword.text.toString() + " To confirm it, long press the bottom left of the screen, or press on bottom right corner to speak again",
                            TextToSpeech.QUEUE_ADD, null
                        )
                    }
                })
                buttonConfirm.setOnLongClickListener {
                    if (userEmail.text.toString() == "abc@gmail.com" && userPassword.text.toString() == "hello world") {
                        val intent = Intent(this, secondScreen::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Wrong Email or Password", Toast.LENGTH_SHORT).show()

                    }
                    true
                }
            }


        }

    }
    private fun speechrecog() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Speech not available", Toast.LENGTH_SHORT).show()
        } else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak")
            getResult.launch(i)
        }
    }
}



