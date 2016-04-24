package com.vvv.singers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by vvv on 13.04.2016.
 */
public class SingersAdapter extends ArrayAdapter<SingerData> { // адаптер для класса SingerData

    private final Activity activity;
    private final ArrayList<SingerData> listSinger;

    // конструктор класса, принимает активность, листвью и массив данных
    public SingersAdapter(final Activity a, final int textViewResourceId, final ArrayList<SingerData> listSinger) {

        super(a, textViewResourceId, listSinger);
        this.listSinger = listSinger;
        activity = a;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        Controls controls;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_list, parent, false);
            controls = new Controls();
            // инициализируем нашу разметку
            controls.ivSmallCover = (ImageView) v.findViewById(R.id.smallCover);
            controls.tvName = (TextView) v.findViewById(R.id.tvName);
            controls.tvGenres = (TextView) v.findViewById(R.id.tvDGenres);
            controls.tvAlbums = (TextView) v.findViewById(R.id.tvAlbums);
            //controls.tvTracks = (TextView) v.findViewById(R.id.tvTracks);
            v.setTag(controls);
        } else {
            controls = (Controls) v.getTag();
        }
        SingerData singer = listSinger.get(position);
        if (singer != null) {
            // скачиваем маленькую обложку
            ImageLoader imageLoader = ImageLoader.getInstance();
            //imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
            imageLoader.displayImage(singer.getCoverSmall(), controls.ivSmallCover);
            // получаем текстовые элементы из объекта
            controls.tvName.setText(singer.getName());
            controls.tvGenres.setText(singer.getGenres());
            controls.tvAlbums.setText(singer.getCreation());
        }
        return v;
    }
    // для ускорения обращения к контролам  выносим контролы в отдельный класс
    private static class Controls {

        public ImageView ivSmallCover;
        public TextView tvName;
        public TextView tvGenres;
        public TextView tvAlbums;
    }

}
