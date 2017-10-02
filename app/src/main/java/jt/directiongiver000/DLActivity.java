package jt.directiongiver000;

/**
 * Created by lp123 on 2017/9/25.
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DLActivity extends AppCompatActivity {

    private boolean isSwitch = true;//true:进入SwitchFragment,false:进入OtherFragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dl_page);


    }

    @Override
    public void onResume(){
        super.onResume();
        switchFragment();
    }

    /**
     * Fragment界面切换
     */
    private void switchFragment()
    {
        SwitchFragment fragment = null;
        if(isSwitch){
            fragment = new SwitchFragment();
            fragment.setJumpTool(this);
        }
        isSwitch = !isSwitch;
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_switch_ll, fragment);
        ft.commitAllowingStateLoss();
    }
    public void haveRead()
    {
        String file_name = "DL";
        String Message = "看過導覽啦";
        try {
            FileOutputStream fileOutputStream = openFileOutput(file_name,MODE_PRIVATE);
            fileOutputStream.write(Message.getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
