package com.example.tiktek_lior_sagi.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.tiktek_lior_sagi.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<ImageAnswerItem> imageList;
    private LayoutInflater inflater;

    public ImageAdapter(Context context, List<ImageAnswerItem> imageList) {
        this.context = context;
        this.imageList = imageList;
        this.inflater = LayoutInflater.from(context);
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

        ImageView imageView = convertView.findViewById(R.id.imageView);
        String imageUrl = imageList.get(position).getImageUrl();


        return convertView;
    }
}
