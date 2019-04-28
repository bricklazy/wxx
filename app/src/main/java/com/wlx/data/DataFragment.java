package com.wlx.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wlx.application.R;
import com.wlx.base.BaseCallBack;
import com.wlx.base.BaseFragment;
import com.wlx.utils.CustomToast;
import com.wlx.utils.DisplayUtil;
import com.wlx.utils.OperateDialogUtil;

import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

/**
 * 数据展示
 */
public class DataFragment extends BaseFragment implements DataAdapter.OnItemClickListener {

    private DataModel dataModel = new DataModel();
    private LinearLayoutManager linearLayoutManagerl;
    private RecyclerView rv_datas;
    private DataAdapter dataAdapter;
    private SmartRefreshLayout refreshLayout;
    WindowManager wm;
    WindowManager.LayoutParams params;
    private View d_view;

    /**
     * 点击显示悬浮窗
     *
     * @param view
     */
    public void show(View view) {
        wm = (WindowManager) getContext().getApplicationContext().getSystemService(
                WINDOW_SERVICE); // 注意：这里必须是全局的context
        // 判断UI控件是否存在，存在则移除，确保开启任意次应用都只有一个悬浮窗
        if (view != null) {
            try {
                wm.removeView(view);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        params = new WindowManager.LayoutParams();
        // 系统级别的窗口
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.type = LAYOUT_FLAG;
        // 居中显示
        params.gravity = Gravity.CENTER;
        // 设置背景透明
        params.format = PixelFormat.TRANSPARENT;

        wm.addView(view, params);

    }

    public static DataFragment getIns() {
        DataFragment dataFragment = new DataFragment();
        return dataFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_data;
    }

    @Override
    public void initView() {

        d_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_img, null);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        rv_datas = view.findViewById(R.id.rv_datas);
        dataAdapter = new DataAdapter();
        linearLayoutManagerl = new LinearLayoutManager(getContext());
        dataAdapter.setOnItemClickListener(this);
        rv_datas.setAdapter(dataAdapter);
        rv_datas.setLayoutManager(linearLayoutManagerl);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                moreDatas();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                initDatas();
            }
        });

        initDatas();
    }

    private void initDatas() {
        dataModel.getDatas(new BaseCallBack<List<Data>>() {

            @Override
            public void success(List<Data> data) {
                dataAdapter.setDatas(data);
                refreshLayout.finishRefresh();
            }

            @Override
            public void faild(String msg) {
                CustomToast.showToast(getContext(), "获取数据失败："+msg);
                refreshLayout.finishRefresh();
            }
        });
    }

    private void moreDatas() {
        dataModel.getMoreDatas(new BaseCallBack<List<Data>>() {

            @Override
            public void success(List<Data> data) {
                dataAdapter.addDatas(data);
                refreshLayout.finishLoadMore();
            }

            @Override
            public void faild(String msg) {
                CustomToast.showToast(getContext(), "获取数据失败："+msg);
                refreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void click(int position, Data data) {

        if (Build.VERSION.SDK_INT >= 23)
        {
            if(!Settings.canDrawOverlays(getContext())){
                CustomToast.showToast(getContext(), "请打开允许显示在其他应用上层");
                 Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                 startActivity(intent);
                 return;
            } else {
                //绘ui代码, 这里说明6.0系统已经有权限了
            }
        }
        else
        {
            //绘ui代码,这里android6.0以下的系统直接绘出即可

        }


        if(data!=null&&data.getMessageType()==2){

            PhotoView pv_img = d_view.findViewById(R.id.pv_img);
            pv_img.setImageDrawable(null);
            pv_img.setOnClickListener(v -> {
                wm.removeView(d_view);
            });

            show(d_view);
            int pic_max_width = DisplayUtil.getScreenWidth(getContext());
            Glide.with(getContext()).load(data.getMessage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                   /* int widht = resource.getWidth();
                    int height = resource.getHeight();
                    if(widht>pic_max_width){
                        float multiple = ((float) widht)/pic_max_width+0.5f;
                        widht = (int) (widht/multiple);
                        height = (int) (height/multiple);
                    }
                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) iv_preview.getLayoutParams();
                    params.width = widht;
                    params.height = height;
                    iv_preview.setLayoutParams(params);*/
                    pv_img.setImageBitmap(resource);
                }
            });
        }

    }

    @Override
    public void longClick(int position, Data data) {
        OperateDialogUtil.getIns().showDelDialog(getContext(), "确定要删除此记录？", new OperateDialogUtil.DialogCallBack() {
            @Override
            public void ok() {
                dataModel.delMsg(data.getId(), new BaseCallBack<Integer>() {
                    @Override
                    public void success(Integer code) {
                        if(code==0){
                            dataAdapter.removeData(position);
                            CustomToast.showToast(getContext(), "删除成功");
                        }
                    }

                    @Override
                    public void faild(String msg) {
                        CustomToast.showToast(getContext(), "删除失败："+msg);
                    }
                });
            }

            @Override
            public void cancel() {

            }
        });
    }
}
