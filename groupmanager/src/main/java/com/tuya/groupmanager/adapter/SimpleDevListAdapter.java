package com.tuya.groupmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.groupmanager.R;
import com.tuya.groupmanager.bean.SimpleDeviceBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Device list adapter on a simple style.
 */
public class SimpleDevListAdapter extends RecyclerView.Adapter<SimpleDevListAdapter.SimpleDevViewHolder> {
    private List<SimpleDeviceBean> mData = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    private boolean isDevice;
    private Context context;

    public SimpleDevListAdapter(Context context, boolean isDevice) {
        this.context = context;
        this.isDevice = isDevice;
    }

    @NonNull
    @Override
    public SimpleDevListAdapter.SimpleDevViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_simple_dev, parent, false);
        return new SimpleDevViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleDevViewHolder viewHolder, int position) {
        final SimpleDeviceBean bean = mData.get(position);
        if (bean == null) {
            return;
        }
        viewHolder.tvName.setText(bean.getName());

        if (isDevice) {
            if (bean.isOnLine()) {
                viewHolder.tvOnline.setText("在线");
            } else {
                viewHolder.tvOnline.setText("离线");
            }
        }

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
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public void setData(List<SimpleDeviceBean> beans) {
        mData.clear();
        mData.addAll(beans);
        notifyDataSetChanged();
    }

    static class SimpleDevViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvOnline;

        public SimpleDevViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvOnline = itemView.findViewById(R.id.tvOnline);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SimpleDeviceBean item, int position);
    }
}
