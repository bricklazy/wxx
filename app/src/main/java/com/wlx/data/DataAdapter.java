package com.wlx.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.wlx.application.R;
import com.wlx.utils.DateUtil;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter {

    private List<Data> list = new ArrayList<>();
    private Context context;
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if(viewType==1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_img, parent, false);
            return new ViewImgHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Data data = list.get(position);

        if(holder instanceof ViewImgHolder){
            ViewImgHolder viewHolder = (ViewImgHolder) holder;
            Glide.with(context).load(data.getMessage()).centerCrop().into(viewHolder.iv_cover);
            viewHolder.tvTime.setText(DateUtil.dateToString(DateUtil.YYYY_MM_DD_hh_mm_ss, data.getTime()));
        }else {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv_lable.setText(data.getMessage());
            viewHolder.tvTime.setText(DateUtil.dateToString(DateUtil.YYYY_MM_DD_hh_mm_ss, data.getTime()));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.click(position, data);
                return true;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        Data data = list.get(position);
        if(data.getMessageType()==2){
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setDatas(List<Data> data) {
        if(data!=null) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addDatas(List<Data> data) {
        if(data!=null) {
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTime;
        TextView tv_lable;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tv_lable = itemView.findViewById(R.id.tv_lable);
        }
    }

    static class ViewImgHolder extends RecyclerView.ViewHolder{

        TextView tvTime;
        ImageView iv_cover;

        public ViewImgHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            iv_cover = itemView.findViewById(R.id.iv_cover);
        }
    }

    interface OnItemClickListener{
        void click(int position, Data data);
    }
}
