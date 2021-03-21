package com.example.androidproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BitmapAdapter extends BaseAdapter {

    private Context context = null;

    public BitmapAdapter(Context context) {
        this.context = context;
    }

    ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
    ArrayList<String> nameList = new ArrayList<String>();

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return bitmapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bitmapList.indexOf(getItem(position));
    }

    public void add(Bitmap image, String name) {
        bitmapList.add(image);
        nameList.add(name);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Bitmap image = (Bitmap)getItem(position);
        String name = nameList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.image, parent, false);
        }

        ImageView iv = (ImageView)convertView.findViewById(R.id.imageView);
        iv.setImageBitmap(image);

        TextView tv = (TextView)convertView.findViewById(R.id.nameTextView);
        tv.setText(name);

        return convertView;
    }

}
