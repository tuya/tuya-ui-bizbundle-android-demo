package com.tuya.smart.bizubundle.panelmore.demo;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Device list adapter on a simple style.
 */
public class SimpleDevListAdapter extends RecyclerView.Adapter<SimpleDevListAdapter.SimpleDevViewHolder> {
    private List<SimpleDeviceBean> mData;

    private OnItemClickListener mOnItemClickListener;

    public SimpleDevListAdapter() {
    }

    @NonNull
    @Override
    public SimpleDevViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.demo_rv_item_simple_dev, parent, false);
        return new SimpleDevViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleDevViewHolder viewHolder, int position) {
        final SimpleDeviceBean bean = mData.get(position);
        if (bean == null) {
            return;
        }
        if (!TextUtils.isEmpty(bean.getIconUrl())) {
            Uri uri = Uri.parse(bean.getIconUrl());
            viewHolder.icon.setImageURI(uri);
        }
        viewHolder.title.setText(bean.getTitle());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(bean, viewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null && !mData.isEmpty() ? mData.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public void setData(List<SimpleDeviceBean> beans) {
        mData = beans;
        notifyDataSetChanged();
    }

    static class SimpleDevViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView icon;
        TextView title;

        public SimpleDevViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.simple_dev_item_icon);
            title = itemView.findViewById(R.id.simple_dev_item_title);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SimpleDeviceBean item, int position);
    }
}
