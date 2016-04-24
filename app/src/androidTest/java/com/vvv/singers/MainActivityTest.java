package com.vvv.singers;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by vvv on 23.04.2016.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private ListView lv;
    private ImageView ivSmallCover;
    private TextView tvName;
    private TextView tvGenres;
    private TextView tvAlbums;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Получим активность
        mActivity = getActivity();
        lv = (ListView) mActivity.findViewById(R.id.listView);
        ivSmallCover = (ImageView) mActivity.findViewById(R.id.smallCover);
        tvName = (TextView) mActivity.findViewById(R.id.tvName);
        tvGenres = (TextView) mActivity.findViewById(R.id.tvDGenres);
        tvAlbums = (TextView) mActivity.findViewById(R.id.tvAlbums);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    //тест на создание контролов
    public void testControlsCreated() throws Exception {
        assertNotNull(mActivity);
        assertNotNull(lv);
        assertNotNull(ivSmallCover);
        assertNotNull(tvName);
        assertNotNull(tvGenres);
        assertNotNull(tvAlbums);
    }

    //тест на видимость контролов
    public void testControlsVisible() throws Exception {
        ViewAsserts.assertOnScreen(lv.getRootView(), lv);
        ViewAsserts.assertOnScreen(ivSmallCover.getRootView(), ivSmallCover);
        ViewAsserts.assertOnScreen(tvName.getRootView(), tvName);
        ViewAsserts.assertOnScreen(tvGenres.getRootView(), tvGenres);
        ViewAsserts.assertOnScreen(tvAlbums.getRootView(), tvAlbums);
    }
}
