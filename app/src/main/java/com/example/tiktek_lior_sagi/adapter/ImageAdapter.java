package com.example.tiktek_lior_sagi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.tiktek_lior_sagi.R;
import com.example.tiktek_lior_sagi.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    //adapter for images

    private static final String TAG = "ImageAdapter";

    private Context context;
    private List<String> imageList;
    private LayoutInflater inflater;

    public ImageAdapter(Context context) {
        this.context = context;
        this.imageList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    public void setItems(List<String> imageList) {
        this.imageList.clear();
        this.imageList.addAll(imageList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        String imageUrl = imageList.get(position);
        Log.i(TAG, "imageUrl: "+imageUrl);
        Log.d(TAG, "Base64 String: " + imageUrl);
        if (imageUrl == null) return convertView;

        ImageView imageView = convertView.findViewById(R.id.imageView);

        imageView.setImageBitmap(ImageUtil.convertFrom64base(imageUrl));


        return convertView;
    }
}
