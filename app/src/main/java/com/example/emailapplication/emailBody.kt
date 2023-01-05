package com.example.emailapplication

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.email_body.*
import java.util.*
import android.view.View.OnLongClickListener
import androidx.activity.result.contract.ActivityResultContracts


class emailBody: AppCompatActivity() {
    var count=0;// this count helps us in keeping the hold of edittexts
    lateinit var tts : TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.email_body)

        //Code for speaking in the Starting the application
        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if(it == TextToSpeech.SUCCESS){
                tts.language = Locale.US
                tts.setSpeechRate(0.8f)
                tts.speak("Whom do you want to sent the mail, Speak by clicking anywhere at the bottom right of the screen",
                    TextToSpeech.QUEUE_ADD,null)
            }
        })

        val mic : ImageView = findViewById(R.id.micBtn3)
        mic.setOnClickListener{
            speechrecog()
        }


    }
    //Code for speech to text in Receiver's email
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                val value = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//            val temp = result?.get(0).toString()
            if(count==0){
                mailTo.setText(value?.get(0).toString().replace("\\s".toRegex(), "").lowercase())
            }
            else if(count==1){
                mailSubject.setText(value?.get(0).toString())
            }else{
                var res=mailBody.text.toString()+ value?.get(0).toString()
                mailBody.setText(res)
            }
        }
        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if(it == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                tts.setSpeechRate(0.8f)
                if (count == 0) {//if its 0, content for sender's mail
                    tts.speak(
                        " You want to send it to " + mailTo.text.toString() +
                                " ,to confirm tap on bottom left of the screen, to speak again, tap on" +
                                "bottom right of the screen",
                        TextToSpeech.QUEUE_ADD, null
                    )
                    buttonConfirm2.setOnClickListener {
                        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                            if (it == TextToSpeech.SUCCESS) {
                                tts.language = Locale.US
                                tts.setSpeechRate(0.8f)
                                tts.speak(
                                    " You confirmed receiver's email. To add Subject, tap on bottom right of the screen",
                                    TextToSpeech.QUEUE_ADD, null
                                )
                            }
                        })
                        count++
                    }

                } else if (count == 1) { // if its 1 content for subject
                    tts.speak(
                        " Subject of the mail is " + mailSubject.text.toString() +
                                " ,to confirm tap on bottom left of the screen, to speak again, tap on" +
                                "bottom right of the screen",
                        TextToSpeech.QUEUE_ADD, null
                    )
                    buttonConfirm2.setOnClickListener {
                        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                            if (it == TextToSpeech.SUCCESS) {
                                tts.language = Locale.US
                                tts.setSpeechRate(0.8f)
                                tts.speak(
                                    " You confirmed subject. To add Body, tap on bottom right of the screen",
                                    TextToSpeech.QUEUE_ADD, null
                                )
                            }
                        })
                        count++
                    }
                } else { // every other will be concatenated in body
                    tts.speak(
                        "Body of the mail is " + mailBody.text.toString() + ". To send long press the bottom left of the screen, to add press bottom right on the screen",
                        TextToSpeech.QUEUE_ADD, null
                    )
                    buttonConfirm2.setOnLongClickListener(OnLongClickListener {
                        Toast.makeText(this, "mail sent ", Toast.LENGTH_SHORT).show()
                        val intent=Intent(this,secondScreen::class.java)
                        startActivity(intent)
                        false



                    })

                }
            }

        })}
    // Code for speech recognition on the app same for all, it calls onActivityResult
    private fun speechrecog(){
        if(!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this, "Speech not available", Toast.LENGTH_SHORT).show()
        }else{
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak")
            getResult.launch(i)
        }
    }
}