package jt.directiongiver000;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.droidsonroids.gif.GifImageButton;

public class DGActivity extends AppCompatActivity
{
    protected boolean voice = true; //true是開 false是關,預設 = 開
    protected float speechRate = 1; //角色講話的語速，1為正常，0.5為慢，2為快
    protected float charactor = (float)0.5; //0.5=鹿憨，1=小鹿，2=瑞迪
    String voiceTmp; // 用來存放音量控制的資訊
    protected GifImageButton button;  //設定角色的圖案
    String charTmp; //用來存放角色的資訊
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //get current date time with Date()
    Date dateToday = new Date();
    private final int LOCATION_REQUEST_CODE = 2;
    private boolean serverStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dg);
        askPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.RECORD_AUDIO, LOCATION_REQUEST_CODE);
        serverStatus = functionList.checkServerStatus();
        if(!serverStatus)
        {

        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            this.finish();
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.mapmode)
        {
            Intent intent = new Intent();
            intent.setClass(DGActivity.this, MapsActivity.class);
            startActivity(intent);
            DGActivity.this.finish();
        }
        else if (id == R.id.history)
        {
            Intent intent = new Intent();
            intent.setClass(DGActivity.this, HistoryActivity.class);
            startActivity(intent);
            DGActivity.this.finish();
        }
        else if (id == R.id.set)
        {
            Intent intent = new Intent();
            intent.setClass(DGActivity.this, SetActivity.class);
            startActivity(intent);
            DGActivity.this.finish();
        }
        else if (id == R.id.aboutus)
        {
            Intent intent = new Intent();
            intent.setClass(DGActivity.this, AboutUsActivity.class);
            startActivity(intent);
            DGActivity.this.finish();

        }
        else if (id == R.id.share)
        {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody = "DirectionGiver 尋路-您的好基友" +
                    "https://goo.gl/IIYqgr";
            String shareSub = "DirectionGiver";
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(myIntent, "Share using"));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected void initCharactor()
    {
        String Message;
        try {
            FileInputStream fileInputStream = openFileInput("hello_file");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((Message = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(Message);
            }
            charTmp = stringBuffer.toString();

            //用來確認角色的int是否是正確的
            //Toast toast = Toast.makeText(this, charTmp, Toast.LENGTH_SHORT);
            //toast.show();

            //\鹿憨
            if (charTmp.equals(String.valueOf(R.drawable.luhen)))
            {
                charactor = (float)0.5;
                button = (GifImageButton) findViewById(R.id.charactor);
                button.setImageResource(R.drawable.luhen_gen);
            }
            //小鹿
            else if(charTmp.equals(String.valueOf(R.drawable.xiaolu)))
            {
                charactor = (float)1;
                button = (GifImageButton) findViewById(R.id.charactor);
                button.setImageResource(R.drawable.xiaolu_gen);
            }
            //瑞迪
            else
            {
                charactor = (float)2;
                button = (GifImageButton) findViewById(R.id.charactor);
                button.setImageResource(R.drawable.reindeer_gen);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void checkCharactor()
    {
        String Message;
        try {
            FileInputStream fileInputStream = openFileInput("hello_file");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((Message = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(Message);
            }
            charTmp = stringBuffer.toString();

            //鹿憨
            if (charTmp.equals(String.valueOf(R.drawable.luhen)))
            {
                charactor = (float)0.5;
            }
            //小鹿
            else if(charTmp.equals(String.valueOf(R.drawable.xiaolu)))
            {
                charactor = (float)1;
            }
            //其他的就是瑞迪
            else
            {
                charactor = (float)2;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void checkVoiceControl()
    {
        String Message;
        try {
            FileInputStream fileInputStream = openFileInput("voice_control");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((Message = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(Message);
            }
            voiceTmp = stringBuffer.toString();

            if (voiceTmp.equals("1"))
            {
                voice = false;
            }
            else
            {
                voice = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void askPermission(String permission,String audioPermission,int requsetCode){
        if(ContextCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{permission,audioPermission}, requsetCode);
        }
    }


}
