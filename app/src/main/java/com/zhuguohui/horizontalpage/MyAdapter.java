package com.zhuguohui.horizontalpage;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyydjk.library.BannerLayout;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private static List<String> data = new ArrayList<>();
    private static int data_name = 0;

    public MyAdapter() {
        setData();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_user_item, parent, false);
        return new MyViewHolder(view);
    }


    private void setData() {
        int size = 70;
        for (int i = 1; i <= size; i++) {
            data.add(data_name + "-" + i + "");
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String title = data.get(position);
        holder.tv_title.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "item" + title + " 被点击了", Toast.LENGTH_SHORT).show();
            }
        });
        holder.scrollView.scrollTo(0, 0);
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            urls.add("https://social-vod.wimift.com/image/default/7D9C97DD4F5B4DFE9723474E1BD3A700-6-2.png");
        }
        holder.bannerLayout.setImageLoader(new GlideImageLoader());
        holder.bannerLayout.setViewUrls(urls);
        holder.bannerLayout.setAutoPlay(false);

        initChildAdapter(holder.recyclerView);
    }

    private void initChildAdapter(RecyclerViewAtRecycleView recycleView) {
        LinearLayoutManager ms = new LinearLayoutManager(recycleView.getContext());
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        recycleView.setLayoutManager(ms);
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            urls.add("https://social-vod.wimift.com/image/default/7D9C97DD4F5B4DFE9723474E1BD3A700-6-2.png");
        }
        ChildAdapter oap = new ChildAdapter(urls);
        recycleView.setAdapter(oap);
    }

    class ChildAdapter extends RecyclerView.Adapter<MyAdapter.ChildViewHolder> {
        private List<String> urls;

        public ChildAdapter(List<String> urls) {
            this.urls = urls;
        }

        @Override
        public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_user_child_item, parent, false);
            return new ChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChildViewHolder holder, int position) {
            final String url = data.get(position);
            GlideManage.load(holder.iv, url);
        }

        @Override
        public int getItemCount() {
            return urls.size();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ScrollView scrollView;
        RecyclerViewAtRecycleView recyclerView;
        BannerLayout bannerLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            scrollView = (ScrollView) itemView.findViewById(R.id.scroll);
            recyclerView = (RecyclerViewAtRecycleView) itemView.findViewById(R.id.rv);
            bannerLayout = (BannerLayout) itemView.findViewById(R.id.banner);
        }
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;

        public ChildViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }


    public void updateData() {
        data_name++;
        data.clear();
        setData();

    }
}
