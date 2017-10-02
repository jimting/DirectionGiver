package jt.directiongiver000;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends DGActivity implements AdapterView.OnItemClickListener ,GoogleApiClient.OnConnectionFailedListener,NavigationView.OnNavigationItemSelectedListener {

    List<View> lt_list = new ArrayList();
    int index;

    public static String[] start;
    public static String[] destination;
    public static String[] date;
    public static Double[] px;
    public static Double[] py;
    public static String[] bestLine;
    public static String[] userLine;
    private double userX;
    private double userY;
    private String userEmail;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("歷史紀錄");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }




    private void getHistory() {


        String url = "http://140.121.197.130:8100/HistoryServlet/ShowHistoryServlet?account="+userEmail;
        System.out.println(url);
        String url2 = new String();
        try {
            url2 = stringParser(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(url2);
        historyGetJson task = new historyGetJson();
        task.execute(new String[]{url2});

    }

    public String stringParser(String url) throws IOException {
        String url2 = new String();
        for (int j = 0; j < url.length(); j++) {
            if (url.substring(j, j + 1).matches("[\\u4e00-\\u9fa5]+")) {
                try {
                    url2 = url2 + URLEncoder.encode(url.substring(j, j + 1), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                url2 = url2 + url.substring(j, j + 1).toString();
            }
        }

        return url2;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),
                "查看" + start[position] + "的歷史紀錄", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setClass(HistoryActivity.this,HistoryMapsActivity.class);
        intent.putExtra("startname",start[position]);
        intent.putExtra("endname", destination[position]);//可放所有基本類別
        startActivity(intent);
    }

    public class historyGetJson extends AsyncTask<String, Void, String> implements AdapterView.OnItemClickListener {    //<doInBackground()傳入的參數, doInBackground() 執行過程中回傳給UI thread的資料, 傳回執行結果>


        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                output = getOutputFromUrl(url);
            }
            return output;        //傳給onPostExecute()
        }

        private String getOutputFromUrl(String url) {

            StringBuilder output = new StringBuilder("");
            try {
                InputStream stream = getHttpConnection(url);
                BufferedReader builder = new BufferedReader(new InputStreamReader(stream));
                output.append(builder.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(output.toString());
            return output.toString();
        }

        private InputStream getHttpConnection(String urlString) throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            System.out.println(url.toString());
            URLConnection connection = url.openConnection();
            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stream;
        }

        @Override
        protected void onPostExecute(String output) {
            System.out.println(output);
            if (!output.equals("[]")) {
                System.out.println("enterErr");
                getJSON(output);

            } else {
                // char_text.setText("找不到前往" + destination + "的路線！");
                // Speech("找不到前往" + destination + "的路線！");
                // roadInfoText.setText("找不到路線！");
                // initMap();
            }
        }

        private void getJSON(String jsonString) {

            System.out.println("StartGettingJson");
            JSONArray jsonArr = null;
            try {
                jsonArr = new JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            start = new String[jsonArr.length()];
            destination = new String[jsonArr.length()];
            date = new String[jsonArr.length()];
            px = new Double[jsonArr.length()];
            py = new Double[jsonArr.length()];
            bestLine = new String[jsonArr.length()];
            userLine = new String[jsonArr.length()];

            System.out.println(jsonArr.toString());
            for (int i = 0; i < jsonArr.length(); i++)
            {
                try
                {
                    // JSONObject modFamily = jsonArr.getJSONObject(i);
                    start[i] = jsonArr.getJSONObject(i).getString("start");
                    destination[i] = jsonArr.getJSONObject(i).getString("destination");
                    date[i] = jsonArr.getJSONObject(i).getString("date");
                    px[i] = jsonArr.getJSONObject(i).getDouble("px");
                    py[i] = jsonArr.getJSONObject(i).getDouble("py");
                    bestLine[i] = jsonArr.getJSONObject(i).getString("bestline");
                    userLine[i] = jsonArr.getJSONObject(i).getString("userline");

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }

            final double[] position = getGPS();
            userY = position[0];
            userX = position[1];

            ListView listview = (ListView) findViewById(R.id.listView1);
            seview();
            BaseAdapter adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return start.length;
                }

                @Override
                public Object getItem(int arg0) {
                    return null;
                }

                @Override
                public long getItemId(int arg0) {
                    return 0;
                }

                @Override
                public View getView(final int arg0, View arg1, ViewGroup arg2) {
                    index = arg0;
//紀錄按下的哪一個索引值
                    return lt_list.get(arg0);
                }
            };
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(this);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(),
                    "按鈕點擊了:" + start[position], Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(HistoryActivity.this,HistoryMapsActivity.class);
            intent.putExtra("startname", start[position]);
            intent.putExtra("endname", destination[position]);//可放所有基本類別
            intent.putExtra("bestLine", bestLine[position]);
            intent.putExtra("userLine", userLine[position]);
            intent.putExtra("px", px[position]);
            intent.putExtra("py", py[position]);
            startActivity(intent);

        }
    }

    public void seview() {
        for (int i = 0; i < start.length; i++) {
            View view = HistoryActivity.this.getLayoutInflater().inflate(
                    R.layout.list, null);
            ImageView imv = (ImageView) view.findViewById(R.id.imageView1);
            Bitmap bitmap = BitmapFactory
                    .decodeResource(getResources(), R.drawable.destination_ico);
            bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
// 圖片的長X寬
            imv.setImageBitmap(bitmap);
// 把圖片塞進去
            TextView tv_name = (TextView) view.findViewById(R.id.textView1);
            tv_name.setText(start[i]);
            TextView tv_name1 = (TextView) view.findViewById(R.id.textView2);
            tv_name1.setText(destination[i]);
            TextView tv_name2 = (TextView) view.findViewById(R.id.textView3);
            double tmp = GetDistance(userX,userY,px[i],py[i]);
            tmp = tmp / 1000;
            System.out.println(tmp);
            tv_name2.setText(String.valueOf(tmp) + "公里");
// 把資料塞進去textview

            lt_list.add(view);
        }
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
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    public double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double EARTH_RADIUS = 6378137;
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)+ Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    @Override
    protected void onStart(){
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){

            GoogleSignInAccount account = result.getSignInAccount();
            userEmail = account.getEmail();
            getHistory();

        } else {
            goLogInScreen();
        }
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, SetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
            HistoryActivity.this.finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}