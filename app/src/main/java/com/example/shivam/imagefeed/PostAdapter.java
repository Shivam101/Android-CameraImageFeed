package com.example.shivam.imagefeed;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostAdapter extends BaseAdapter {

    static Context context;
    static int layoutResourceId;
    ArrayList<String> uri = new ArrayList<String>();
    ArrayList<String> coordinate = new ArrayList<String>();
    ArrayList<String> address = new ArrayList<String>();

    public PostAdapter(Context c,int layoutResourceId,ArrayList<String> uri,ArrayList<String> coordinate,ArrayList<String> address)
    {

        this.context = c;
        this.layoutResourceId = layoutResourceId;
        this.uri = uri;
        this.coordinate = coordinate;
        this.address = address;
    }

    @Override
    public int getCount() {
        return uri.size();
    }

    @Override
    public Object getItem(int position) {
        return uri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PostHolder holder = null;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PostHolder();
            holder.postImage = (ImageView)row.findViewById(R.id.listImage);
            holder.txtTitle2 = (TextView)row.findViewById(R.id.listCoordinates);
            holder.txtTitle3 = (TextView)row.findViewById(R.id.listAddress);
            row.setTag(holder);
        }
        else
        {
            holder = (PostHolder)row.getTag();
        }

            Picasso.with(this.context).load(Uri.parse(uri.get(position)).toString()).resize(90,90).into(holder.postImage);
            holder.txtTitle2.setText(coordinate.get(position));
            holder.txtTitle3.setText(address.get(position));
        return row;
    }

    /*View holder design pattern to allow for recycling of list items*/
    static class PostHolder
    {
        ImageView postImage;
        TextView txtTitle2;
        TextView txtTitle3;
    }
}
