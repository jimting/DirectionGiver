package jt.directiongiver000;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;


public class MainActivity extends RPGConversationActivity  {
    EditText destination;
    Button toMapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        destination = (EditText)findViewById(R.id.destination);
        button = (GifImageButton) findViewById(R.id.charactor);
        toMapButton = (Button)findViewById(R.id.toMapButton);


        Toast toast = Toast.makeText(this, "哈囉！好久不見！", Toast.LENGTH_SHORT);
        toast.show();

        //設定角色
        super.initCharactor();
        //設定音量控制
        super.checkVoiceControl();

        /*final MediaController mc = new MediaController(this);
        mc.setMediaPlayer((GifDrawable) button.getDrawable());
        mc.setAnchorView(button);*/

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //STT();
                speech.startListening(recognizerIntent);
            }
        });

        toMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapsActivity.class);
                String file_name = "destination";
                String destinationString = destination.getText().toString();

                try {
                    FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
                    fileOutputStream.write(destinationString.getBytes());
                    fileOutputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        Speech("哈囉！好久不見！");

    }

    @Override
    void RPGConversation(ArrayList<String> text)
    {final String tmpText = text.get(0);
        Thread thread = new Thread(new Runnable() {
            public void run() {
                //使用API.AI分析使用者講的話
                String url = "http://140.121.197.130:8100/AIConversation/AIConversationServlet?userInput=" + tmpText;
                System.out.println(url);
                InputStream stream = null;
                StringBuilder output = new StringBuilder("");
                try {
                    url = stringParser(url);
                    URL url2 = new URL(url);
                    URLConnection connection = url2.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    httpConnection.setRequestMethod("GET");
                    httpConnection.connect();
                    if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        stream = httpConnection.getInputStream();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BufferedReader builder = null;
                try {
                    builder = new BufferedReader(new InputStreamReader(stream, "utf-8"));
                    String tmpText = null;
                    while ((tmpText = builder.readLine()) != null) {
                        System.out.println(tmpText);
                        output.append(tmpText);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] tokens = output.toString().split(";");
                int tokenTop = 0;
                String status = null;
                String text1 = null;
                String text2 = null;
                for (String token : tokens) {
                    switch (tokenTop) {
                        case 0:
                            status = token;
                            break;
                        case 1:
                            text1 = token;
                            break;
                        case 2:
                            text2 = token;
                            break;
                    }
                    tokenTop++;
                }
                if (status.equals("weatherNothing"))
                {
                    String date = dateFormat.format(dateToday);
                    double[] position = getGPS();
                    Geocoder gc = new Geocoder(MainActivity.this, Locale.TRADITIONAL_CHINESE); 	//地區:台灣
                    List<Address> lstAddress = null;
                    try {
                        lstAddress = gc.getFromLocation(position[0], position[1], 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final String returnAddress = lstAddress.get(0).getAddressLine(0);
                    String location = returnAddress.substring(5,8);
                    String result = null;
                    try {
                        result = functionList.getWeather(location,date);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Speech(result);
                }
                else if (status.equals("weatherNotDate"))
                {
                    String date = dateFormat.format(dateToday);
                    String result = null;
                    try {
                        result = functionList.getWeather(text1,date);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Speech(result);
                }
                else if (status.equals("whereToGo"))
                {
                    final String tmpText1 = text1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, MapsActivity.class);
                            String file_name = "destination";
                            String destinationString = tmpText1;
                            try
                            {
                                FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
                                fileOutputStream.write(destinationString.getBytes());
                                fileOutputStream.close();
                            } catch (FileNotFoundException e)
                            {
                                e.printStackTrace();
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            startActivity(intent);
                        }
                    });
                } else if (status.equals("weatherNotLocation"))
                {
                    double[] position = getGPS();
                    Geocoder gc = new Geocoder(MainActivity.this, Locale.TRADITIONAL_CHINESE); 	//地區:台灣
                    List<Address> lstAddress = null;
                    try {
                        lstAddress = gc.getFromLocation(position[0], position[1], 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final String returnAddress = lstAddress.get(0).getAddressLine(0);
                    System.out.println("查詢地址:"+returnAddress);
                    String location = returnAddress.substring(5,8);
                    System.out.println("查詢資料:"+location);
                    String result = null;
                    try {
                        result = functionList.getWeather(location,text1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Speech(result);
                } else if (status.equals("whatIsShopData")) {
                    Speech("找尋" + text1 + "的商家介紹");
                } else if (status.equals("defaultWelcomeIntent")) {
                    Speech(text1);
                } else if (status.equals("howManyTime")) {
                    Speech("你要先導航才能問多久呀！");
                } else if (status.equals("weatherComposite"))
                {
                    String result = null;
                    try {
                        result = functionList.getWeather(text1,text2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Speech(result);
                }
                else if(status.equals("filterDirtyWords"))
                {
                    Speech("喂！不要說髒話！");
                }
                else if(status.equals("goToilet"))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, MapsActivity.class);
                            String file_name = "toilet";
                            String tmpFileContent = "我想去廁所";
                            try
                            {
                                FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
                                fileOutputStream.write(tmpFileContent.getBytes());
                                fileOutputStream.close();
                            }
                            catch (FileNotFoundException e)
                            {
                                e.printStackTrace();
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            startActivity(intent);
                        }
                    });
                }
                else//聽不懂
                {
                    Speech(text1);
                }
            }
        });
        thread.start();
    }
    private double[] getGPS()
    {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);


/* 迴圈讀取providers,如果有位址資訊, 退出迴圈*/
        Location l = null;


        for (int i = providers.size() - 1; i >= 0; i--) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }


        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude(); //緯度
            gps[1] = l.getLongitude();//經度
        }
        return gps;
    }
}
