package com.journaldev.mvpdagger2.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.journaldev.mvpdagger2.Data.ItemPhotoData;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.myVIew.zoomImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.LinkedList;

import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class ImagesPageAdapter extends PagerAdapter {

    private final LinkedList<ItemPhotoData> imageUri;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    public ImagesPageAdapter(Context context, LinkedList<ItemPhotoData> images) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageUri = images;
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.zoomimage, container, false);

        zoomImageView imageView = itemView.findViewById(R.id.picture);
        imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        File file = new File(String.valueOf(imageUri.get(position).getPhoto()));
        Uri uri = Uri.fromFile(file);

        if (uri.toString().contains("content"))
            uri = imageUri.get(position).getPhoto();

        Picasso.with(mContext)
                .load(uri)
                .into(imageView);
        container.addView(itemView);

        return itemView;
    }


    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);
    }
}