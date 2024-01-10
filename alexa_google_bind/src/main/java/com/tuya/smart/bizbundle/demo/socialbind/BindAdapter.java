package com.tuya.smart.bizbundle.demo.socialbind;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.thingclips.smart.social.auth.manager.api.AuthorityBean;

import java.util.List;

public class BindAdapter extends Adapter<BindAdapter.ViewHolder> {
    private List<AuthorityBean> mData;
    private OnItemClickListener mOnItemClickListener;

    public BindAdapter() {
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bind_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final AuthorityBean bean = (AuthorityBean) this.mData.get(position);
        if (bean != null) {
            if (!TextUtils.isEmpty(bean.getPlatformName())) {
                viewHolder.title.setText(bean.getPlatformName());
            }


            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(bean, viewHolder.getAdapterPosition());
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return this.mData != null && !this.mData.isEmpty() ? this.mData.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public void setData(List<AuthorityBean> beans) {
        this.mData = beans;
        this.notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(AuthorityBean var1, int var2);
    }

    static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.tv_bind_title);
        }
    }
}
