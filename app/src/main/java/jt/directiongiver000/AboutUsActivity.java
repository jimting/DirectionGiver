package jt.directiongiver000;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class AboutUsActivity extends DGActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("關於我們");

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


        Button button = (Button) findViewById(R.id.reportbutton);
        button.setOnClickListener(this);

    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(AboutUsActivity.this, MainActivity.class);
            startActivity(intent);
            AboutUsActivity.this.finish();
        }
    }

    @Override
    public void onClick(View v) {


        Toast.makeText(getApplicationContext(), "感謝您的留言", Toast.LENGTH_SHORT).show();
        EditText editText = (EditText) findViewById(R.id.reportText);
        final String report = String.valueOf(editText.getText()).replace(" ","_");
        System.out.println(report);
        editText.setText("");
        Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
                String url = "http://140.121.197.130:8100/ErrorServlet/WriteInServlet?account="+email+"&errorMsg="+report;
                //            140.121.197.130:8100/ErrorServlet/WriteInServlet?account=123&errorMsg=sadjado
                System.out.println(url);
                StringBuilder output = new StringBuilder("");
                InputStream stream;
                try {
                    url = stringParser(url);
                    URL url2 = new URL(url);
                    URLConnection connection = url2.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    httpConnection.setRequestMethod("GET");
                    httpConnection.connect();
                    stream = httpConnection.getInputStream();
                    BufferedReader builder = new BufferedReader(new InputStreamReader(stream,"utf-8"));
                    output.append(builder.readLine());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
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
            email = account.getEmail();

        } else {
            goLogInScreen();
        }
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, SetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
