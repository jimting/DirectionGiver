package jt.directiongiver000;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pl.droidsonroids.gif.GifImageButton;

public class SetActivity extends DGActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
    final static int[] imagesId = new int[]{R.drawable.luhen, R.drawable.xiaolu, R.drawable.reindeer};
    ImageSwitcher imageSwitcher;
    Gallery gallery;
    private LinearLayout Prof_section;
    private Button SignOut;
    private SignInButton SignIn;
    private TextView Name, Email;
    private ImageView Prof_pic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private Switch voiceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("設定");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Prof_section = (LinearLayout) findViewById(R.id.prof_section);
        SignOut = (Button) findViewById(R.id.bn_logout);
        SignIn = (SignInButton)findViewById(R.id.bn_login);
        Name = (TextView)findViewById(R.id.name);
        Email = (TextView)findViewById(R.id.email);
        Prof_pic = (ImageView)findViewById(R.id.prof_pic);
        SignIn.setOnClickListener(this);
        SignOut.setOnClickListener(this);
        Prof_section.setVisibility(View.GONE);
        voiceButton = (Switch)findViewById(R.id.voiceButton);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        gallery = (Gallery) findViewById(R.id.gallery);

        //確認語音控制資訊
        super.checkVoiceControl();
        //確認角色資訊
        super.checkCharactor();

        //設定音量開關
        voiceButton.setChecked(voice);
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //將資訊寫到開關資訊中
                String Message;
                if(voiceButton.isChecked())
                {
                    Message = String.valueOf(0);
                }
                else
                {
                    Message = String.valueOf(1);
                }
                String file_name = "voice_control";

                try {
                    FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
                    fileOutputStream.write(Message.getBytes());
                    fileOutputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        //為ImageSwitcher設置ViewFactory，用來處理圖片切換的顯示
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());

                return imageView;
            }
        });

        //為ImageSwitcher設置淡入淡出動畫
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));
        imageSwitcher.setAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_out));


        //為Gallery設置Adapter以讓Gallery顯示圖片
        gallery.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return imagesId.length;
            }

            @Override
            public Object getItem(int position) {
                return imagesId[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(imagesId[position]);
                //設定圖片尺寸等比例縮放
                imageView.setAdjustViewBounds(true);
                imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.WRAP_CONTENT));
                return imageView;
            }
        });

        //為Gallery設置一個OnItemSelectedListener，置於中間的縮圖為被Selected
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //將對應選到的縮圖的大圖放置於ImageSwitcher中
                imageSwitcher.setImageResource(imagesId[position]);
                String Message = String.valueOf(imagesId[position]);
                String file_name = "hello_file";

                try {
                    FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
                    fileOutputStream.write(Message.getBytes());
                    fileOutputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //什麼也不必做
            }
        });

        //設定已選擇的角色
        if(charactor == 0.5)//如果是鹿憨
        {
            //什麼也不做
        }
        else if(charactor == 1)//如果是小鹿
        {
            //把小鹿和鹿憨做交換
            int tmp = imagesId[0];
            imagesId[0] = imagesId[1];
            imagesId[1] = tmp;
        }
        else//不然就是瑞迪
        {
            //把瑞迪和鹿憨做交換
            int tmp = imagesId[0];
            imagesId[0] = imagesId[2];
            imagesId[2] = tmp;
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(SetActivity.this, MainActivity.class);
            startActivity(intent);
            SetActivity.this.finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bn_login:
                SignIn();
                break;

            case R.id.bn_logout:
                LogOut();
                break;
        }
    }

    private void SignIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    private void LogOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updataUI(false);
            }
        });
    }

    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            Name.setText(name);
            Email.setText(email);
            Glide.with(this).load(account.getPhotoUrl()).into(Prof_pic);
            updataUI(true);
        } else{
            updataUI(false);
        }
    }

    private void updataUI(boolean isLogin){
        if(isLogin){
            Prof_section.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
        } else {
            Prof_section.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
    @Override
    protected void onStart(){
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleResult(googleSignInResult);
                }
            });
        }
    }
}
