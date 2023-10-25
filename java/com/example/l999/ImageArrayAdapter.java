package com.example.l999;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ImageArrayAdapter extends ArrayAdapter<Integer> {
    private Integer[] images;

    public ImageArrayAdapter(Context context, Integer[] images) {
        super(context, R.layout.image_item, images);
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView imageView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_item, null);
            imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(new ViewHolder(imageView));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            imageView = viewHolder.imageView;
        }
        imageView.setImageResource(images[position]);
        return convertView;
    }

    static class ViewHolder {
        final ImageView imageView;

        ViewHolder(ImageView imageView) {
            this.imageView = imageView;
        }
    }
}
