package com.tuya.smart.bizubundle.panel.demo;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.smart.api.MicroContext;
import com.thingclips.smart.api.service.MicroServiceManager;
import com.thingclips.smart.clearcache.api.ClearCacheService;
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.home.sdk.bean.HomeBean;
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback;
import com.thingclips.smart.panelcaller.api.AbsPanelCallerService;
import com.thingclips.smart.sdk.bean.DeviceBean;
import com.thingclips.smart.sdk.bean.GroupBean;
import com.thingclips.smart.utils.ProgressUtil;

import java.util.ArrayList;
import java.util.List;

public class PanelActivity extends AppCompatActivity {

    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.inflateMenu(R.menu.panel_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.panel_clear) {
                    ClearCacheService service = MicroContext.getServiceManager().findServiceByInterface(ClearCacheService.class.getName());
                    if (service != null) {
                        service.clearCache(PanelActivity.this);
                    }
                    return true;
                }
                return false;
            }
        });
        toolbar.setTitle("Panel");
        RecyclerView homeRecycler = findViewById(R.id.home_recycler);
        homeRecycler.setLayoutManager(new LinearLayoutManager(this));
        homeRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new HomeAdapter();
        homeRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemBean bean, int position) {
                AbsPanelCallerService service = MicroContext.getServiceManager().findServiceByInterface(AbsPanelCallerService.class.getName());
                if (bean.getGroupId() != 0) {
                    boolean isAdmin = null != ThingHomeSdk.getDataInstance().getHomeBean(getService().getCurrentHomeId())
                            && ThingHomeSdk.getDataInstance().getHomeBean(getService().getCurrentHomeId()).isAdmin();
                    service.goPanelWithCheckAndTip(PanelActivity.this, bean.getGroupId(), isAdmin);
                } else {
                    service.goPanelWithCheckAndTip(PanelActivity.this, bean.devId);
                }
            }
        });
        getCurrentHomeDetail();
    }

    /**
     * you must implementation AbsBizBundleFamilyService
     *
     * @return AbsBizBundleFamilyService
     */
    private AbsBizBundleFamilyService getService() {
        return MicroServiceManager.getInstance().findServiceByInterface(AbsBizBundleFamilyService.class.getName());
    }

    private void getCurrentHomeDetail() {
        ProgressUtil.showLoading(this, "Loading...");
        long homeid = getService().getCurrentHomeId();
        ThingHomeSdk.newHomeInstance(homeid).getHomeDetail(new IThingHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean homeBean) {
                List<ItemBean> beans = new ArrayList<>(8);
                for (GroupBean groupBean : homeBean.getGroupList()) {
                    beans.add(getItemBeanFromGroup(groupBean));
                }
                for (DeviceBean deviceBean : homeBean.getDeviceList()) {
                    beans.add(getItemBeanFromDevice(deviceBean));
                }
                mAdapter.setData(beans);
                ProgressUtil.hideLoading();
            }

            @Override
            public void onError(String s, String s1) {
                ProgressUtil.hideLoading();
                Toast.makeText(PanelActivity.this, s + "\n" + s1, Toast.LENGTH_LONG).show();
            }
        });
    }

    private ItemBean getItemBeanFromGroup(GroupBean groupBean) {
        ItemBean itemBean = new ItemBean();
        itemBean.setGroupId(groupBean.getId());
        itemBean.setTitle(groupBean.getName());
        itemBean.setIconUrl(groupBean.getIconUrl());
        return itemBean;
    }

    private ItemBean getItemBeanFromDevice(DeviceBean deviceBean) {
        ItemBean itemBean = new ItemBean();
        itemBean.setDevId(deviceBean.getDevId());
        itemBean.setIconUrl(deviceBean.getIconUrl());
        itemBean.setTitle(deviceBean.getName());
        return itemBean;
    }

    public static class ItemBean {
        private String devId;
        private long groupId;
        private String iconUrl;
        private String title;

        public String getDevId() {
            return devId;
        }

        public void setDevId(String devId) {
            this.devId = devId;
        }

        public long getGroupId() {
            return groupId;
        }

        public void setGroupId(long groupId) {
            this.groupId = groupId;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
