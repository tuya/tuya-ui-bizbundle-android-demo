package com.tuya.smart.bizubundle.demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thingclips.smart.optimus.sdk.ThingOptimusSdk;
import com.thingclips.smart.theme.open.IThingThemeManager;

public class ThemeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        IThingThemeManager manager = ThingOptimusSdk.getManager(IThingThemeManager.class);
        if (manager == null) return;

        TextView theme = findViewById(R.id.theme_color);
        StringBuilder sb = new StringBuilder("Theme: \n");
        sb.append("主题色(Theme color): ").append(Integer.toHexString(manager.getThemeColor())).append("\n");
        sb.append("警告颜色(Warning color): ").append(Integer.toHexString(manager.getWarningColor())).append("\n");
        sb.append("提示颜色(Tips color): ").append(Integer.toHexString(manager.getTipsColor())).append("\n");
        sb.append("引导颜色(Guide color): ").append(Integer.toHexString(manager.getGuideColor())).append("\n");
        sb.append("tab栏选中颜色(Tab Bar Selected Color): ").append(Integer.toHexString(manager.getTabBarSelectedColor())).append("\n");
        sb.append("背景色(Background color): ").append(Integer.toHexString(manager.getBackgroundColor())).append("\n");
        sb.append("导航栏背景色(Navigator Background color): ").append(Integer.toHexString(manager.getNavigatorBgColor())).append("\n");
        sb.append("底部Tab栏背景色(Bottom Tab Bar Background color): ").append(Integer.toHexString(manager.getBottomTabBarBgColor())).append("\n");
        sb.append("卡片背景色(Card Background color): ").append(Integer.toHexString(manager.getCardBgColor())).append("\n");
        sb.append("弹窗内容背景色(Alert Background color): ").append(Integer.toHexString(manager.getAlertBgColor())).append("\n");
        sb.append("弹窗蒙层色(Alert Mask color): ").append(Integer.toHexString(manager.getAlertMaskColor())).append("\n");
        sb.append("列表单元格背景颜色(List Cell Background color): ").append(Integer.toHexString(manager.getListCellBgColor())).append("\n");
        theme.setText(sb.toString());
    }
}
