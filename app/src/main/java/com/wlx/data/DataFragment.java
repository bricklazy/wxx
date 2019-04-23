package com.wlx.data;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wlx.application.R;
import com.wlx.base.BaseCallBack;
import com.wlx.base.BaseFragment;
import com.wlx.utils.CustomToast;
import com.wlx.utils.OperateDialogUtil;

import java.util.List;

/**
 * 数据展示
 */
public class DataFragment extends BaseFragment implements DataAdapter.OnItemClickListener {

    private DataModel dataModel = new DataModel();
    private LinearLayoutManager linearLayoutManagerl;
    private RecyclerView rv_datas;
    private DataAdapter dataAdapter;
    private SmartRefreshLayout refreshLayout;

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
