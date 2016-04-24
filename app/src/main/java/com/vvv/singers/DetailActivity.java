package com.vvv.singers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //включаем кнопку возврата домой (вверху)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle(getIntent().getExtras().getString("com.vvv.singers.TITLE"));
        // скачиваем обложку
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        // получаем адрес картинки
        imageLoader.displayImage(getIntent().getExtras().getString("com.vvv.singers.IMAGE"), (ImageView) findViewById(R.id.ivBig));
        //устанавливаем значения текстовых полей
        TextView tvGenres;
        tvGenres = (TextView) findViewById(R.id.tvDGenres);
        tvGenres.setText(getIntent().getExtras().getString("com.vvv.singers.GENRES"));
        TextView tvCreation;
        tvCreation = (TextView) findViewById(R.id.tvDCreation);
        tvCreation.setText(getIntent().getExtras().getString("com.vvv.singers.CREATION"));
        TextView tvDescription;
        tvDescription = (TextView) findViewById(R.id.tvDDescription);
        tvDescription.setText(getIntent().getExtras().getString("com.vvv.singers.DESCRIPTION"));
    }

    //добавляем к finish() анимацию
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    //переопределяем обработку нажатия кнопки домой (сверху) чтобы не пересоздавалась MainActivity и анимация отрабатывала
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

