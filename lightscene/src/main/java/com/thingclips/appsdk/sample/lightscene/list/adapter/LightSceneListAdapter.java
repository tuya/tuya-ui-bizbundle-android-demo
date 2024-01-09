/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Tuya Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.thingclips.appsdk.sample.lightscene.list.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.appsdk.sample.lightscene.R;
import com.thingclips.appsdk.sample.lightscene.list.LightSceneListActivity;
import com.thingclips.light.android.callback.IThingLightResultCallback;
import com.thingclips.light.android.scene.bean.ThingLightSceneBean;
import com.thingclips.sdk.scene.ThingLightSceneSdk;
import com.thingclips.smart.android.common.utils.L;
import com.thingclips.smart.api.MicroContext;
import com.thingclips.smart.api.service.MicroServiceManager;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.bean.RoomBean;
import com.thingclips.smart.light.scene.api.bean.LightSceneDetailBean;
import com.thingclips.smart.light.scene.plug.api.AbsLightScenePlugService;

import java.util.ArrayList;

/**
 * Home List Adapter
 *
 * @author chuanfeng <a href="mailto:developer@tuya.com"/>
 * @since 2021/2/21 10:02 AM
 */
public class LightSceneListAdapter extends RecyclerView.Adapter<LightSceneListAdapter.ViewHolder> {

    public ArrayList<ThingLightSceneBean> data = new ArrayList<>();
    private Context context;

    public LightSceneListAdapter(LightSceneListActivity lightSceneListActivity) {
        this.context = lightSceneListActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_list, parent, false));

        holder.ivIcon.setImageResource(R.drawable.scene_schedule_arrow_right);
        holder.itemView.setOnClickListener(v -> {
            // lightScene Detail
            ThingLightSceneBean tuyaLightSceneBean = data.get(holder.getAdapterPosition());
            AbsLightScenePlugService sceneLightingService = MicroContext.findServiceByInterface(AbsLightScenePlugService.class.getName());
            if (null != sceneLightingService) {
                sceneLightingService.launchLightSceneEdit(v.getContext(), new LightSceneDetailBean(tuyaLightSceneBean), 101);
            }
        });

        holder.btnExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThingLightSceneBean tuyaLightSceneBean = data.get(holder.getAdapterPosition());
                AbsBizBundleFamilyService familyService = MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
                long homeId = 0;
                if (null != familyService) {
                    homeId = familyService.getCurrentHomeId();
                }
                ThingLightSceneSdk.newLightSceneInstance(tuyaLightSceneBean.getCode()).executeLightScene(homeId, new IThingLightResultCallback<Boolean>() {

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Toast.makeText(context, "execute success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String code, String detail) {
                        Toast.makeText(context, "execute error->" + detail, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThingLightSceneBean bean = data.get(position);
        String roomName = "";
        try {
            String parentRegionId = bean.getParentRegionId();
            RoomBean roomBean = ThingHomeSdk.getDataInstance().getRoomBean(Long.parseLong(parentRegionId));
            if (roomBean != null) {
                roomName = roomBean.getName();
            }
        } catch (Exception e) {
            L.e("light_scene", e.getMessage());
        }
        if (!TextUtils.isEmpty(roomName)) {
            holder.tvName.setText(roomName + "-" + bean.getName());
        } else {
            holder.tvName.setText(bean.getName());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final Button btnExecute;
        private final ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            btnExecute = itemView.findViewById(R.id.btnExecute);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }

    }
}
