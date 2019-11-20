package com.journaldev.mvpdagger2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.journaldev.mvpdagger2.data.AppPreference;
import com.journaldev.mvpdagger2.data.ItemPhotoData;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.utils.GlideUtils;

import java.io.File;
import java.util.LinkedList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class ImagesPageAdapter extends PagerAdapter {

    private final LinkedList<ItemPhotoData> imageUri;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private PagerClickListener listener;
    private int current;

    public interface PagerClickListener {
        void setStartPostTransition(View view);
    }

    public ImagesPageAdapter(Context context, LinkedList<ItemPhotoData> images, PagerClickListener listener, int current ) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageUri = images;
        this.current = current;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return imageUri.size();
    }

    public ItemPhotoData getCurrentUri(int position) {
        return imageUri.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.zoomimage, container, false);
        final ImageViewTouch imageView = itemView.findViewById(R.id.picture);
        imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        Uri uri =getCurrectUri(imageUri.get(position).getPhoto());
        setTransitionName(position,imageView);
        viewImage(uri,position,imageView);
        container.addView(itemView);
        return itemView;
    }

    private void viewImage(Uri uri , final int position , final ImageViewTouch imageView ){
        Glide.with(mContext)
                .load(uri)
                .apply(setIsCache())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (position == current && AppPreference.getIsAnim())
                            listener.setStartPostTransition(imageView);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (position == current && AppPreference.getIsAnim())
                            listener.setStartPostTransition(imageView);
                        return false;
                    }
                })
                .into(imageView);
    }


    private RequestOptions setIsCache()
    {
        RequestOptions options = new RequestOptions();

        if (!AppPreference.getIsCache())
            GlideUtils.optionsCleanCache(options);

        return options;
    }

    private void setTransitionName(int position ,ImageViewTouch imageView )
    {
        String name = mContext.getString(R.string.transition_name, position);
        imageView.setTransitionName(name);
    }

    private Uri getCurrectUri(Uri uri){
        Uri currectUri;
        File file = new File(uri.toString());

        if (uri.toString().contains("content"))
            currectUri = uri;
        else
            currectUri = Uri.fromFile(file);

        return currectUri;
    }


    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);
    }
}