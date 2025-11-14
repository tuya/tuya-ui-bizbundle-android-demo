package com.tuya.smart.bizubundle.demo;

import androidx.annotation.Keep;

/**
 * 应用层代理配置
 */
@Keep
public class AppConfig {
    /**
     * 主题色支持深色模式的开关
     */
    public static final boolean is_darkMode_support = true;
    /**
     * 华为包支持开关
     */
    public static final boolean is_huawei_pkg = false;

    /**
     * 热点配网支持的热点前缀列表
     */
    public static final String ap_mode_ssid = "SmartLife";
    /**
     * 是否支持蓝牙设备配网功能
     */
    public static final boolean is_need_ble_support = true;

    /**
     * 是否支持蓝牙 Mesh 设备配网功能
     */
    public static final boolean is_need_blemesh_support = true;

    /**
     * 是否支持列表页面右上角扫描配网功能
     */
    public static final boolean is_scan_support = true;

}
