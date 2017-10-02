package jt.directiongiver000;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lp123 on 2017/1/17.
 */

public class Intro extends AppCompatActivity
{
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //先讀取紀錄，看有沒有看過導覽
                String Message;
                try {
                    FileInputStream fileInputStream = openFileInput("DL");
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    if ((bufferedReader.readLine()) != null) //如果不是null 就代表看過了
                    {
                        //直接跳到MainActivity
                        Intent homeIntent = new Intent(Intro.this,MainActivity.class);
                        startActivity(homeIntent);
                        Intro.this.finish();
                    }
                }
                catch (FileNotFoundException e)
                {
                    //不然就跳到導覽頁面
                    Intent homeIntent = new Intent(Intro.this,DLActivity.class);
                    startActivity(homeIntent);
                    Intro.this.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },SPLASH_TIME_OUT);
    }
}
