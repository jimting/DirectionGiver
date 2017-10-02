package jt.directiongiver000;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SwitchFragment extends Fragment {
    private DLActivity jumpTool;
    private LinearLayout groupViewLl;
    private ViewPager viewPager;

    private ImageView[] imageViews;
    private ImageView imageView;
    private Button btn;

    private List<View> viewList = new ArrayList<View>();
    private LayoutInflater mInflater;

    public void setJumpTool(DLActivity tmp)
    {
        jumpTool = tmp;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View switchView = inflater.inflate(R.layout.fragment_switch, container,
                false);
        mInflater = inflater;

        groupViewLl = (LinearLayout) switchView.findViewById(R.id.viewGroup);
        viewPager = (ViewPager) switchView.findViewById(R.id.viewPager);

        return switchView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * 将需要滑动的View加入到viewList
         */
        View oneView = mInflater.inflate(R.layout.view_three, null);
        viewList.add(mInflater.inflate(R.layout.view_one, null));
        viewList.add(mInflater.inflate(R.layout.view_two, null));
        viewList.add(oneView);

        /**
         * 定义个圆点滑动导航ImageView，根据View的个数而定
         */
        imageViews = new ImageView[viewList.size()];

        btn = (Button) oneView.findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                //記錄使用者已經看過此教程導覽
                jumpTool.haveRead();

                //切換Activity
                Intent homeIntent = new Intent(jumpTool,MainActivity.class);
                startActivity(homeIntent);
                jumpTool.finish();
            }

        });
        for (int i = 0; i < viewList.size(); i++) {
            imageView = new ImageView(this.getActivity());
            imageView.setLayoutParams(new LayoutParams(40, 40));
            imageView.setPadding(40, 0, 40, 0);
            imageViews[i] = imageView;

            if (i == 0) {
                // 默认选中第一张图片
                imageViews[i]
                        .setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }

            groupViewLl.addView(imageViews[i]);
        }

                viewPager.setAdapter(new MyPagerAdapter(viewList));
        viewPager.setOnPageChangeListener(new SwitchPageChangeListener());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // 指引页面更改事件监听器，设置圆点滑动时的背景变化。
    class SwitchPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.drawable.page_indicator_focused);

                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }
        }
    }

}