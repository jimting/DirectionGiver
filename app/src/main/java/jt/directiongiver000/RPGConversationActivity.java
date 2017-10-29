package jt.directiongiver000;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageButton;

/**
 * Created by lp123 on 2017/5/30.
 */

public abstract class RPGConversationActivity extends DGActivity implements NavigationView.OnNavigationItemSelectedListener, RecognitionListener
{
    protected TextToSpeech tts;
    protected SpeechRecognizer speech = null;
    protected Intent recognizerIntent;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {
        super.onDestroy();
        // shutdown tts
        if (tts != null) {
            tts.stop();
            tts.shutdown();

        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Log.i("RPGConversationActivity", "destroy");
        }

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i("RPGConversationActivity", "onBeginningOfSpeech");
        //progressBar.setIndeterminate(false);
        //progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i("RPGConversationActivity", "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i("RPGConversationActivity", "onEndOfSpeech");
        //progressBar.setIndeterminate(true);
        //toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d("RPGConversationActivity", "FAILED " + errorMessage);
        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
        //toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i("RPGConversationActivity", "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i("RPGConversationActivity", "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i("RPGConversationActivity", "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i("RPGConversationActivity", "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        RPGConversation(matches);

        //destination.setText(matches.get(0).toString());
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i("RPGConversationActivity", "onRmsChanged: " + rmsdB);
        //progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "請再說一次";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    abstract void RPGConversation(ArrayList<String> text);

    protected void Speech(final String talk)
    {
        if(voice)
        {
            tts = new TextToSpeech(RPGConversationActivity.this, new TextToSpeech.OnInitListener() {
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = tts.setLanguage(Locale.CHINESE);
                        if (result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "This Language is not supported");
                        } else {
                            //設定角色語速
                            tts.setPitch(charactor);
                            tts.setSpeechRate(speechRate);
                        }
                        tts.stop();
                        tts.speak(talk, TextToSpeech.QUEUE_ADD, null);
                    }
                }
            });
        }
    }

}
