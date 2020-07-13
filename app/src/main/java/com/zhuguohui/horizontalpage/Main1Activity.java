package com.zhuguohui.horizontalpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Main1Activity extends AppCompatActivity {



    private VerticalViewPager verticalViewPager;
    private final int[] imgSource = { R.drawable.p001, R.drawable.p002, R.drawable.p003, R.drawable.p001, R.drawable.p002, R.drawable.p003
    ,R.drawable.p001, R.drawable.p002, R.drawable.p003, R.drawable.p001, R.drawable.p002, R.drawable.p003
    ,R.drawable.p001, R.drawable.p002, R.drawable.p003, R.drawable.p001, R.drawable.p002, R.drawable.p003};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_main1);
        verticalViewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        List<View> viewList = new ArrayList<View>();
        for (int i = 0; i < imgSource.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.page_item, verticalViewPager,false);
            ImageView img = (ImageView) view.findViewById(R.id.iv);
            img.setImageResource(imgSource[i]);
            viewList.add(view);
        }
        verticalViewPager.setViewList(viewList);
    }


}
