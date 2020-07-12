package com.zhuguohui.horizontalpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements PagingScrollHelper.onPageChangeListener {
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    PagingScrollHelper scrollHelper = new PagingScrollHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_main);
        init();
    }

    private LinearLayoutManager vLinearLayoutManager = null;
    private DividerItemDecoration vDividerItemDecoration = null;

    private void init() {
        vDividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        vLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myAdapter = new MyAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setAdapter(myAdapter);
        scrollHelper.setUpRecycleView(recyclerView);
        scrollHelper.setOnPageChangeListener(this);

        recyclerView.setLayoutManager(vLinearLayoutManager);
        recyclerView.addItemDecoration(vDividerItemDecoration);
        scrollHelper.updateLayoutManger();
        scrollHelper.scrollToPosition(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPageChange(int index) {
        Log.e("===","=====================第" + (index + 1) + "页");
    }


}
