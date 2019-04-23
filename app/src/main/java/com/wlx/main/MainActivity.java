package com.wlx.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.wlx.application.R;
import com.wlx.data.DataFragment;
import com.wlx.upload.UploadFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabItem ti_datas;
    private TabItem ti_upload;
    private ViewPager viewPage;
    private TabLayout tl_menu;
    private List<Fragment> frags;
    private List<String> titles;

    DataFragment dataFragment;
    UploadFragment uploadFragment;
    Myadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
    }

    private void initView() {
        viewPage = findViewById(R.id.vp);
        ti_datas = findViewById(R.id.ti_datas);
        ti_upload = findViewById(R.id.ti_upload);
        tl_menu = findViewById(R.id.tl_menu);
    }

    private void initFragment() {
        dataFragment = DataFragment.getIns();
        uploadFragment = UploadFragment.getIns();
        frags = new ArrayList<>();
        frags.add(dataFragment);
        frags.add(uploadFragment);
        titles=new ArrayList<>();
        titles.add("数据");
        titles.add("上传");
        tl_menu.setupWithViewPager(viewPage);

        adapter = new Myadapter(getSupportFragmentManager());
        adapter.setDatas(titles, frags);
        //联动
        viewPage.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (int i=0; i<frags.size(); i++){
            frags.get(i).onActivityResult(requestCode, resultCode, data);
        }
    }
}
