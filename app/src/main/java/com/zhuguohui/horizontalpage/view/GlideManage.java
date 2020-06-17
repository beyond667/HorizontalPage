package com.zhuguohui.horizontalpage.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zhuguohui.horizontalpage.R;

/**
 * @author YanYang
 * created at 2019-06-13
 */
public class GlideManage {
    public static void load(ImageView view, String url) {
        if (url == null) return;

        if (url.endsWith(".gif")) {
            Glide.with(view.getContext())
                    .asGif()
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(view);
        } else {
            Glide.with(view.getContext())
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher)
                            .placeholder(R.mipmap.ic_launcher))
                    .load(url)
                    .into(view);
        }
    }

    public static void load(Context context, ImageView view, String url) {
        if (url == null) return;

        if (url.endsWith(".gif")) {
            Glide.with(context)
                    .asGif()
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(view);
        } else {
            Glide.with(context)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher)
                            .placeholder(R.mipmap.ic_launcher))
                    .load(url)
                    .into(view);
        }
    }


    public static void circlemage(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    public static void circlemage(Context context, ImageView imageView, String url, int placeholder, int errorResource) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()).error(errorResource).placeholder(placeholder))
                .into(imageView);
    }


}
