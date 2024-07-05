package com.tuya.smart.bizubundle.control.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.smart.api.service.MicroServiceManager;
import com.thingclips.smart.bizbundle.initializer.BizBundleInitializer;
import com.thingclips.smart.control.PluginControlService;
import com.thingclips.smart.control.plug.api.IPluginControlService;
import com.thingclips.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Device list adapter on a simple style.
 */
public class SimpleDevListAdapter extends RecyclerView.Adapter<SimpleDevListAdapter.SimpleDevViewHolder> {
    private List<DeviceBean> mData = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    private Context context;
    private IPluginControlService pluginControlService;

    public SimpleDevListAdapter(Context context) {
        this.context = context;
        BizBundleInitializer.registerService(IPluginControlService.class, new PluginControlService());
        pluginControlService = MicroServiceManager.getInstance().findServiceByInterface(IPluginControlService.class.getName());
    }

    @NonNull
    @Override
    public SimpleDevViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_simple_dev, parent, false);
        return new SimpleDevViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleDevViewHolder viewHolder, int position) {
        final DeviceBean bean = mData.get(position);
        if (bean == null) {
            return;
        }
        viewHolder.tvName.setText(bean.getName());

        String devId = bean.getDevId();
        Boolean bool = pluginControlService.isDeviceSupportMultiControl(devId);
        if (bool) {
            viewHolder.tvOnline.setText("支持多控关联");
        } else {
            viewHolder.tvOnline.setText("不支持多控关联");
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

    public void setData(List<DeviceBean> beans) {
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
        void onItemClick(DeviceBean item, int position);
    }
}
